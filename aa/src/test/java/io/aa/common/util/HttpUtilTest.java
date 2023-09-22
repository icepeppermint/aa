package io.aa.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.aa.common.HttpHeaderNames;
import io.aa.common.HttpHeaderValues;
import io.aa.common.HttpMethod;
import io.aa.common.HttpVersion;
import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;

class HttpUtilTest {

    @Test
    void isKeepAlive_when_request_headers_contains_connection_close_and_protocol_version_http_1_1() {
        final var requestHeaders = RequestHeaders.builder(HttpMethod.GET, "/")
                                                 .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
                                                 .build();
        assertFalse(HttpUtil.isKeepAlive(requestHeaders, HttpVersion.HTTP_1_1));
    }

    @Test
    void isKeepAlive_when_request_headers_does_not_contain_connection_close_and_protocol_version_http_1_1() {
        final var requestHeaders = RequestHeaders.of(HttpMethod.GET, "/");
        assertTrue(HttpUtil.isKeepAlive(requestHeaders, HttpVersion.HTTP_1_1));
    }

    @Test
    void isKeepAlive_when_request_headers_contains_connection_keep_alive_and_protocol_version_http_1_0() {
        final var requestHeaders = RequestHeaders.builder(HttpMethod.GET, "/")
                                                 .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                                                 .build();
        assertTrue(HttpUtil.isKeepAlive(requestHeaders, HttpVersion.HTTP_1_0));
    }

    @Test
    void isKeepAlive_when_request_headers_does_not_contain_connection_keep_alive_and_protocol_version_http_1_0() {
        final var requestHeaders = RequestHeaders.of(HttpMethod.GET, "/");
        assertFalse(HttpUtil.isKeepAlive(requestHeaders, HttpVersion.HTTP_1_0));
    }

    @Test
    void isKeepAlive_when_response_headers_contains_connection_close_and_protocol_version_http_1_1() {
        final var responseHeaders = ResponseHeaders.builder(200)
                                                   .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
                                                   .build();
        assertFalse(HttpUtil.isKeepAlive(responseHeaders, HttpVersion.HTTP_1_1));
    }

    @Test
    void isKeepAlive_when_response_headers_does_not_contain_connection_close_and_protocol_version_http_1_1() {
        final var responseHeaders = ResponseHeaders.of(200);
        assertTrue(HttpUtil.isKeepAlive(responseHeaders, HttpVersion.HTTP_1_1));
    }

    @Test
    void isKeepAlive_when_response_headers_contains_connection_keep_alive_and_protocol_version_http_1_0() {
        final var responseHeaders = ResponseHeaders.builder(200)
                                                   .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                                                   .build();
        assertTrue(HttpUtil.isKeepAlive(responseHeaders, HttpVersion.HTTP_1_0));
    }

    @Test
    void isKeepAlive_when_response_headers_does_not_contain_connection_keep_alive_and_protocol_version_http_1_0() {
        final var responseHeaders = ResponseHeaders.of(200);
        assertFalse(HttpUtil.isKeepAlive(responseHeaders, HttpVersion.HTTP_1_0));
    }

    @Test
    void isTransferEncodingChunked_when_request_headers() {
        RequestHeaders requestHeaders;

        requestHeaders = RequestHeaders.of(HttpMethod.GET, "/");
        assertFalse(HttpUtil.isTransferEncodingChunked(requestHeaders));

        requestHeaders = RequestHeaders.builder(HttpMethod.GET, "/")
                                       .add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED)
                                       .build();
        assertTrue(HttpUtil.isTransferEncodingChunked(requestHeaders));
    }

    @Test
    void isTransferEncodingChunked_when_response_headers() {
        ResponseHeaders responseHeaders;

        responseHeaders = ResponseHeaders.of(200);
        assertFalse(HttpUtil.isTransferEncodingChunked(responseHeaders));

        responseHeaders = ResponseHeaders.builder(200)
                                         .add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED)
                                         .build();
        assertTrue(HttpUtil.isTransferEncodingChunked(responseHeaders));
    }
}
