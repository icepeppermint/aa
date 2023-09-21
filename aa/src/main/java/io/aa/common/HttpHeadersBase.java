package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class HttpHeadersBase implements HttpHeaders {

    private static final Splitter VALUES_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    private static final Joiner VALUES_JOINER = Joiner.on(", ").skipNulls();

    private final Multimap<String, String> multimap;

    protected HttpHeadersBase() {
        multimap = ArrayListMultimap.create();
    }

    @Override
    public HttpHeaders put(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        multimap.put(name, value);
        return this;
    }

    @Override
    public HttpHeaders putAll(Multimap<String, String> multimap) {
        requireNonNull(multimap, "multimap");
        this.multimap.putAll(multimap);
        return this;
    }

    @Override
    @Nullable
    public final String get(String name) {
        requireNonNull(name, "name");
        final var values = multimap.get(name);
        if (values.isEmpty()) {
            return null;
        }
        return VALUES_JOINER.join(values);
    }

    @Override
    public final boolean contains(String name) {
        requireNonNull(name, "name");
        return multimap.containsKey(name);
    }

    @Override
    public final boolean containsValue(String name, String value, boolean ignoreCase) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        final var iterator = multimap.get(name).iterator();
        while (iterator.hasNext()) {
            for (var value0 : VALUES_SPLITTER.split(iterator.next())) {
                if (ignoreCase ? value.equalsIgnoreCase(value0) : value.equals(value0)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final void remove(String name) {
        requireNonNull(name, "name");
        multimap.removeAll(name);
    }

    @Override
    public final void remove(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        multimap.remove(name, value);
    }

    @Override
    public final void clear() {
        multimap.clear();
    }

    @Override
    public final boolean isEmpty() {
        return multimap.isEmpty();
    }

    @Override
    public final Collection<Entry<String, String>> entries() {
        return multimap.entries();
    }

    @Nullable
    public final MediaType contentType() {
        final var contentType = get(HttpHeaderNames.CONTENT_TYPE);
        if (contentType == null) {
            return null;
        }
        return MediaType.parse(contentType);
    }

    public final void contentType(MediaType contentType) {
        requireNonNull(contentType, "contentType");
        multimap.put(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
    }
}
