package io.aa.examples.chatservice;

import java.util.Scanner;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.aa.common.HttpData;
import io.aa.common.HttpMethod;
import io.aa.common.HttpObject;
import io.aa.common.HttpRequest;
import io.aa.common.HttpRequestWriter;
import io.aa.common.RequestHeaders;
import io.aa.common.client.HttpClient;

public class ChatClientExample {

    public static void main(String[] args) throws InterruptedException {
        HttpClient client = HttpClient.of("http://localhost:8080");
        HttpRequestWriter req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.POST, "/chat"));
        client.execute(req).subscribe(new Subscriber<HttpObject>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(HttpObject object) {
                if (object instanceof HttpData httpData) {
                    System.err.println(httpData.contentUtf8());
                }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                req.close();
                break;
            }
            req.write(HttpData.ofUtf8(input));
        }
    }
}
