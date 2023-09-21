package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

public class DefaultHttpHeaders extends HttpHeadersBase implements HttpHeaders {

    protected DefaultHttpHeaders(MediaType mediaType) {
        put(HttpHeaderNames.CONTENT_TYPE, requireNonNull(mediaType, "mediaType").toString());
    }

    protected DefaultHttpHeaders(Multimap<String, String> multimap) {
        putAll(multimap);
    }
}
