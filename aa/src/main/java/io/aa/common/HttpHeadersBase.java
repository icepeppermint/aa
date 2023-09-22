package io.aa.common;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

class HttpHeadersBase extends StringMultimap implements HttpHeaderGetters {

    HttpHeadersBase(Multimap<String, String> multimap) {
        add(requireNonNull(multimap, "multimap").entries());
    }

    @Override
    @Nullable
    public final MediaType contentType() {
        final var contentType = get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    public final void contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        add(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
    }
}
