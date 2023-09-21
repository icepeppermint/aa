package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.RequestHeaders;
import io.aa.common.ResponseHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public final class NettyAs {

    public static RequestHeaders requestHeaders(HttpRequest nettyReq) {
        requireNonNull(nettyReq, "nettyReq");
        return RequestHeaders.of(HttpMethod.valueOf(nettyReq.method().name()), nettyReq.uri())
                             .putAll(multimap(nettyReq.headers()));
    }

    public static ResponseHeaders responseHeaders(HttpResponse nettyRes) {
        requireNonNull(nettyRes, "nettyRes");
        return ResponseHeaders.of(nettyRes.status().code()).putAll(multimap(nettyRes.headers()));
    }

    public static HttpHeaders httpHeaders(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        requireNonNull(nettyHeaders, "nettyHeaders");
        return HttpHeaders.builder().addAll(multimap(nettyHeaders)).build();
    }

    private static Multimap<String, String> multimap(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        requireNonNull(nettyHeaders, "nettyHeaders");
        return ImmutableListMultimap.copyOf(nettyHeaders);
    }

    private NettyAs() {}
}
