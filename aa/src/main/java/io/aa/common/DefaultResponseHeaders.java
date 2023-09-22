package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map.Entry;

final class DefaultResponseHeaders extends DefaultHttpHeaders implements ResponseHeaders {

    private final HttpStatus status;

    DefaultResponseHeaders(Collection<Entry<String, String>> entries, HttpStatus status) {
        super(entries);
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
