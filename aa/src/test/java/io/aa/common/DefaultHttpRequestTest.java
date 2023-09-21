package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DefaultHttpRequestTest {

    @Test
    void aggregate() {
        final var req = new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                               HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        final var aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertFalse(aggregated.headers().isEmpty());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().contains("a"));
    }

    @Test
    void subscribe() {
        final var req = new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                               HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void protocolVersion() {
        final var req = new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                               HttpData.empty(), HttpHeaders.of());
        assertSame(HttpVersion.HTTP_1_1, req.protocolVersion());
    }
}
