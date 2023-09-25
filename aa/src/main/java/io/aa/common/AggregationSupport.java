package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.netty.util.concurrent.EventExecutor;

interface AggregationSupport {

    default CompletableFuture<? extends AggregatedHttpMessage> aggregate(HttpMessage message,
                                                                         EventExecutor executor) {
        requireNonNull(message, "message");
        requireNonNull(executor, "executor");
        final CompletableFuture<AggregatedHttpMessage> future = new CompletableFuture<>();
        message.subscribe(new SubscriberImpl<>(future), executor);
        return future;
    }

    final class SubscriberImpl<T extends HttpObject> implements Subscriber<T> {

        private final CompletableFuture<AggregatedHttpMessage> future;
        private HttpHeaders headers;
        private final Queue<HttpData> queue;
        @Nullable
        private HttpHeaders trailers;

        private SubscriberImpl(CompletableFuture<AggregatedHttpMessage> future) {
            this.future = requireNonNull(future, "future");
            queue = new LinkedList<>();
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            requireNonNull(subscription, "subscription").request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T object) {
            if (object instanceof RequestHeaders) {
                headers = (HttpHeaders) object;
            } else if (object instanceof ResponseHeaders) {
                headers = (HttpHeaders) object;
            } else if (object instanceof HttpData httpData) {
                queue.add(httpData);
            } else {
                assert object instanceof HttpHeaders;
                trailers = (HttpHeaders) object;
            }
        }

        @Override
        public void onError(Throwable t) {
            future.completeExceptionally(t);
        }

        @Override
        public void onComplete() {
            HttpHeaders trailers = this.trailers;
            if (trailers == null) {
                trailers = HttpHeaders.of();
            }
            final AggregatedHttpMessage aggregated;
            if (headers instanceof RequestHeaders) {
                aggregated = new DefaultAggregatedHttpRequest((RequestHeaders) headers, queue, trailers);
            } else {
                assert headers instanceof ResponseHeaders;
                aggregated = new DefaultAggregatedHttpResponse((ResponseHeaders) headers, queue, trailers);
            }
            future.complete(aggregated);
        }
    }
}
