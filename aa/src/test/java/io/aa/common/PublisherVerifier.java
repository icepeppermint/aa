package io.aa.common;

import static java.util.Objects.requireNonNull;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class PublisherVerifier<T> {

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<PublisherVerifier, Subscription>
            subscriptionUpdater = AtomicReferenceFieldUpdater.newUpdater(PublisherVerifier.class,
                                                                         Subscription.class, "subscription");
    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<PublisherVerifier, Object>
            valueUpdater = AtomicReferenceFieldUpdater.newUpdater(PublisherVerifier.class,
                                                                  Object.class, "value");
    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<PublisherVerifier, Throwable>
            errorUpdater = AtomicReferenceFieldUpdater.newUpdater(PublisherVerifier.class,
                                                                  Throwable.class, "error");

    @Nullable
    private volatile Subscription subscription;
    private final AtomicBoolean subscribed;
    @Nullable
    private volatile T value;
    private final AtomicBoolean valueUpdated;
    private final AtomicBoolean done;
    @Nullable
    private volatile Throwable error;

    private PublisherVerifier(Publisher<T> publisher) {
        requireNonNull(publisher, "publisher");
        subscription = null;
        subscribed = new AtomicBoolean(false);
        value = null;
        valueUpdated = new AtomicBoolean(false);
        done = new AtomicBoolean(false);
        error = null;
        publisher.subscribe(new SubscriberImpl());
    }

    public static <T> PublisherVerifier<T> of(Publisher<T> publisher) {
        return new PublisherVerifier<>(publisher);
    }

    public PublisherVerifier<T> assertNext(Consumer<? super T> consumer) {
        requireNonNull(consumer, "consumer");
        return assertNext0(() -> consumer.accept(nextValue()));
    }

    public PublisherVerifier<T> assertNext(Class<? extends T> expected) {
        requireNonNull(expected, "expected");
        return assertNext(object -> assertSame(expected, object.getClass()));
    }

    public PublisherVerifier<T> assertRequestHeaders(Consumer<RequestHeaders> consumer) {
        requireNonNull(consumer, "consumer");
        return assertNext0(() -> consumer.accept(expect(nextValue(), RequestHeaders.class)));
    }

    public PublisherVerifier<T> assertRequestHeaders(RequestHeaders headers) {
        requireNonNull(headers, "headers");
        return assertRequestHeaders(headers0 -> {
            assertSame(headers.method(), headers0.method());
            assertEquals(headers.path(), headers0.path());
        });
    }

    public PublisherVerifier<T> assertRequestHeaders(HttpMethod method, String path) {
        return assertRequestHeaders(RequestHeaders.of(method, path));
    }

    public PublisherVerifier<T> assertResponseHeaders(Consumer<ResponseHeaders> consumer) {
        requireNonNull(consumer, "consumer");
        return assertNext0(() -> consumer.accept(expect(nextValue(), ResponseHeaders.class)));
    }

    public PublisherVerifier<T> assertResponseHeaders(ResponseHeaders headers) {
        requireNonNull(headers, "headers");
        return assertResponseHeaders(headers0 -> assertSame(headers.status(), headers0.status()));
    }

    public PublisherVerifier<T> assertResponseHeaders(HttpStatus status) {
        return assertResponseHeaders(ResponseHeaders.of(status));
    }

    public PublisherVerifier<T> assertResponseHeaders(int statusCode) {
        return assertResponseHeaders(ResponseHeaders.of(statusCode));
    }

    public PublisherVerifier<T> assertContent(Consumer<HttpData> consumer) {
        requireNonNull(consumer, "consumer");
        return assertNext0(() -> consumer.accept(expect(nextValue(), HttpData.class)));
    }

    public PublisherVerifier<T> assertContent(CharSequence content) {
        requireNonNull(content, "content");
        return assertContent(content0 -> assertEquals(content, content0.contentUtf8()));
    }

    public PublisherVerifier<T> assertContent(Supplier<? extends CharSequence> supplier) {
        requireNonNull(supplier, "supplier");
        return assertContent(supplier.get());
    }

    public PublisherVerifier<T> assertTrailers(Consumer<HttpHeaders> consumer) {
        requireNonNull(consumer, "consumer");
        return assertNext0(() -> consumer.accept(expect(nextValue(), DefaultHttpHeaders.class)));
    }

    public void assertComplete() {
        request();
        assertNull(error(), "error is not null (expected null)");
    }

    public void assertError(Consumer<? super Throwable> consumer) {
        requireNonNull(consumer, "consumer");
        assertError0(() -> consumer.accept(mustError()));
    }

    public void assertError(Class<? extends Throwable> expected) {
        requireNonNull(expected, "expected");
        assertError(e -> assertSame(expected, e.getClass()));
    }

    public void assertError(Supplier<? extends Throwable> supplier) {
        requireNonNull(supplier, "supplier");
        assertError(e -> assertSame(supplier.get().getClass(), e.getClass()));
    }

    private void request() {
        await().untilTrue(subscribed);
        final var subscription = this.subscription;
        assertNotNull(subscription, "subscription is null (expected not null)");
        subscription.request(1);
    }

    private T nextValue() {
        request();
        await().untilTrue(valueUpdated);
        return requireNonNull(value, "value");
    }

    private PublisherVerifier<T> assertNext0(Runnable runnable) {
        requireNonNull(runnable, "runnable").run();
        return unsetValue();
    }

    private PublisherVerifier<T> unsetValue() {
        final var value = valueUpdater.getAndSet(this, null);
        if (!valueUpdated.compareAndSet(true, false)) {
            valueUpdater.set(this, value);
            throw new IllegalStateException("valueUpdated is false (expected true)");
        }
        return this;
    }

    private void assertError0(Runnable runnable) {
        requireNonNull(runnable, "runnable");
        request();
        runnable.run();
    }

    @Nullable
    private Throwable error() {
        await().untilTrue(done);
        return error;
    }

    private Throwable mustError() {
        final var error = error();
        if (error == null) {
            throw new AssertionError("error is null (expected not null)");
        }
        return error;
    }

    @SuppressWarnings("unchecked")
    private static <T> T expect(Object value, Class<T> expected) {
        requireNonNull(value, "value");
        requireNonNull(expected, "expected");
        final var actual = value.getClass();
        if (!expected.equals(actual)) {
            throw new AssertionError("The value is " + actual.getSimpleName() +
                                     " (expected " + expected.getSimpleName() + ')');
        }
        return (T) value;
    }

    private final class SubscriberImpl implements Subscriber<T> {

        @Override
        public void onSubscribe(Subscription subscription) {
            requireNonNull(subscription, "subscription");
            if (!subscriptionUpdater.compareAndSet(PublisherVerifier.this, null, subscription)) {
                throw new IllegalStateException(
                        "PublisherVerifier.this.subscription is not null (expected null)");
            }
            if (!subscribed.compareAndSet(false, true)) {
                subscriptionUpdater.set(PublisherVerifier.this, null);
                throw new IllegalStateException("Already subscribed");
            }
        }

        @Override
        public void onNext(T o) {
            await().untilFalse(valueUpdated);
            final var value = valueUpdater.getAndSet(PublisherVerifier.this, o);
            if (!valueUpdated.compareAndSet(false, true)) {
                valueUpdater.set(PublisherVerifier.this, value);
                throw new IllegalStateException("valuedUpdated is true (expected false)");
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!errorUpdater.compareAndSet(PublisherVerifier.this, null, t)) {
                throw new IllegalStateException("PublisherVerifier.this.error is not null (expected null)");
            }
            if (!done.compareAndSet(false, true)) {
                errorUpdater.set(PublisherVerifier.this, null);
                throw new IllegalStateException("PublisherVerifier.this.done is true (expected false)");
            }
        }

        @Override
        public void onComplete() {
            if (!done.compareAndSet(false, true)) {
                throw new IllegalStateException("PublisherVerifier.this.done is true (expected false)");
            }
        }
    }
}
