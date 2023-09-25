package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.server.annotation.Delete;
import io.aa.common.server.annotation.Get;
import io.aa.common.server.annotation.Patch;
import io.aa.common.server.annotation.Post;
import io.aa.common.server.annotation.Put;

public final class ServerBuilder {

    private static final List<Class<? extends Annotation>> HTTP_METHOD_ANNOTATION_CLASSES =
            List.of(Get.class, Post.class, Put.class, Patch.class, Delete.class);

    private final Route route;
    private int port;

    ServerBuilder() {
        route = new Route();
    }

    public ServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ServerBuilder service(String path, HttpService service) {
        return service(HttpMethod.GET, path, service);
    }

    public ServerBuilder service(HttpMethod method, String path, HttpService service) {
        route.add(method, path, service);
        return this;
    }

    public ServerBuilder annotatedService(Object object) {
        return annotatedService("", object);
    }

    public ServerBuilder annotatedService(String prefix, Object object) {
        requireNonNull(prefix, "prefix");
        requireNonNull(object, "object");
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!isPublic(method)) {
                continue;
            }
            for (Class<? extends Annotation> httpMethodAnnotationClass : HTTP_METHOD_ANNOTATION_CLASSES) {
                routeAdd(httpMethodAnnotationClass, prefix, object, method);
            }
        }
        return this;
    }

    private static boolean isPublic(Method method) {
        requireNonNull(method, "method");
        return Modifier.isPublic(method.getModifiers());
    }

    private <T extends Annotation> void routeAdd(Class<T> httpMethodAnnotationClass, String prefix,
                                                 Object object, Method method) {
        requireNonNull(httpMethodAnnotationClass, "httpMethodAnnotationClass");
        requireNonNull(prefix, "prefix");
        requireNonNull(object, "object");
        requireNonNull(method, "method");

        assert isPublic(method);
        final T httpMethodAnnotation = method.getDeclaredAnnotation(httpMethodAnnotationClass);
        if (httpMethodAnnotation == null) {
            return;
        }
        final HttpMethod httpMethod;
        final String[] path;
        if (httpMethodAnnotation instanceof Get annotatedHttpGet) {
            httpMethod = HttpMethod.GET;
            path = annotatedHttpGet.value();
        } else if (httpMethodAnnotation instanceof Post annotatedHttpPost) {
            httpMethod = HttpMethod.POST;
            path = annotatedHttpPost.value();
        } else if (httpMethodAnnotation instanceof Put annotatedHttpPut) {
            httpMethod = HttpMethod.PUT;
            path = annotatedHttpPut.value();
        } else if (httpMethodAnnotation instanceof Patch annotatedHttpPatch) {
            httpMethod = HttpMethod.PATCH;
            path = annotatedHttpPatch.value();
        } else if (httpMethodAnnotation instanceof Delete annotatedHttpDelete) {
            httpMethod = HttpMethod.DELETE;
            path = annotatedHttpDelete.value();
        } else {
            throw new IllegalStateException(
                    "Unhandled HTTP method annotation: " + httpMethodAnnotation.getClass().getSimpleName());
        }
        for (String path0 : path) {
            route.add(httpMethod, prefix + path0, new AnnotatedHttpService(object, method));
        }
    }

    private static final class AnnotatedHttpService implements HttpService {

        private final Object object;
        private final Method method;

        private AnnotatedHttpService(Object object, Method method) {
            this.object = requireNonNull(object, "object");
            this.method = requireNonNull(method, "method");
        }

        @Override
        public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
            method.setAccessible(true);
            return (HttpResponse) method.invoke(object, args(ctx, req));
        }

        private Object[] args(ServiceRequestContext ctx, HttpRequest req) {
            requireNonNull(ctx, "ctx");
            requireNonNull(req, "req");
            final ArrayList<Object> args = new ArrayList<>();
            for (Class<?> parameterType : method.getParameterTypes()) {
                Object arg = null;
                if (parameterType.equals(ServiceRequestContext.class)) {
                    arg = ctx;
                } else if (parameterType.equals(HttpRequest.class)) {
                    arg = req;
                }
                if (arg != null) {
                    args.add(arg);
                }
            }
            return args.toArray();
        }
    }

    public ServiceBindingBuilder route() {
        return new ServiceBindingBuilder(this);
    }

    public ServerBuilder withRoute(Consumer<ServiceBindingBuilder> consumer) {
        requireNonNull(consumer, "consumer");
        consumer.accept(route());
        return this;
    }

    public Server build() {
        return new Server(port, route);
    }
}
