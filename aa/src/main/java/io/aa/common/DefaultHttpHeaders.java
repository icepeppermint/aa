package io.aa.common;

import java.util.Collection;
import java.util.Map.Entry;

class DefaultHttpHeaders extends HttpHeadersBase implements HttpHeaders {

    DefaultHttpHeaders(Collection<Entry<String, String>> entries) {
        super(entries);
    }
}
