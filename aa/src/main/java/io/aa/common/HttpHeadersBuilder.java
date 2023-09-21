package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

public class HttpHeadersBuilder {

    private final ImmutableListMultimap.Builder<String, String> multimap = ImmutableListMultimap.builder();

    protected HttpHeadersBuilder() {
    }

    protected HttpHeadersBuilder(Multimap<String, String> multimap) {
        this.multimap.putAll(multimap);
    }

    public HttpHeadersBuilder add(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        multimap.put(name, value);
        return this;
    }

    public HttpHeadersBuilder addAll(Multimap<String, String> multimap) {
        requireNonNull(multimap, "multimap");
        this.multimap.putAll(multimap);
        return this;
    }

    public HttpHeadersBuilder contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        multimap.put(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
        return this;
    }

    public HttpHeaders build() {
        return new DefaultHttpHeaders(multimap.build());
    }
}
