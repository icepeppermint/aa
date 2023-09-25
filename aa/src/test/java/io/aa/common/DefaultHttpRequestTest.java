package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DefaultHttpRequestTest {

    @Test
    void aggregate() {
        final DefaultHttpRequest req =
                new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                       HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        final AggregatedHttpRequest aggregated = req.aggregate().join();
        assertSame(HttpMethod.GET, aggregated.method());
        assertEquals("/", aggregated.path());
        assertTrue(aggregated.headers().isEmpty());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().contains("a"));
    }

    @Test
    void subscribe() {
        final DefaultHttpRequest req =
                new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                       HttpData.ofUtf8("Content"), HttpHeaders.of("a", "b"));
        PublisherVerifier.of(req)
                         .assertRequestHeaders(RequestHeaders.of(HttpMethod.GET, "/"))
                         .assertContent("Content")
                         .assertTrailers(trailers -> assertEquals("b", trailers.get("a")))
                         .assertComplete();
    }

    @Test
    void protocolVersion() {
        final DefaultHttpRequest req = new DefaultHttpRequest(RequestHeaders.of(HttpMethod.GET, "/"),
                                                              HttpData.empty(), HttpHeaders.of());
        assertSame(HttpVersion.HTTP_1_1, req.protocolVersion());
    }
}
