package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.netty.util.concurrent.EventExecutor;

final class DeferredHttpResponseWriter extends AbstractStreamWriter
        implements HttpResponse, AggregationSupport {

    DeferredHttpResponseWriter(CompletableFuture<? extends HttpResponse> future) {
        this(future, DEFAULT_SUBSCRIBER_EXECUTOR);
    }

    DeferredHttpResponseWriter(CompletableFuture<? extends HttpResponse> future,
                               EventExecutor executor) {
        defer(future, executor);
    }

    private void defer(CompletableFuture<? extends HttpResponse> future, EventExecutor executor) {
        requireNonNull(future, "future");
        requireNonNull(executor, "executor");
        future.thenAccept(res -> res.subscribe(new DeferredSubscriber<>(), executor));
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<AggregatedHttpResponse> aggregate(EventExecutor executor) {
        return (CompletableFuture<AggregatedHttpResponse>) aggregate(this, executor);
    }

    @Override
    public HttpVersion protocolVersion() {
        return HttpVersion.HTTP_1_1;
    }

    private final class DeferredSubscriber<T extends HttpObject> implements Subscriber<T> {

        private Subscription subscription;

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = requireNonNull(subscription, "subscription");
            this.subscription.request(1);
        }

        @Override
        public void onNext(T object) {
            write(object);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable t) {
            close(t);
        }

        @Override
        public void onComplete() {
            close();
        }
    }
}
