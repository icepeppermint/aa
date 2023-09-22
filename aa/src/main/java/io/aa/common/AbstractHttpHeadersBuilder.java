package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

abstract class AbstractHttpHeadersBuilder<SELF extends HttpHeadersBuilder> {

    private final ImmutableListMultimap.Builder<String, String> container;

    AbstractHttpHeadersBuilder() {
        container = ImmutableListMultimap.builder();
    }

    public final SELF add(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        container.put(name, value);
        return self();
    }

    public final SELF addAll(Multimap<String, String> multimap) {
        requireNonNull(multimap, "multimap");
        container.putAll(multimap);
        return self();
    }

    public final SELF contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        container.put(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
        return self();
    }

    protected final ImmutableListMultimap<String, String> container() {
        return container.build();
    }

    @SuppressWarnings("unchecked")
    private SELF self() {
        return (SELF) this;
    }
}
