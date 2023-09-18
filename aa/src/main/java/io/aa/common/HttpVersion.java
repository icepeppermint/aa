package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class HttpVersion {

    private static final String HTTP_1_0_STRING = "HTTP/1.0";
    private static final String HTTP_1_1_STRING = "HTTP/1.1";

    public static final HttpVersion HTTP_1_0 = new HttpVersion(1, 0, false);
    public static final HttpVersion HTTP_1_1 = new HttpVersion(1, 1, true);

    private final int majorVersion;
    private final int minorVersion;
    private final boolean keepAliveDefault;

    private HttpVersion(int majorVersion, int minorVersion, boolean keepAliveDefault) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.keepAliveDefault = keepAliveDefault;
    }

    public static HttpVersion valueOf(String text) {
        requireNonNull(text, "text");
        if (text == HTTP_1_1_STRING) {
            return HTTP_1_1;
        }
        if (text == HTTP_1_0_STRING) {
            return HTTP_1_0;
        }

        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("text is empty (possibly HTTP/0.9)");
        }
        if (HTTP_1_1_STRING.equals(text)) {
            return HTTP_1_1;
        }
        if (HTTP_1_0_STRING.equals(text)) {
            return HTTP_1_0;
        }
        throw new IllegalArgumentException("Unexpected HTTP version: " + text);
    }

    public int majorVersion() {
        return majorVersion;
    }

    public int minorVersion() {
        return minorVersion;
    }

    public boolean isKeepAliveDefault() {
        return keepAliveDefault;
    }
}
