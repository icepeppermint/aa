package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class DefaultResponseHeadersBuilder
        extends AbstractHttpHeadersBuilder<DefaultResponseHeadersBuilder>
        implements ResponseHeadersBuilder {

    private HttpStatus status;

    DefaultResponseHeadersBuilder() {
        super(new HttpHeadersBase());
    }

    @Override
    public ResponseHeadersBuilder status(HttpStatus status) {
        this.status = requireNonNull(status, "status");
        return this;
    }

    @Override
    public ResponseHeadersBuilder statusCode(int statusCode) {
        status = HttpStatus.valueOf(statusCode);
        return this;
    }

    @Override
    public ResponseHeaders build() {
        return new DefaultResponseHeaders(entries(), status);
    }
}
