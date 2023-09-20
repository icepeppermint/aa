package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class ResponseHeaders extends HttpHeaders {

    private final HttpStatus status;
    private final MediaType contentType;

    private ResponseHeaders(HttpStatus status) {
        this(status, MediaType.PLAIN_TEXT_UTF_8);
    }

    private ResponseHeaders(HttpStatus status, MediaType mediaType) {
        this.status = requireNonNull(status, "status");
        contentType = requireNonNull(mediaType, "mediaType");
    }

    public static ResponseHeaders of(HttpStatus status) {
        return new ResponseHeaders(status);
    }

    public static ResponseHeaders of(int statusCode) {
        return of(HttpStatus.valueOf(statusCode));
    }

    public static ResponseHeaders of(HttpStatus status, MediaType contentType) {
        return new ResponseHeaders(status, contentType);
    }

    public static ResponseHeaders of(int statusCode, MediaType contentType) {
        return of(HttpStatus.valueOf(statusCode), contentType);
    }

    @Override
    public ResponseHeaders put(String name, String value) {
        return (ResponseHeaders) super.put(name, value);
    }

    @Override
    public ResponseHeaders putAll(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        return (ResponseHeaders) super.putAll(nettyHeaders);
    }

    @Override
    public ResponseHeaders removeAll(String name) {
        return (ResponseHeaders) super.removeAll(name);
    }

    public HttpStatus status() {
        return status;
    }

    public int statusCode() {
        return status.code();
    }

    public MediaType contentType() {
        return contentType;
    }
}
