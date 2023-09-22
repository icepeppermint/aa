package io.aa.common;

import java.util.Collection;
import java.util.Map.Entry;

public interface HttpHeaderGetters {

    String get(String name);

    boolean contains(String name);

    boolean containsValue(String name, String value, boolean ignoreCase);

    boolean isEmpty();

    Collection<Entry<String, String>> entries();

    MediaType contentType();
}
