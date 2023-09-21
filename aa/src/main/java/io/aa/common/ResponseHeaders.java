package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

public final class ResponseHeaders extends DefaultHttpHeaders {

    private final HttpStatus status;

    private ResponseHeaders(HttpStatus status) {
        this(status, MediaType.PLAIN_TEXT_UTF_8);
    }

    private ResponseHeaders(HttpStatus status, MediaType mediaType) {
        super(mediaType);
        this.status = requireNonNull(status, "status");
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
    public ResponseHeaders putAll(Multimap<String, String> multimap) {
        return (ResponseHeaders) super.putAll(multimap);
    }

    public HttpStatus status() {
        return status;
    }

    public int statusCode() {
        return status.code();
    }
}
