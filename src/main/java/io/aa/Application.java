package io.aa;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.aa.common.HttpData;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.HttpResponseWriter;
import io.aa.common.HttpStatus;
import io.aa.common.ResponseHeaders;
import io.aa.common.server.HttpService;
import io.aa.common.server.Server;
import io.aa.common.server.ServiceRequestContext;
import io.aa.common.server.annotation.Get;

public final class Application {

    public static void main(String[] args) throws InterruptedException {
        final var serverBuilder = Server.builder();
        serverBuilder.port(8888);

        serverBuilder.service("/deco", ((HttpService) (ctx, req) -> {
            System.err.println("HttpResponse.serve");
            return HttpResponse.of(200);
        }).decorate((delegate, ctx, req) -> {
            System.err.println("111");
            final var res = delegate.serve(ctx, req);
            System.err.println("222");
            return res;
        }).decorate((delegate, ctx, req) -> {
            System.err.println("333");
            final var res = delegate.serve(ctx, req);
            System.err.println("444");
            return res;
        }));
        serverBuilder.service("/", new HttpService() {
            @Override
            public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                final var res = HttpResponse.streaming();
                res.write(ResponseHeaders.of(200));
                streaming(ctx, res);
                return res;
            }

            private static void streaming(ServiceRequestContext ctx, HttpResponseWriter res) {
                ctx.blockingTaskExecutor().schedule(() -> {
                    res.write(HttpData.ofUtf8("Hello"));
                    streaming(ctx, res);
                }, 1, TimeUnit.SECONDS);
            }
        });
        serverBuilder.service(HttpMethod.GET, "/hello",
                              (ctx, req) -> HttpResponse.of(HttpStatus.OK, HttpData.ofUtf8("Hello")));
        serverBuilder.annotatedService(new Object() {
            @Get("/hello2")
            public HttpResponse hello2(HttpRequest req, ServiceRequestContext ctx) throws Exception {
                System.err.println(req.getClass().getSimpleName() + ": " + req);
                System.err.println(ctx.getClass().getSimpleName() + ": " + ctx);
                return HttpResponse.of(HttpStatus.OK, HttpData.ofUtf8("Hello2"));
            }
        });
        serverBuilder.service(HttpMethod.POST, "/hello", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(
                        HttpResponse.of(HttpStatus.OK, HttpData.ofUtf8("Hello, " + contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        serverBuilder.route()
                     .methods(HttpMethod.GET)
                     .path("/hi")
                     .build((ctx, req) -> HttpResponse.of(200, HttpData.ofUtf8("Hi!")));
        serverBuilder.withRoute(
                builder -> builder.get("/oh")
                                  .build((ctx, req) -> HttpResponse.of(200, HttpData.ofUtf8("Oh!"))));
        final var server = serverBuilder.build();
        server.start();
    }
}
