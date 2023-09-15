package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class HttpResponseWriterTest {

    @Test
    void subscribe_response_with_headers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_trailers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of("a", "b"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_trailers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.write(HttpHeaders.of("a", "b"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_after_content_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_after_content1_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content2"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_after_content2_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_trailers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content"));
            res.write(HttpHeaders.of("a", "b"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_trailers_after_content_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpHeaders.of("a", "b"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_trailers_after_trailers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_trailers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.write(HttpHeaders.of("a", "b"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_trailers_after_content1_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content2"));
            res.write(HttpHeaders.of("a", "b"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_trailers_after_content2_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpHeaders.of("a", "b"));
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_contents_trailers_after_trailers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void aggregate_response_with_headers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.close();

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.close();

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_trailers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of());
        res.close();

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.close();

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.write(HttpHeaders.of());
        res.close();

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_before_headers_wrote() {
        final var res = new HttpResponseWriter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(ResponseHeaders.of(200));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_before_headers_wrote() {
        final var res = new HttpResponseWriter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(ResponseHeaders.of(200));
            res.write(HttpData.ofUtf8("Content"));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content"));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_after_content_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_before_headers_wrote() {
        final var res = new HttpResponseWriter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(ResponseHeaders.of(200));
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_after_content1_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content2"));
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_after_content2_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_trailers_before_headers_wrote() {
        final var res = new HttpResponseWriter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(ResponseHeaders.of(200));
            res.write(HttpData.ofUtf8("Content"));
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_trailers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content"));
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_trailers_after_content_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_content_trailers_after_trailers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers_before_headers_wrote() {
        final var res = new HttpResponseWriter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(ResponseHeaders.of(200));
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers_after_headers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content1"));
            res.write(HttpData.ofUtf8("Content2"));
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers_after_content1_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpData.ofUtf8("Content2"));
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers_after_content2_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.write(HttpHeaders.of());
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }

    @Test
    void aggregate_response_with_headers_contents_trailers_after_trailers_wrote() {
        final var res = new HttpResponseWriter();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.write(HttpHeaders.of());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });

        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());
    }
}