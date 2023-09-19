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
        if (headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)) {
            return false;
        }
        return protocolVersion.isKeepAliveDefault() ||
               headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);
    }

    public static boolean isTransferEncodingChunked(HttpHeaders headers) {
        requireNonNull(headers, "headers");
        return headers.containsValue(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
    }

    private HttpUtil() {}
}
