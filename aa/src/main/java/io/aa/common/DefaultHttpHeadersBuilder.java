package io.aa.common;

public final class DefaultHttpHeadersBuilder
        extends AbstractHttpHeadersBuilder<DefaultHttpHeadersBuilder>
        implements HttpHeadersBuilder {

    DefaultHttpHeadersBuilder() {
        super(new HttpHeadersBase());
    }

    @Override
    public HttpHeaders build() {
        return new DefaultHttpHeaders(entries());
    }
}
