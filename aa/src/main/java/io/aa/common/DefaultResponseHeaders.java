package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

final class DefaultResponseHeaders extends DefaultHttpHeaders implements ResponseHeaders {

    private final HttpStatus status;

    DefaultResponseHeaders(Multimap<String, String> multimap, HttpStatus status) {
        super(multimap);
        this.status = requireNonNull(status, "status");
    }

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public int statusCode() {
        return status.code();
    }
}
