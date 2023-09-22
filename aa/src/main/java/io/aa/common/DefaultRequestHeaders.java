package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

final class DefaultRequestHeaders extends DefaultHttpHeaders implements RequestHeaders {

    private final HttpMethod method;
    private final String path;

    DefaultRequestHeaders(Multimap<String, String> multimap, HttpMethod method, String path) {
        super(multimap);
        this.method = requireNonNull(method, "method");
        this.path = requireNonNull(path, "path");
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return path;
    }
}
