package io.aa.examples.chatservice;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.aa.common.HttpData;
import io.aa.common.HttpMethod;
import io.aa.common.HttpObject;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.HttpResponseWriter;
import io.aa.common.ResponseHeaders;
import io.aa.common.server.HttpService;
import io.aa.common.server.Server;
import io.aa.common.server.ServerBuilder;
import io.aa.common.server.ServiceRequestContext;

public class ChatServerExample {

    public static void main(String[] args) throws InterruptedException {
        ServerBuilder sb = Server.builder().port(8080);
        sb.service(HttpMethod.POST, "/chat", new HttpService() {

            Queue<HttpResponseWriter> queue = new ConcurrentLinkedQueue<>();

            @Override
            public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                HttpResponseWriter res = HttpResponse.streaming();
                res.write(ResponseHeaders.of(200));
                queue.add(res);
                req.subscribe(new Subscriber<>() {

                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.subscription.request(1);
                    }

                    @Override
                    public void onNext(HttpObject object) {
                        if (object instanceof HttpData httpData) {
                            for (HttpResponseWriter res0 : queue) {
                                if (res != res0) {
                                    res0.write(HttpData.ofUtf8("MESSAGE >> " + httpData.contentUtf8()));
                                }
                            }
                        }
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        queue.remove(res);
                        res.close(t);
                    }

                    @Override
                    public void onComplete() {
                        queue.remove(res);
                        res.close();
                    }
                });
                return res;
            }
        });
        Server server = sb.build();
        server.start();
    }
}
