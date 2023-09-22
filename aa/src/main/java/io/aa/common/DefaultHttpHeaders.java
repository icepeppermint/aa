package io.aa.common;

import com.google.common.collect.Multimap;

class DefaultHttpHeaders extends HttpHeadersBase implements HttpHeaders {

    DefaultHttpHeaders(Multimap<String, String> multimap) {
        putAll(multimap);
    }
}
