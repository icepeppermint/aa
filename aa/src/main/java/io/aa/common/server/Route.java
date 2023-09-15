package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import io.aa.common.HttpMethod;
import io.aa.common.RequestHeaders;

final class Route {

    private final Table<String, String, HttpService> table = HashBasedTable.create();

    public void add(HttpMethod method, String path, HttpService service) {
        requireNonNull(method, "method");
        requireNonNull(path, "path");
        requireNonNull(service, "service");
        table.put(method.name(), path, service);
    }

    @Nullable
    public HttpService get(HttpMethod method, String path) {
        requireNonNull(method, "method");
        requireNonNull(path, "path");
        return table.get(method.name(), path);
    }

    @Nullable
    public HttpService get(RequestHeaders headers) {
        requireNonNull(headers, "headers");
        return get(headers.method(), headers.path());
    }
}
