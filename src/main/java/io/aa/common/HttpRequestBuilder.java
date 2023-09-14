package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class HttpRequestBuilder extends HttpMessageBuilder<HttpRequest> {

    private RequestHeaders headers;

    HttpRequestBuilder() {}

    public HttpRequestBuilder headers(RequestHeaders headers) {
        this.headers = requireNonNull(headers, "headers");
        return this;
    }

    @Override
    public HttpRequestBuilder content(HttpData content) {
        return (HttpRequestBuilder) super.content(content);
    }

    @Override
    public HttpRequestBuilder content(String content) {
        return (HttpRequestBuilder) super.content(content);
    }

    @Override
    public HttpRequestBuilder content(byte[] content) {
        return (HttpRequestBuilder) super.content(content);
    }

    @Override
    public HttpRequestBuilder trailers(HttpHeaders trailers) {
        return (HttpRequestBuilder) super.trailers(trailers);
    }

    @Override
    public HttpRequest build() {
        return HttpRequest.of(headers, content, trailers);
    }
}
