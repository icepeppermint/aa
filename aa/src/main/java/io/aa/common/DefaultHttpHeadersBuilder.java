package io.aa.common;

public final class DefaultHttpHeadersBuilder
        extends AbstractHttpHeadersBuilder<DefaultHttpHeadersBuilder>
        implements HttpHeadersBuilder {

    @Override
    public HttpHeaders build() {
        return new DefaultHttpHeaders(container());
    }
}
