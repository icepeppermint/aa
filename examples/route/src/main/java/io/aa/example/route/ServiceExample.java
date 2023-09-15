package io.aa.example.route;

import java.util.concurrent.CompletableFuture;

import io.aa.common.AggregatedHttpResponse;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.client.HttpClient;
import io.aa.common.server.HttpService;
import io.aa.common.server.Server;
import io.aa.common.server.ServerBuilder;
import io.aa.common.server.ServiceRequestContext;

public class ServiceExample {

    public static void main(String[] args) throws InterruptedException {
        ServerBuilder sb = Server.builder().port(0);
        sb.service("/hello1", new HttpService() {
            @Override
            public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                return HttpResponse.of(200);
            }
        });
        sb.service(HttpMethod.GET, "/hello2", new HttpService() {
            @Override
            public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                return HttpResponse.of(200);
            }
        });
        Server server = sb.build();
        server.start();

        HttpClient client = HttpClient.of(server.httpUri());

        CompletableFuture<AggregatedHttpResponse> aggregated1 = client.get("/hello1").aggregate();
        System.err.println(aggregated1.join().statusCode());

        CompletableFuture<AggregatedHttpResponse> aggregated2 = client.get("/hello2").aggregate();
        System.err.println(aggregated2.join().statusCode());
    }
}
