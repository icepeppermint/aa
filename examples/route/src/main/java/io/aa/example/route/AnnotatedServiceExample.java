package io.aa.example.route;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.aa.common.AggregatedHttpRequest;
import io.aa.common.AggregatedHttpResponse;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.client.HttpClient;
import io.aa.common.server.Server;
import io.aa.common.server.ServerBuilder;
import io.aa.common.server.ServiceRequestContext;
import io.aa.common.server.annotation.Post;

public class AnnotatedServiceExample {

    public static void main(String[] args) throws InterruptedException {
        ServerBuilder sb = Server.builder().port(0);
        sb.annotatedService(new Object() {
            @Post("/hello")
            public HttpResponse hello(ServiceRequestContext ctx, HttpRequest req) {
                CompletableFuture<HttpResponse> future = new CompletableFuture<>();
                req.aggregate().thenAccept(new Consumer<AggregatedHttpRequest>() {
                    @Override
                    public void accept(AggregatedHttpRequest aggregatedHttpRequest) {
                        String contentUtf8 = aggregatedHttpRequest.contentUtf8();
                        future.complete(HttpResponse.of(200, "Hello " + contentUtf8));
                    }
                });
                return HttpResponse.of(future);
            }
        });
        sb.annotatedService("/prefix", new Object() {
            @Post("/hello")
            public HttpResponse hello(ServiceRequestContext ctx, HttpRequest req) {
                CompletableFuture<HttpResponse> future = new CompletableFuture<>();
                req.aggregate().thenAccept(new Consumer<AggregatedHttpRequest>() {
                    @Override
                    public void accept(AggregatedHttpRequest aggregatedHttpRequest) {
                        String contentUtf8 = aggregatedHttpRequest.contentUtf8();
                        future.complete(HttpResponse.of(200, "Hello " + contentUtf8));
                    }
                });
                return HttpResponse.of(future);
            }
        });
        Server server = sb.build();
        server.start();

        HttpClient client = HttpClient.of(server.httpUri());
        CompletableFuture<AggregatedHttpResponse> aggregated;

        aggregated = client.post("/hello", "Content").aggregate();
        System.err.println(aggregated.join().contentUtf8());

        aggregated = client.post("/prefix/hello", "Content").aggregate();
        System.err.println(aggregated.join().contentUtf8());
    }
}
