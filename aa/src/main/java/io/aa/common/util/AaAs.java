package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

public final class AaAs {

    public static HttpHeaders nettyHttpHeaders(io.aa.common.HttpHeaders httpHeaders) {
        requireNonNull(httpHeaders, "httpHeaders");
        final var nettyHeaders = new DefaultHttpHeaders();
        for (var entry : httpHeaders.entries()) {
            nettyHeaders.add(entry.getKey(), entry.getValue());
        }
        return nettyHeaders;
    }

    private AaAs() {}
}
