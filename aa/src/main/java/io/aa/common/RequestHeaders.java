package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class RequestHeaders extends HttpHeaders {

    private static final HttpVersion DEFAULT_HTTP_VERSION = HttpVersion.HTTP_1_1;

    private final HttpVersion protocolVersion;
    private final HttpMethod method;
    private final String path;

    private RequestHeaders(HttpVersion protocolVersion, HttpMethod method, String path) {
        this.protocolVersion = requireNonNull(protocolVersion, "protocolVersion");
        this.method = requireNonNull(method, "method");
        this.path = requireNonNull(path, "path");
    }

    public static RequestHeaders of(HttpMethod method, String path) {
        return new RequestHeaders(DEFAULT_HTTP_VERSION, method, path);
    }

    public static RequestHeaders of(HttpVersion protocolVersion, HttpMethod method, String path) {
        return new RequestHeaders(protocolVersion, method, path);
    }

    @Override
    public RequestHeaders put(String name, String value) {
        return (RequestHeaders) super.put(name, value);
    }

    @Override
    public RequestHeaders putAll(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        return (RequestHeaders) super.putAll(nettyHeaders);
    }

    @Override
    public RequestHeaders removeAll(String name) {
        return (RequestHeaders) super.removeAll(name);
    }

    public HttpVersion protocolVersion() {
        return protocolVersion;
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }
}
