package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map.Entry;

import javax.annotation.Nullable;

class HttpHeadersBase extends StringMultimap implements HttpHeaderGetters {

    HttpHeadersBase() {}

    HttpHeadersBase(Collection<Entry<String, String>> entries) {
        add(requireNonNull(entries, "entries"));
    }

    @Override
    @Nullable
    public final MediaType contentType() {
        final var contentType = get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    public final void contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        set(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
    }
}
