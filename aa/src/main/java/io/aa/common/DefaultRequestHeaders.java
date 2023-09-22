package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map.Entry;

final class DefaultRequestHeaders extends DefaultHttpHeaders implements RequestHeaders {

    private final HttpMethod method;
    private final String path;

    DefaultRequestHeaders(Collection<Entry<String, String>> entries, HttpMethod method, String path) {
        super(entries);
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
