package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import javax.annotation.Nullable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

abstract class AbstractStreamWriter implements StreamWriter<HttpObject> {

    private static final AtomicReferenceFieldUpdater<AbstractStreamWriter, SubscriptionImpl>
            subscriptionUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractStreamWriter.class,
                                                                         SubscriptionImpl.class,
                                                                         "subscription");

    protected static final EventExecutor DEFAULT_SUBSCRIBER_EXECUTOR = new NioEventLoopGroup().next();

    private final Queue<HttpObject> queue;
    @Nullable
    private volatile SubscriptionImpl subscription;

    protected AbstractStreamWriter() {
        queue = new ConcurrentLinkedQueue<>();
        subscription = null;
    }

    @Override
    public final void subscribe(Subscriber<? super HttpObject> subscriber, EventExecutor executor) {
        requireNonNull(subscriber, "subscriber");
        requireNonNull(executor, "executor");
        final var subscription = new SubscriptionImpl(subscriber, executor, queue);
        if (subscriptionUpdater.compareAndSet(this, null, subscription)) {
            if (subscription.inEventLoop()) {
                subscribe0(subscription);
            } else {
                subscription.executor().execute(() -> subscribe0(subscription));
            }
        } else {
            throw new IllegalStateException("this.subscription is not null (expected null)");
        }
    }

    private static void subscribe0(SubscriptionImpl subscription) {
        requireNonNull(subscription, "subscription");
        assert subscription.inEventLoop();
        subscription.subscriber().onSubscribe(subscription);
        subscription.doOnSubscribe();
    }

    @Override
    public final void write(HttpObject object) {
        requireNonNull(object, "object");
        queue.add(object);
        final var subscription = this.subscription;
        if (subscription == null) {
            return;
        }
        if (subscription.inEventLoop()) {
            subscription.notifySubscriber();
        } else {
            subscription.executor().execute(subscription::notifySubscriber);
        }
    }

    @Override
    public final void close() {
        write(CloseEvent.INSTANCE);
    }

    @Override
    public final void close(Throwable e) {
        write(new ErrorEvent(e));
    }

    @Override
    public final EventExecutor defaultSubscriberExecutor() {
        return DEFAULT_SUBSCRIBER_EXECUTOR;
    }

    private static final class CloseEvent implements HttpObject {

        static final CloseEvent INSTANCE = new CloseEvent();
    }

    private static final class ErrorEvent implements HttpObject {

        private final Throwable e;

        ErrorEvent(Throwable e) {
            this.e = requireNonNull(e, "e");
        }

        private Throwable e() {
            return e;
        }
    }

    private static final class SubscriptionImpl implements Subscription {

        private final Subscriber<? super HttpObject> downstream;
        private final EventExecutor executor;
        private final Queue<HttpObject> queue;
        private long demand;
        private boolean terminated;
        @Nullable
        private Throwable error;

        private SubscriptionImpl(Subscriber<? super HttpObject> subscriber, EventExecutor executor,
                                 Queue<HttpObject> queue) {
            downstream = requireNonNull(subscriber, "subscriber");
            this.executor = requireNonNull(executor, "executor");
            this.queue = requireNonNull(queue, "queue");
        }

        private void doOnSubscribe() {
            assert inEventLoop();
            if (error != null && !terminate()) {
                downstream.onError(error);
            }
        }

        @Override
        public void request(long n) {
            if (inEventLoop()) {
                request0(n);
            } else {
                executor.execute(() -> request0(n));
            }
        }

        private void request0(long n) {
            assert inEventLoop();
            if (n <= 0 && !terminate()) {
                subscriber().onError(new IllegalArgumentException("negative subscription request"));
                return;
            }
            if (demandGetAndAdd(n) > 0) {
                return;
            }
            notifySubscriber();
        }

        private void notifySubscriber() {
            assert inEventLoop();
            while (shouldProcess()) {
                final var object = queue.poll();
                assert object != null;
                if (processEvent(object)) {
                    return;
                }
                if (shouldSkip(object)) {
                    continue;
                }
                process(object);
            }
        }

        private boolean shouldProcess() {
            return demand > 0 && !queue.isEmpty() && !terminated;
        }

        private boolean processEvent(HttpObject object) {
            requireNonNull(object, "object");
            if (object instanceof CloseEvent && !terminate()) {
                downstream.onComplete();
                return true;
            }
            if (object instanceof ErrorEvent err && !terminate()) {
                downstream.onError(err.e());
                return true;
            }
            return false;
        }

        private static boolean shouldSkip(HttpObject object) {
            requireNonNull(object, "object");
            return (object instanceof HttpData httpData && httpData.isEmpty()) ||
                   (object instanceof HttpHeaders httpHeaders && httpHeaders.isEmpty());
        }

        private void process(HttpObject object) {
            requireNonNull(object, "object");
            try {
                downstream.onNext(object);
            } catch (Throwable e) {
                if (!terminate()) {
                    downstream.onError(e);
                    return;
                }
            }
            demand--;
        }

        @Override
        public void cancel() {
            if (inEventLoop()) {
                cancel0();
            } else {
                executor.execute(this::cancel0);
            }
        }

        private void cancel0() {
            terminate();
        }

        private long demandGetAndAdd(long inc) {
            assert inEventLoop();
            final var demand0 = demand;
            demand += inc;
            return demand0;
        }

        private Subscriber<? super HttpObject> subscriber() {
            return downstream;
        }

        private EventExecutor executor() {
            return executor;
        }

        private boolean inEventLoop() {
            return executor.inEventLoop();
        }

        private boolean terminate() {
            assert inEventLoop();
            final var terminated0 = terminated;
            terminated = true;
            return terminated0;
        }
    }
}
