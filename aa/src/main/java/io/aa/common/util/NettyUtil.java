package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import java.util.Map.Entry;

import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public final class NettyUtil {

    public static RequestHeaders toRequestHeaders(HttpRequest nettyReq) {
        requireNonNull(nettyReq, "nettyReq");
        return RequestHeaders.builder()
                             .method(HttpMethod.valueOf(nettyReq.method().name()))
                             .path(nettyReq.uri())
                             .add(nettyReq.headers())
                             .build();
    }

    public static ResponseHeaders toResponseHeaders(HttpResponse nettyRes) {
        requireNonNull(nettyRes, "nettyRes");
        return ResponseHeaders.builder()
                              .statusCode(nettyRes.status().code())
                              .add(nettyRes.headers())
                              .build();
    }

    public static HttpHeaders toHttpHeaders(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        requireNonNull(nettyHeaders, "nettyHeaders");
        return HttpHeaders.of(nettyHeaders.entries());
    }

    public static io.netty.handler.codec.http.HttpHeaders toNettyHttpHeaders(HttpHeaders httpHeaders) {
        requireNonNull(httpHeaders, "httpHeaders");
        final DefaultHttpHeaders nettyHeaders = new DefaultHttpHeaders();
        for (Entry<String, String> entry : httpHeaders.entries()) {
            nettyHeaders.add(entry.getKey(), entry.getValue());
        }
        return nettyHeaders;
    }

    private NettyUtil() {}
}
