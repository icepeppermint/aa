package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.aa.common.AggregatedHttpRequest;
import io.aa.common.HttpMethod;
import io.aa.common.HttpObject;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.HttpResponseWriter;
import io.aa.common.server.annotation.Delete;
import io.aa.common.server.annotation.Get;
import io.aa.common.server.annotation.Patch;
import io.aa.common.server.annotation.Post;
import io.aa.common.server.annotation.Put;

public final class ServerBuilder {

    private static final Map<Class<? extends Annotation>, HttpMethod>
            HTTP_METHOD_ANNOTATION_CLASS_TO_HTTP_METHOD = Map.of(Get.class, HttpMethod.GET,
                                                                 Post.class, HttpMethod.POST,
                                                                 Put.class, HttpMethod.PUT,
                                                                 Patch.class, HttpMethod.PATCH,
                                                                 Delete.class, HttpMethod.DELETE);
    private static final Set<Class<? extends Annotation>> HTTP_METHOD_ANNOTATION_CLASSES =
            HTTP_METHOD_ANNOTATION_CLASS_TO_HTTP_METHOD.keySet();

    private int port;
    private final Route route;

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
        final HttpMethod httpMethod =
                HTTP_METHOD_ANNOTATION_CLASS_TO_HTTP_METHOD.get(httpMethodAnnotationClass);
        if (httpMethod == null) {
            throw new IllegalStateException(
                    "Unhandled HTTP method annotation: " + httpMethodAnnotation.getClass().getSimpleName());
        }
        for (String path : pathValue(httpMethodAnnotation)) {
            route.add(httpMethod, prefix + path, new AnnotatedHttpService(object, method));
        }
    }

    private static <T extends Annotation> String[] pathValue(T httpMethodAnnotation) {
        requireNonNull(httpMethodAnnotation, "httpMethodAnnotation");
        if (httpMethodAnnotation instanceof Get annotatedHttpGet) {
            return annotatedHttpGet.value();
        } else if (httpMethodAnnotation instanceof Post annotatedHttpPost) {
            return annotatedHttpPost.value();
        } else if (httpMethodAnnotation instanceof Put annotatedHttpPut) {
            return annotatedHttpPut.value();
        } else if (httpMethodAnnotation instanceof Patch annotatedHttpPatch) {
            return annotatedHttpPatch.value();
        } else {
            assert httpMethodAnnotation instanceof Delete;
            return ((Delete) httpMethodAnnotation).value();
        }
    }

    private static final class AnnotatedHttpService implements HttpService {

        final Object object;
        final Method method;

        AnnotatedHttpService(Object object, Method method) {
            this.object = requireNonNull(object, "object");
            this.method = requireNonNull(method, "method");
        }

        @Override
        public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
            assert method != null;
            checkMethodParameterTypes();
            method.setAccessible(true);
            return needsDirectInvocation() ? serve0(ctx, req) : delegate(ctx, req);
        }

        void checkMethodParameterTypes() {
            assert method != null;
            boolean containsHttpRequestType = false;
            boolean containsAggregatedHttpRequestType = false;
            for (Class<?> parameterType : method.getParameterTypes()) {
                if (parameterType.equals(HttpRequest.class)) {
                    containsHttpRequestType = true;
                } else if (parameterType.equals(AggregatedHttpRequest.class)) {
                    containsAggregatedHttpRequestType = true;
                }
            }
            if (containsHttpRequestType && containsAggregatedHttpRequestType) {
                throw new IllegalStateException(
                        HttpRequest.class.getSimpleName() + " and "
                        + AggregatedHttpRequest.class.getSimpleName()
                        + " cannot be used together in the parameter type of a service method.");
            }
        }

        boolean needsDirectInvocation() {
            assert method != null;
            for (Class<?> parameterType : method.getParameterTypes()) {
                if (parameterType.equals(AggregatedHttpRequest.class)) {
                    return false;
                }
            }
            return true;
        }

        HttpResponse serve0(ServiceRequestContext ctx, Object req) throws Exception {
            requireNonNull(ctx, "ctx");
            requireNonNull(req, "req");
            return (HttpResponse) method.invoke(object, args(ctx, req));
        }

        Object[] args(ServiceRequestContext ctx, Object req) {
            requireNonNull(ctx, "ctx");
            requireNonNull(req, "req");
            final List<Object> args = new ArrayList<>();
            for (Class<?> parameterType : method.getParameterTypes()) {
                Object arg = null;
                if (parameterType.isInstance(ctx)) {
                    arg = ctx;
                } else if (parameterType.isInstance(req)) {
                    arg = req;
                }
                if (arg != null) {
                    args.add(arg);
                }
            }
            return args.toArray();
        }

        HttpResponseWriter delegate(ServiceRequestContext ctx, HttpRequest req) throws Exception {
            requireNonNull(ctx, "ctx");
            requireNonNull(req, "req");
            final HttpResponseWriter downstream = HttpResponse.streaming();
            req.aggregate().thenAcceptAsync(aggregatedReq -> {
                final HttpResponse upstream;
                try {
                    upstream = serve0(ctx, aggregatedReq);
                } catch (Exception e) {
                    downstream.close(e);
                    return;
                }
                upstream.subscribe(new UpstreamSubscriber(downstream), ctx.eventLoop());
            }, ctx.eventLoop());
            return downstream;
        }

        private static final class UpstreamSubscriber implements Subscriber<HttpObject> {

            HttpResponseWriter downstream;

            UpstreamSubscriber(HttpResponseWriter downstream) {
                this.downstream = requireNonNull(downstream, "downstream");
            }

            @Override
            public void onSubscribe(Subscription subscription) {
                requireNonNull(subscription, "subscription").request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(HttpObject object) {
                downstream.write(object);
            }

            @Override
            public void onError(Throwable t) {
                downstream.close(t);
            }

            @Override
            public void onComplete() {
                downstream.close();
            }
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
