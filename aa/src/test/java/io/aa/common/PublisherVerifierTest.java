package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.reactivex.rxjava3.core.Flowable;

class PublisherVerifierTest {

    @Test
    void assertNext() {
        final var publisher = Flowable.just(1, 2, 3, 4, 5);
        PublisherVerifier.of(publisher)
                         .assertNext(v -> assertEquals(1, v))
                         .assertNext(v -> assertEquals(2, v))
                         .assertNext(v -> assertEquals(3, v))
                         .assertNext(v -> assertEquals(4, v))
                         .assertNext(v -> assertEquals(5, v));
    }

    @Test
    void assertNext_class() {
        final var publisher = Flowable.just(1, 2, 3, 4, 5);
        PublisherVerifier.of(publisher)
                         .assertNext(Integer.class)
                         .assertNext(Integer.class)
                         .assertNext(Integer.class)
                         .assertNext(Integer.class)
                         .assertNext(Integer.class);
    }

    @Test
    void assertRequestHeader() {
        final var publisher = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"));
        PublisherVerifier.of(publisher)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"));
    }

    @Test
    void assertResponseHeader() {
        final var publisher = HttpResponse.of(ResponseHeaders.of(200));
        PublisherVerifier.of(publisher)
                         .assertResponseHeaders(ResponseHeaders.of(200));
    }

    @Test
    void assertContent() {
        final var publisher = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"),
                                             HttpData.ofUtf8("Content"));
        PublisherVerifier.of(publisher)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content");
    }

    @Test
    void assertTrailers() {
        final var publisher = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/"),
                                             HttpHeaders.of("a", "b"));
        PublisherVerifier.of(publisher)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")));
    }

    @Test
    void assertComplete() {
        final var publisher = Flowable.empty();
        PublisherVerifier.of(publisher)
                         .assertComplete();
    }

    @Test
    void assertError() {
        final var publisher = Flowable.error(new RuntimeException());
        PublisherVerifier.of(publisher)
                         .assertError(e -> assertTrue(e instanceof RuntimeException));
    }

    @Test
    void assertError_class() {
        final var publisher = Flowable.error(new RuntimeException());
        PublisherVerifier.of(publisher)
                         .assertError(RuntimeException.class);
    }
}
