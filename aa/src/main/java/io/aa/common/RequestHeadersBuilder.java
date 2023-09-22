package io.aa.common;

import java.util.Map.Entry;

public interface RequestHeadersBuilder extends HttpHeadersBuilder {

    @Override
    RequestHeadersBuilder add(String name, String value);

    @Override
    RequestHeadersBuilder add(String name, Iterable<String> values);

    @Override
    RequestHeadersBuilder add(String name, String... values);

    @Override
    RequestHeadersBuilder add(Iterable<? extends Entry<String, String>> entries);

    @Override
    RequestHeadersBuilder addObject(String name, Object value);

    @Override
    RequestHeadersBuilder addObject(String name, Iterable<?> values);

    @Override
    RequestHeadersBuilder addObject(String name, Object... values);

    @Override
    RequestHeadersBuilder addObject(Iterable<? extends Entry<String, ?>> entries);

    @Override
    RequestHeadersBuilder addInt(String name, int value);

    @Override
    RequestHeadersBuilder addLong(String name, long value);

    @Override
    RequestHeadersBuilder addFloat(String name, float value);

    @Override
    RequestHeadersBuilder addDouble(String name, double value);

    @Override
    RequestHeadersBuilder set(String name, String value);

    @Override
    RequestHeadersBuilder set(String name, Iterable<String> values);

    @Override
    RequestHeadersBuilder set(String name, String... values);

    @Override
    RequestHeadersBuilder set(Iterable<? extends Entry<String, String>> entries);

    @Override
    RequestHeadersBuilder setObject(String name, Object value);

    @Override
    RequestHeadersBuilder setObject(String name, Iterable<?> values);

    @Override
    RequestHeadersBuilder setObject(String name, Object... values);

    @Override
    RequestHeadersBuilder setObject(Iterable<? extends Entry<String, ?>> entries);

    @Override
    RequestHeadersBuilder setInt(String name, int value);

    @Override
    RequestHeadersBuilder setLong(String name, long value);

    @Override
    RequestHeadersBuilder setFloat(String name, float value);

    @Override
    RequestHeadersBuilder setDouble(String name, double value);

    @Override
    RequestHeadersBuilder remove(String name);

    @Override
    RequestHeadersBuilder contentType(MediaType contentType);

    RequestHeadersBuilder method(HttpMethod method);

    RequestHeadersBuilder path(String path);

    @Override
    RequestHeaders build();
}
