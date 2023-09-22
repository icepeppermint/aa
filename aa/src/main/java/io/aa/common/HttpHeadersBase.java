package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

abstract class HttpHeadersBase implements HttpHeaderGetters {

    private static final Splitter VALUES_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    private static final Joiner VALUES_JOINER = Joiner.on(", ").skipNulls();

    private final Multimap<String, String> container;

    HttpHeadersBase() {
        container = ArrayListMultimap.create();
    }

    public void put(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        container.put(name, value);
    }

    public void putAll(Multimap<String, String> multimap) {
        requireNonNull(multimap, "multimap");
        container.putAll(multimap);
    }

    @Override
    @Nullable
    public final String get(String name) {
        requireNonNull(name, "name");
        final var values = container.get(name);
        if (values.isEmpty()) {
            return null;
        }
        return VALUES_JOINER.join(values);
    }

    @Override
    public final boolean contains(String name) {
        requireNonNull(name, "name");
        return container.containsKey(name);
    }

    @Override
    public final boolean containsValue(String name, String value, boolean ignoreCase) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        final var iterator = container.get(name).iterator();
        while (iterator.hasNext()) {
            for (var value0 : VALUES_SPLITTER.split(iterator.next())) {
                if (ignoreCase ? value.equalsIgnoreCase(value0) : value.equals(value0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final void remove(String name) {
        requireNonNull(name, "name");
        container.removeAll(name);
    }

    public final void remove(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        container.remove(name, value);
    }

    public final void clear() {
        container.clear();
    }

    @Override
    public final boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public final Collection<Entry<String, String>> entries() {
        return container.entries();
    }

    @Override
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
        container.put(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
    }
}
