package io.aa.common;

import com.google.common.collect.Multimap;

public interface RequestHeadersBuilder extends HttpHeadersBuilder {

    RequestHeadersBuilder method(HttpMethod method);

    RequestHeadersBuilder path(String path);

    @Override
    RequestHeadersBuilder add(String name, String value);

    @Override
    RequestHeadersBuilder addAll(Multimap<String, String> multimap);

    @Override
    RequestHeadersBuilder contentType(MediaType contentType);

    @Override
    RequestHeaders build();
}
