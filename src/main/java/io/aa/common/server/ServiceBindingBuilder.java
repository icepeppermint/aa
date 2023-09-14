package io.aa.common.server;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.annotation.Nullable;

import io.aa.common.HttpMethod;

public final class ServiceBindingBuilder {

    private final ServerBuilder serverBuilder;
    @Nullable
    private List<HttpMethod> methods;
    @Nullable
    private String path;

    ServiceBindingBuilder(ServerBuilder serverBuilder) {
        this.serverBuilder = requireNonNull(serverBuilder, "serverBuilder");
    }

    public ServiceBindingBuilder get(String path) {
        return methods(HttpMethod.GET).path(path);
    }

    public ServiceBindingBuilder post(String path) {
        return methods(HttpMethod.POST).path(path);
    }

    public ServiceBindingBuilder put(String path) {
        return methods(HttpMethod.PUT).path(path);
    }

    public ServiceBindingBuilder patch(String path) {
        return methods(HttpMethod.PATCH).path(path);
    }

    public ServiceBindingBuilder delete(String path) {
        return methods(HttpMethod.DELETE).path(path);
    }

    public ServiceBindingBuilder methods(HttpMethod... methods) {
        requireNonNull(methods, "methods");
        this.methods = List.of(methods);
        return this;
    }

    public ServiceBindingBuilder path(String path) {
        this.path = requireNonNull(path, "path");
        return this;
    }

    public ServerBuilder build(HttpService service) {
        requireNonNull(service, "service");
        checkState(methods != null, "methods == null");
        checkState(path != null, "path == null");
        for (var method : methods) {
            serverBuilder.service(method, path, service);
        }
        return serverBuilder;
    }
}
