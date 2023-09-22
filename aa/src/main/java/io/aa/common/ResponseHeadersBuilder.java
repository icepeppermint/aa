package io.aa.common;

import java.util.Map.Entry;

public interface ResponseHeadersBuilder extends HttpHeadersBuilder {

    @Override
    ResponseHeadersBuilder add(String name, String value);

    @Override
    ResponseHeadersBuilder add(String name, Iterable<String> values);

    @Override
    ResponseHeadersBuilder add(String name, String... values);

    @Override
    ResponseHeadersBuilder add(Iterable<? extends Entry<String, String>> entries);

    @Override
    ResponseHeadersBuilder addObject(String name, Object value);

    @Override
    ResponseHeadersBuilder addObject(String name, Iterable<?> values);

    @Override
    ResponseHeadersBuilder addObject(String name, Object... values);

    @Override
    ResponseHeadersBuilder addObject(Iterable<? extends Entry<String, ?>> entries);

    @Override
    ResponseHeadersBuilder addInt(String name, int value);

    @Override
    ResponseHeadersBuilder addLong(String name, long value);

    @Override
    ResponseHeadersBuilder addFloat(String name, float value);

    @Override
    ResponseHeadersBuilder addDouble(String name, double value);

    @Override
    ResponseHeadersBuilder set(String name, String value);

    @Override
    ResponseHeadersBuilder set(String name, Iterable<String> values);

    @Override
    ResponseHeadersBuilder set(String name, String... values);

    @Override
    ResponseHeadersBuilder set(Iterable<? extends Entry<String, String>> entries);

    @Override
    ResponseHeadersBuilder setObject(String name, Object value);

    @Override
    ResponseHeadersBuilder setObject(String name, Iterable<?> values);

    @Override
    ResponseHeadersBuilder setObject(String name, Object... values);

    @Override
    ResponseHeadersBuilder setObject(Iterable<? extends Entry<String, ?>> entries);

    @Override
    ResponseHeadersBuilder setInt(String name, int value);

    @Override
    ResponseHeadersBuilder setLong(String name, long value);

    @Override
    ResponseHeadersBuilder setFloat(String name, float value);

    @Override
    ResponseHeadersBuilder setDouble(String name, double value);

    @Override
    ResponseHeadersBuilder remove(String name);

    @Override
    ResponseHeadersBuilder contentType(MediaType contentType);

    ResponseHeadersBuilder status(HttpStatus status);

    ResponseHeadersBuilder statusCode(int statusCode);

    @Override
    ResponseHeaders build();
}
