package io.aa.common;

import java.util.Map.Entry;

public interface HttpHeadersBuilder extends HttpHeaderGetters {

    HttpHeadersBuilder add(String name, String value);

    HttpHeadersBuilder add(String name, Iterable<String> values);

    HttpHeadersBuilder add(String name, String... values);

    HttpHeadersBuilder add(Iterable<? extends Entry<String, String>> entries);

    HttpHeadersBuilder addObject(String name, Object value);

    HttpHeadersBuilder addObject(String name, Iterable<?> values);

    HttpHeadersBuilder addObject(String name, Object... values);

    HttpHeadersBuilder addObject(Iterable<? extends Entry<String, ?>> entries);

    HttpHeadersBuilder addInt(String name, int value);

    HttpHeadersBuilder addLong(String name, long value);

    HttpHeadersBuilder addFloat(String name, float value);

    HttpHeadersBuilder addDouble(String name, double value);

    HttpHeadersBuilder set(String name, String value);

    HttpHeadersBuilder set(String name, Iterable<String> values);

    HttpHeadersBuilder set(String name, String... values);

    HttpHeadersBuilder set(Iterable<? extends Entry<String, String>> entries);

    HttpHeadersBuilder setObject(String name, Object value);

    HttpHeadersBuilder setObject(String name, Iterable<?> values);

    HttpHeadersBuilder setObject(String name, Object... values);

    HttpHeadersBuilder setObject(Iterable<? extends Entry<String, ?>> entries);

    HttpHeadersBuilder setInt(String name, int value);

    HttpHeadersBuilder setLong(String name, long value);

    HttpHeadersBuilder setFloat(String name, float value);

    HttpHeadersBuilder setDouble(String name, double value);

    HttpHeadersBuilder remove(String name);

    void clear();

    HttpHeadersBuilder contentType(MediaType contentType);

    HttpHeaders build();
}
