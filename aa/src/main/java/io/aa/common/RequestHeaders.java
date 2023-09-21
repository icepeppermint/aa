package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

public final class RequestHeaders extends DefaultHttpHeaders {

    private final HttpMethod method;
    private final String path;

    private RequestHeaders(HttpMethod method, String path, MediaType mediaType) {
        super(mediaType);
        this.method = requireNonNull(method, "method");
        this.path = requireNonNull(path, "path");
    }

    public static RequestHeaders of(HttpMethod method, String path) {
        return of(method, path, MediaType.PLAIN_TEXT_UTF_8);
    }

    public static RequestHeaders of(HttpMethod method, String path, MediaType mediaType) {
        return new RequestHeaders(method, path, mediaType);
    }

    @Override
    public RequestHeaders put(String name, String value) {
        return (RequestHeaders) super.put(name, value);
    }

    @Override
    public RequestHeaders putAll(Multimap<String, String> multimap) {
        return (RequestHeaders) super.putAll(multimap);
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }
}
