package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class HttpResponseBuilder extends HttpMessageBuilder<HttpResponse> {

    private ResponseHeaders headers;

    HttpResponseBuilder() {}

    public HttpResponseBuilder headers(ResponseHeaders headers) {
        this.headers = requireNonNull(headers, "headers");
        return this;
    }

    @Override
    public HttpResponseBuilder content(HttpData content) {
        return (HttpResponseBuilder) super.content(content);
    }

    @Override
    public HttpResponseBuilder content(String content) {
        return (HttpResponseBuilder) super.content(content);
    }

    @Override
    public HttpResponseBuilder content(byte[] content) {
        return (HttpResponseBuilder) super.content(content);
    }

    @Override
    public HttpResponseBuilder trailers(HttpHeaders trailers) {
        return (HttpResponseBuilder) super.trailers(trailers);
    }

    @Override
    public HttpResponse build() {
        return HttpResponse.of(headers, content, trailers);
    }
}
