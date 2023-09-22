package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import io.aa.common.HttpHeaderNames;
import io.aa.common.HttpHeaderValues;
import io.aa.common.HttpHeaders;
import io.aa.common.HttpVersion;

public final class HttpUtil {

    public static boolean isKeepAlive(HttpHeaders headers, HttpVersion protocolVersion) {
        requireNonNull(headers, "headers");
        requireNonNull(protocolVersion, "protocolVersion");
        if (headers.contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)) {
            return false;
        }
        return protocolVersion.isKeepAliveDefault() ||
               headers.contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    }

    public static boolean isTransferEncodingChunked(HttpHeaders headers) {
        requireNonNull(headers, "headers");
        return headers.contains(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
    }

    private HttpUtil() {}
}
