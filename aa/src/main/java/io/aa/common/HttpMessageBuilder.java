package io.aa.common;

import static java.util.Objects.requireNonNull;

abstract class HttpMessageBuilder<T extends HttpMessage> {

    private HttpData content;
    private HttpHeaders trailers;

    public HttpMessageBuilder<T> content(HttpData content) {
        this.content = requireNonNull(content, "content");
        return this;
    }

    public HttpMessageBuilder<T> content(String content) {
        return content(HttpData.ofUtf8(content));
    }

    public HttpMessageBuilder<T> content(byte[] content) {
        return content(HttpData.of(content));
    }

    public final HttpData content() {
        return content;
    }

    public HttpMessageBuilder<T> trailers(HttpHeaders trailers) {
        this.trailers = requireNonNull(trailers, "trailers");
        return this;
    }

    public final HttpHeaders trailers() {
        return trailers;
    }

    public abstract T build();
}
