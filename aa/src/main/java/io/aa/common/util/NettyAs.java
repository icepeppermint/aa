package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public final class NettyAs {

    public static RequestHeaders requestHeaders(HttpRequest nettyReq) {
        requireNonNull(nettyReq, "nettyReq");
        return RequestHeaders.builder()
                             .method(HttpMethod.valueOf(nettyReq.method().name()))
                             .path(nettyReq.uri())
                             .add(nettyReq.headers())
                             .build();
    }

    public static ResponseHeaders responseHeaders(HttpResponse nettyRes) {
        requireNonNull(nettyRes, "nettyRes");
        return ResponseHeaders.builder()
                              .statusCode(nettyRes.status().code())
                              .add(nettyRes.headers())
                              .build();
    }

    public static HttpHeaders httpHeaders(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        requireNonNull(nettyHeaders, "nettyHeaders");
        return HttpHeaders.builder().add(nettyHeaders).build();
    }

    private NettyAs() {}
}
