package io.aa.examples.streaming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.aa.common.AggregatedHttpRequest;
import io.aa.common.AggregatedHttpResponse;
import io.aa.common.HttpData;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpRequestWriter;
import io.aa.common.HttpResponse;
import io.aa.common.RequestHeaders;
import io.aa.common.client.HttpClient;
import io.aa.common.server.Server;
import io.aa.common.server.ServerBuilder;

public class StreamingRequestExample {

    public static void main(String[] args) throws InterruptedException {
        ServerBuilder sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> {
            CompletableFuture<HttpResponse> future = new CompletableFuture<>();
            req.aggregate().thenAccept(new Consumer<AggregatedHttpRequest>() {
                @Override
                public void accept(AggregatedHttpRequest aggregatedHttpRequest) {
                    String contentUtf8 = aggregatedHttpRequest.contentUtf8();
                    future.complete(HttpResponse.of(200, contentUtf8));
                }
            });
            return HttpResponse.of(future);
        });
        Server server = sb.build();
        server.start();

        HttpClient client = HttpClient.of(server.httpUri());
        HttpRequestWriter req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.POST, "/"));
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                req.write(HttpData.ofUtf8("Hello"));
                req.write(HttpData.ofUtf8(", World!"));
                req.close();
            }
        });
        CompletableFuture<AggregatedHttpResponse> aggregated = client.execute(req).aggregate();
        System.err.println(aggregated.join().contentUtf8());
    }
}
