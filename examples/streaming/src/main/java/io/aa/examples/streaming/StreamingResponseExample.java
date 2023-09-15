package io.aa.examples.streaming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.aa.common.AggregatedHttpResponse;
import io.aa.common.HttpData;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.HttpResponseWriter;
import io.aa.common.ResponseHeaders;
import io.aa.common.client.HttpClient;
import io.aa.common.server.HttpService;
import io.aa.common.server.Server;
import io.aa.common.server.ServerBuilder;
import io.aa.common.server.ServiceRequestContext;

public class StreamingResponseExample {

    public static void main(String[] args) throws InterruptedException {
        ServerBuilder sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", new HttpService() {
            @Override
            public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                HttpResponseWriter res = HttpResponse.streaming();
                res.write(ResponseHeaders.of(200));
                ctx.blockingTaskExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        res.write(HttpData.ofUtf8("Hello, World!"));
                        res.close();
                    }
                });
                return res;
            }
        });
        Server server = sb.build();
        server.start();

        HttpClient client = HttpClient.of(server.httpUri());
        CompletableFuture<AggregatedHttpResponse> aggregated = client.get("/").aggregate();
        System.err.println(aggregated.join().contentUtf8());
    }
}
