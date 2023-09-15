package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class RequestHeaders extends HttpHeaders {

    private final HttpMethod method;
    private final String path;

    private RequestHeaders(HttpMethod method, String path) {
        this.method = requireNonNull(method, "method");
        this.path = requireNonNull(path, "path");
    }

    public static RequestHeaders of(HttpMethod method, String path) {
        return new RequestHeaders(method, path);
    }

    @Override
    public RequestHeaders put(String key, String value) {
        return (RequestHeaders) super.put(key, value);
    }

    @Override
    public RequestHeaders putAll(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        return (RequestHeaders) super.putAll(nettyHeaders);
    }

    @Override
    public RequestHeaders removeAll(String key) {
        return (RequestHeaders) super.removeAll(key);
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }
}
