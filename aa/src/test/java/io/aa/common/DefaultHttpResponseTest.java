package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DefaultHttpResponseTest {

    @Test
    void aggregate() {
        final var res = new DefaultHttpResponse(ResponseHeaders.of(200),
                                                HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        final var aggregated = res.aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().contains("a"));
    }

    @Test
    void subscribe() {
        final var res = new DefaultHttpResponse(ResponseHeaders.of(200),
                                                HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        PublisherVerifier.of(res)
                         .assertResponseHeaders(ResponseHeaders.of(200))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void protocolVersion() {
        final var res = new DefaultHttpResponse(ResponseHeaders.of(200),
                                                HttpData.empty(), HttpHeaders.of());
        assertSame(HttpVersion.HTTP_1_1, res.protocolVersion());
    }
}
