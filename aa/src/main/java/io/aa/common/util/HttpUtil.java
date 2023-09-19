package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import io.aa.common.HttpHeaderNames;
import io.aa.common.HttpHeaderValues;
import io.aa.common.HttpHeaders;

public final class HttpUtil {

    public static boolean isTransferEncodingChunked(HttpHeaders headers) {
        requireNonNull(headers, "headers");
        return headers.containsValue(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
    }

    private HttpUtil() {}
}
