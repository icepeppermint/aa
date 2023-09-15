package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    void subscribe_request_with_headers() {
        final var req = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"));

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content() {
        final var req = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"), HttpData.ofUtf8("Content"));

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_request_with_headers_content_trailers() {
        final var req = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"), HttpData.ofUtf8("Content"),
                                       HttpHeaders.of("a", "b"));

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_content() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_content_trailers() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of("a", "b"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents_trailers() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content1"));
        req.write(HttpData.ofUtf8("Content2"));
        req.write(HttpHeaders.of("a", "b"));
        req.close();

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
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
    void subscribe_streaming_request_with_headers_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_content_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_content_after_content_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_contents_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_contents_after_content1_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_contents_after_content2_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
    void subscribe_streaming_request_with_headers_content_trailers_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_content_trailers_after_content_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_content_trailers_after_trailers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents_trailers_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents_trailers_after_content1_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents_trailers_after_content2_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_headers_contents_trailers_after_trailers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
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
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_request_with_error() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.close(RuntimeException::new);

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertError(RuntimeException.class);
    }

    @Test
    void subscribe_streaming_request_with_error_after_headers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close(RuntimeException::new);
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertError(RuntimeException.class);
    }

    @Test
    void subscribe_streaming_request_with_error_after_content_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close(RuntimeException::new);
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertError(RuntimeException.class);
    }

    @Test
    void subscribe_streaming_request_with_error_after_trailers_wrote() {
        final var req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.GET, "/"));
        req.write(HttpData.ofUtf8("Content"));
        req.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            req.close(RuntimeException::new);
        });

        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> {
                             final var values = trailers.get("a");
                             assertEquals(1, values.size());
                             assertEquals("b", values.get(0));
                         })
                         .assertError(RuntimeException.class);
    }
}