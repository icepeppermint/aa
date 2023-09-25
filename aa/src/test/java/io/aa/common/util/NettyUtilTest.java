package io.aa.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

class NettyUtilTest {

    @Test
    void toRequestHeaders() {
        final HttpHeaders nettyHeaders = new DefaultHttpHeaders();
        nettyHeaders.add("a", "b");

        final HttpRequest nettyReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/",
                                                            nettyHeaders);
        final RequestHeaders requestHeaders = NettyUtil.toRequestHeaders(nettyReq);
        assertSame(io.aa.common.HttpMethod.GET, requestHeaders.method());
        assertEquals("/", requestHeaders.path());
        assertEquals("b", requestHeaders.get("a"));
    }

    @Test
    void toResponseHeaders() {
        final HttpHeaders nettyHeaders = new DefaultHttpHeaders();
        nettyHeaders.add("a", "b");

        final DefaultHttpResponse nettyRes = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                                                                     HttpResponseStatus.OK,
                                                                     nettyHeaders);
        final ResponseHeaders responseHeaders = NettyUtil.toResponseHeaders(nettyRes);
        assertEquals(200, responseHeaders.statusCode());
        assertEquals("b", responseHeaders.get("a"));
    }

    @Test
    void toHttpHeaders() {
        final HttpHeaders nettyHeaders = new DefaultHttpHeaders();
        nettyHeaders.add("a", "b");

        final io.aa.common.HttpHeaders httpHeaders = NettyUtil.toHttpHeaders(nettyHeaders);
        assertEquals("b", httpHeaders.get("a"));
    }
}
