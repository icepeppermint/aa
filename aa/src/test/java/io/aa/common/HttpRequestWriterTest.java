package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class HttpRequestWriterTest {

    @Test
    void subscribe_request_with_headers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_trailers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of("a", "b"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_trailers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.write(HttpHeaders.of("a", "b"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_after_content_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content1"));
            req.write(HttpData.ofUtf8("Content2"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_after_content1_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content2"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_after_content2_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_trailers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content"));
            req.write(HttpHeaders.of("a", "b"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_trailers_after_content_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpHeaders.of("a", "b"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_trailers_after_trailers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_trailers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content1"));
            req.write(HttpData.ofUtf8("Content2"));
            req.write(HttpHeaders.of("a", "b"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_trailers_after_content1_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content2"));
            req.write(HttpHeaders.of("a", "b"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_trailers_after_content2_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpHeaders.of("a", "b"));
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_contents_trailers_after_trailers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void aggregate_request_with_headers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.close();

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.close();

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.close();

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_trailers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of());
        req.close();

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_trailers() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.write(HttpHeaders.of());
        req.close();

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content"));
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_after_content_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content1"));
            req.write(HttpData.ofUtf8("Content2"));
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_after_content1_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content2"));
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_after_content2_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_trailers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content"));
            req.write(HttpHeaders.of());
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_trailers_after_content_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpHeaders.of());
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_content_trailers_after_trailers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_trailers_after_headers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content1"));
            req.write(HttpData.ofUtf8("Content2"));
            req.write(HttpHeaders.of());
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_trailers_after_content1_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpData.ofUtf8("Content2"));
            req.write(HttpHeaders.of());
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_trailers_after_content2_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.write(HttpHeaders.of());
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_request_with_headers_contents_trailers_after_trailers_wrote() {
        final HttpRequestWriter req = new HttpRequestWriter(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.write(HttpHeaders.of());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close();
        });

        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }
}
