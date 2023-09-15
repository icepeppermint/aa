package io.aa.common;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

public final class ResponseHeaders extends HttpHeaders {

    private final HttpStatus status;
    @Nullable
    private final MediaType contentType;

    ResponseHeaders(HttpStatus status, @Nullable MediaType contentType) {
        this.status = requireNonNull(status, "status");
        this.contentType = contentType;
    }

    public static ResponseHeaders of(HttpStatus status) {
        return new ResponseHeaders(status, null);
    }

    public static ResponseHeaders of(int statusCode) {
        return new ResponseHeaders(HttpStatus.valueOf(statusCode), null);
    }

    public static ResponseHeaders of(HttpStatus status, MediaType contentType) {
        requireNonNull(contentType, "contentType");
        return new ResponseHeaders(status, contentType);
    }

    public static ResponseHeaders of(int statusCode, MediaType contentType) {
        requireNonNull(contentType, "contentType");
        return new ResponseHeaders(HttpStatus.valueOf(statusCode), contentType);
    }

    @Override
    public ResponseHeaders put(String key, String value) {
        return (ResponseHeaders) super.put(key, value);
    }

    @Override
    public ResponseHeaders putAll(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        return (ResponseHeaders) super.putAll(nettyHeaders);
    }

    @Override
    public ResponseHeaders removeAll(String key) {
        return (ResponseHeaders) super.removeAll(key);
    }

    public HttpStatus status() {
        return status;
    }

    public int statusCode() {
        return status.code();
    }

    @Nullable
    public MediaType contentType() {
        return contentType;
    }
}
