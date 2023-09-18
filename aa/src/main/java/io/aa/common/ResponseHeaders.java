package io.aa.common;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

public final class ResponseHeaders extends HttpHeaders {

    private final HttpVersion protocolVersion;
    private final HttpStatus status;
    @Nullable
    private final MediaType contentType;

    private ResponseHeaders(HttpStatus status, @Nullable MediaType contentType) {
        this(DEFAULT_HTTP_VERSION, status, contentType);
    }

    private ResponseHeaders(HttpVersion protocolVersion, HttpStatus status, @Nullable MediaType contentType) {
        this.protocolVersion = requireNonNull(protocolVersion, "protocolVersion");
        this.status = requireNonNull(status, "status");
        this.contentType = contentType;
    }

    public static ResponseHeaders of(HttpStatus status) {
        return new ResponseHeaders(status, null);
    }

    public static ResponseHeaders of(int statusCode) {
        return of(HttpStatus.valueOf(statusCode), null);
    }

    public static ResponseHeaders of(HttpStatus status, MediaType contentType) {
        requireNonNull(contentType, "contentType");
        return new ResponseHeaders(status, contentType);
    }

    public static ResponseHeaders of(int statusCode, MediaType contentType) {
        return of(HttpStatus.valueOf(statusCode), contentType);
    }

    public static ResponseHeaders of(HttpVersion protocolVersion, HttpStatus status, MediaType contentType) {
        requireNonNull(contentType, "contentType");
        return new ResponseHeaders(protocolVersion, status, contentType);
    }

    public static ResponseHeaders of(HttpVersion protocolVersion, int statusCode, MediaType contentType) {
        return of(protocolVersion, HttpStatus.valueOf(statusCode), contentType);
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

    public HttpVersion protocolVersion() {
        return protocolVersion;
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
