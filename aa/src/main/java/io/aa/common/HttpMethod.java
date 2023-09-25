package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

public final class HttpMethod {

    private static final Map<String, HttpMethod> KNOWN_METHODS = new HashMap<>();

    private static final String OPTIONS_STRING = "OPTIONS";
    private static final String GET_STRING = "GET";
    private static final String HEAD_STRING = "HEAD";
    private static final String POST_STRING = "POST";
    private static final String PUT_STRING = "PUT";
    private static final String PATCH_STRING = "PATCH";
    private static final String DELETE_STRING = "DELETE";
    private static final String TRACE_STRING = "TRACE";
    private static final String CONNECT_STRING = "CONNECT";

    public static final HttpMethod OPTIONS = newConstant(OPTIONS_STRING);
    public static final HttpMethod GET = newConstant(GET_STRING);
    public static final HttpMethod HEAD = newConstant(HEAD_STRING);
    public static final HttpMethod POST = newConstant(POST_STRING);
    public static final HttpMethod PUT = newConstant(PUT_STRING);
    public static final HttpMethod PATCH = newConstant(PATCH_STRING);
    public static final HttpMethod DELETE = newConstant(DELETE_STRING);
    public static final HttpMethod TRACE = newConstant(TRACE_STRING);
    public static final HttpMethod CONNECT = newConstant(CONNECT_STRING);

    private final String name;

    private HttpMethod(String name) {
        this.name = requireNonNull(name, "name");
    }

    private static HttpMethod newConstant(String name) {
        requireNonNull(name, "name");
        final HttpMethod httpMethod = new HttpMethod(name);
        KNOWN_METHODS.put(name, httpMethod);
        return httpMethod;
    }

    public static HttpMethod valueOf(String name) {
        requireNonNull(name, "name");
        // fast-path
        if (name == GET_STRING) {
            return GET;
        }
        if (name == POST_STRING) {
            return POST;
        }
        if (name == PUT_STRING) {
            return PUT;
        }
        if (name == PATCH_STRING) {
            return PATCH;
        }
        if (name == DELETE_STRING) {
            return DELETE;
        }
        return KNOWN_METHODS.get(name);
    }

    public String name() {
        return name;
    }
}
