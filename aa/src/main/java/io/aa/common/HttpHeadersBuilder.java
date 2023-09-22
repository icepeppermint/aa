package io.aa.common;

import com.google.common.collect.Multimap;

public interface HttpHeadersBuilder {

    HttpHeadersBuilder add(String name, String value);

    HttpHeadersBuilder addAll(Multimap<String, String> multimap);

    HttpHeadersBuilder contentType(MediaType contentType);

    HttpHeaders build();
}
