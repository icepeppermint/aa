package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import java.util.Map.Entry;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

public final class AaAs {

    public static HttpHeaders nettyHttpHeaders(io.aa.common.HttpHeaders httpHeaders) {
        requireNonNull(httpHeaders, "httpHeaders");
        final DefaultHttpHeaders nettyHeaders = new DefaultHttpHeaders();
        for (Entry<String, String> entry : httpHeaders.entries()) {
            nettyHeaders.add(entry.getKey(), entry.getValue());
        }
        return nettyHeaders;
    }

    private AaAs() {}
}
