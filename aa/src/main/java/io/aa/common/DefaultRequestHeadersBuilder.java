package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class DefaultRequestHeadersBuilder
        extends AbstractHttpHeadersBuilder<DefaultRequestHeadersBuilder>
        implements RequestHeadersBuilder {

    private HttpMethod method;
    private String path;

    @Override
    public RequestHeadersBuilder method(HttpMethod method) {
        this.method = requireNonNull(method, "method");
        return this;
    }

    @Override
    public RequestHeadersBuilder path(String path) {
        this.path = requireNonNull(path, "path");
        return this;
    }

    @Override
    public RequestHeaders build() {
        return new DefaultRequestHeaders(container(), method, path);
    }
}
