package io.aa.common;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

abstract class AbstractHttpHeadersBuilder<SELF>
        extends StringMultimapBuilder<HttpHeadersBase, SELF> {

    AbstractHttpHeadersBuilder(HttpHeadersBase container) {
        super(container);
    }

    public final SELF contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        set(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
        return self();
    }

    @Nullable
    public final MediaType contentType() {
        final String contentType = get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null ? MediaType.parse(contentType) : null;
    }
}
