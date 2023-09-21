package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    void subscribe_response_with_headers() {
        final var res = HttpResponse.of(ResponseHeaders.of(200));

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content() {
        final var res = HttpResponse.of(ResponseHeaders.of(200), HttpData.ofUtf8("Content"));

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_response_with_headers_content_trailers() {
        final var res = HttpResponse.of(ResponseHeaders.of(200), HttpData.ofUtf8("Content"),
                                        HttpHeaders.of("a", "b"));

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_content() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents() {
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_content_trailers() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of("a", "b"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents_trailers() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content1"));
        res.write(HttpData.ofUtf8("Content2"));
        res.write(HttpHeaders.of("a", "b"));
        res.close();

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content1")
                         .assertContent("Content2")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_after_headers_wrote() {
        final var res = HttpResponse.streaming();
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
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_content_after_content_wrote() {
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_contents_after_headers_wrote() {
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_contents_after_content1_wrote() {
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_contents_after_content2_wrote() {
        final var res = HttpResponse.streaming();
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
    void subscribe_streaming_response_with_headers_content_trailers_after_headers_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_content_trailers_after_content_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_content_trailers_after_trailers_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents_trailers_after_headers_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents_trailers_after_content1_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents_trailers_after_content2_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_headers_contents_trailers_after_trailers_wrote() {
        final var res = HttpResponse.streaming();
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
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void subscribe_streaming_response_with_error_after_headers_wrote() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close(RuntimeException::new);
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertError(RuntimeException.class);
    }

    @Test
    void subscribe_streaming_response_with_error_after_content_wrote() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close(RuntimeException::new);
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertError(RuntimeException.class);
    }

    @Test
    void subscribe_streaming_response_with_error_after_trailers_wrote() {
        final var res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        res.write(HttpData.ofUtf8("Content"));
        res.write(HttpHeaders.of("a", "b"));
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            res.close(RuntimeException::new);
        });

        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertError(RuntimeException.class);
    }
}
