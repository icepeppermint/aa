package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.netty.handler.codec.http.DefaultHttpHeaders;

public class HttpHeaders implements HttpObject {

    private final Multimap<String, String> multimap;

    protected HttpHeaders() {
        multimap = ArrayListMultimap.create();
    }

    public static HttpHeaders of() {
        return new HttpHeaders();
    }

    public static HttpHeaders of(String k1, String v1) {
        return of().put(k1, v1);
    }

    public static HttpHeaders of(String k1, String v1, String k2, String v2) {
        return of().put(k1, v1).put(k2, v2);
    }

    public static HttpHeaders of(String k1, String v1, String k2, String v2, String k3, String v3) {
        return of().put(k1, v1).put(k2, v2).put(k3, v3);
    }

    public static HttpHeaders of(String k1, String v1, String k2, String v2, String k3, String v3,
                                 String k4, String v4) {
        return of().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4);
    }

    public static HttpHeaders of(String k1, String v1, String k2, String v2, String k3, String v3,
                                 String k4, String v4, String k5, String v5) {
        return of().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5);
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

    public HttpHeaders put(String key, String value) {
        requireNonNull(key, "key");
        requireNonNull(value, "value");
        multimap.put(key, value);
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

    public final List<String> get(String key) {
        requireNonNull(key, "key");
        return List.copyOf(multimap.get(key));
    }

    public HttpHeaders remove(String key, String value) {
        requireNonNull(key, "key");
        requireNonNull(value, "value");
        multimap.remove(key, value);
        return this;
    }

    public HttpHeaders removeAll(String key) {
        requireNonNull(key, "key");
        multimap.removeAll(key);
        return this;
    }

    public final boolean isEmpty() {
        return multimap.isEmpty();
    }

    public final io.netty.handler.codec.http.HttpHeaders asNettyHeaders() {
        final var nettyHeaders = new DefaultHttpHeaders();
        for (var key : multimap.keys()) {
            nettyHeaders.set(key, multimap.get(key));
        }
        return nettyHeaders;
    }
}
