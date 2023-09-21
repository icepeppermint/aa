package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class DeferredHttpResponseWriterTest {

    @Test
    void subscribe_with_headers() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        PublisherVerifier.of(deferred)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertComplete();
    }

    @Test
    void subscribe_with_headers_content() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200, HttpData.ofUtf8("Content")));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        PublisherVerifier.of(deferred)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_with_headers_content_trailers() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200, HttpData.ofUtf8("Content"),
                                            HttpHeaders.of("a", "b")));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        PublisherVerifier.of(deferred)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void aggregate_future_with_headers() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_future_with_headers_content() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200, HttpData.ofUtf8("Content")));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_future_with_headers_content_trailers() {
        final var future = new CompletableFuture<HttpResponse>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete(HttpResponse.of(200, HttpData.ofUtf8("Content"),
                                            HttpHeaders.of()));
        });
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_completed_future_with_headers() {
        final var future = CompletableFuture.completedFuture(HttpResponse.of(200));
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_completed_future_with_headers_content() {
        final var future = CompletableFuture.completedFuture(HttpResponse.of(200, HttpData.ofUtf8("Content")));
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_completed_future_with_headers_content_trailers() {
        final var future = CompletableFuture.completedFuture(HttpResponse.of(200, HttpData.ofUtf8("Content"),
                                                                             HttpHeaders.of()));
        final var deferred = new DeferredHttpResponseWriter(future);
        final var aggregated = deferred.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }
}
