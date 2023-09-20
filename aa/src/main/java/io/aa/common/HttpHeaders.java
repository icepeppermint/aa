package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.netty.handler.codec.http.DefaultHttpHeaders;

public class HttpHeaders implements HttpObject {

    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    protected static final MediaType DEFAULT_MEDIA_TYPE = MediaType.PLAIN_TEXT_UTF_8;

    private final Multimap<String, String> multimap;

    protected HttpHeaders() {
        multimap = ArrayListMultimap.create();
    }

    public static HttpHeaders of() {
        return new HttpHeaders();
    }

    public static HttpHeaders of(String name1, String value1) {
        return of().put(name1, value1);
    }

    public static HttpHeaders of(String name1, String value1, String name2, String value2) {
        return of().put(name1, value1).put(name2, value2);
    }

    public static HttpHeaders of(String name1, String value1, String name2, String value2,
                                 String name3, String value3) {
        return of().put(name1, value1).put(name2, value2).put(name3, value3);
    }

    public static HttpHeaders of(String name1, String value1, String name2, String value2,
                                 String name3, String value3, String name4, String value4) {
        return of().put(name1, value1).put(name2, value2).put(name3, value3).put(name4, value4);
    }

    public static HttpHeaders of(String name1, String value1, String name2, String value2,
                                 String name3, String value3, String name4, String value4,
                                 String name5, String value5) {
        return of().put(name1, value1).put(name2, value2).put(name3, value3).put(name4, value4)
                   .put(name5, value5);
    }

    public static HttpHeaders of(Map<String, String> map) {
        return of().putAll(map);
    }

    public static HttpHeaders of(Multimap<String, String> multimap) {
        return of().putAll(multimap);
    }

    public static HttpHeaders of(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        return of().putAll(nettyHeaders);
    }

    public HttpHeaders put(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        multimap.put(name, value);
        return this;
    }

    public HttpHeaders putAll(Map<String, String> map) {
        requireNonNull(map, "map");
        for (var entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public HttpHeaders putAll(Multimap<String, String> multimap) {
        requireNonNull(multimap, "multimap");
        this.multimap.putAll(multimap);
        return this;
    }

    public HttpHeaders putAll(io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        requireNonNull(nettyHeaders, "nettyHeaders");
        for (var nettyHeader : nettyHeaders) {
            put(nettyHeader.getKey(), nettyHeader.getValue());
        }
        return this;
    }

    public final List<String> get(String name) {
        requireNonNull(name, "name");
        return List.copyOf(multimap.get(name));
    }

    public final boolean contains(String name) {
        requireNonNull(name, "name");
        return multimap.containsKey(name);
    }

    public final boolean containsValue(String name, String value, boolean ignoreCase) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        final var iterator = multimap.get(name).iterator();
        while (iterator.hasNext()) {
            for (var value0 : COMMA_SPLITTER.split(iterator.next())) {
                if (ignoreCase ? value.equalsIgnoreCase(value0) : value.equals(value0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public HttpHeaders remove(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        multimap.remove(name, value);
        return this;
    }

    public HttpHeaders removeAll(String name) {
        requireNonNull(name, "name");
        multimap.removeAll(name);
        return this;
    }

    public final boolean isEmpty() {
        return multimap.isEmpty();
    }

    public final io.netty.handler.codec.http.HttpHeaders asNettyHeaders() {
        final var nettyHeaders = new DefaultHttpHeaders();
        for (var name : multimap.keys()) {
            nettyHeaders.set(name, multimap.get(name));
        }
        return nettyHeaders;
    }
}
