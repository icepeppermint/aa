package io.aa.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.aa.common.HttpHeaderNames;
import io.aa.common.HttpHeaderValues;
import io.aa.common.HttpMethod;
import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;

class HttpUtilTest {

    @Test
    void isTransferEncodingChunked_when_request_headers() {
        RequestHeaders requestHeaders;

        requestHeaders = RequestHeaders.of(HttpMethod.GET, "/");
        assertFalse(HttpUtil.isTransferEncodingChunked(requestHeaders));

        requestHeaders = RequestHeaders.of(HttpMethod.GET, "/")
                                       .put(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        assertTrue(HttpUtil.isTransferEncodingChunked(requestHeaders));
    }

    @Test
    void isTransferEncodingChunked_when_response_headers() {
        ResponseHeaders responseHeaders;

        responseHeaders = ResponseHeaders.of(200);
        assertFalse(HttpUtil.isTransferEncodingChunked(responseHeaders));

        responseHeaders = ResponseHeaders.of(200)
                                         .put(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        assertTrue(HttpUtil.isTransferEncodingChunked(responseHeaders));
    }
}
