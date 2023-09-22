package io.aa.common;

import com.google.common.collect.Multimap;

public interface ResponseHeadersBuilder extends HttpHeadersBuilder {

    ResponseHeadersBuilder status(HttpStatus status);

    ResponseHeadersBuilder statusCode(int statusCode);

    @Override
    ResponseHeadersBuilder add(String name, String value);

    @Override
    ResponseHeadersBuilder addAll(Multimap<String, String> multimap);

    @Override
    ResponseHeadersBuilder contentType(MediaType contentType);

    @Override
    ResponseHeaders build();
}
