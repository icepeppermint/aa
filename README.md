# Aa

> Build your own reactive microservices more elegant.

Aa is a project to understand the internals of [Armeria](https://armeria.dev/).

It's missing a lot of features provided by common web frameworks.
It must never be used in your production.

## Getting started

You can simply start and stop the server.
```java
ServerBuilder sb = Server.builder().port(8080);
Server server = sb.build();
server.start();
server.stop();
```

Let's create a simple HTTP API and try it out.
```java
ServerBuilder sb = Server.builder().port(8080);
sb.service(HttpMethod.POST, "/echo", (ctx, req) -> {
    CompletableFuture<HttpResponse> future = new CompletableFuture<>();
    req.aggregate().thenAccept(aggregatedHttpRequest -> {
        ctx.blockingTaskExecutor().execute(() -> {
            // Do something blocking task here.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String contentUtf8 = aggregatedHttpRequest.contentUtf8();
            future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
        });
    });
    return HttpResponse.of(future);
});
Server server = sb.build();
server.start();

HttpClient client = HttpClient.of(server.httpUri());
AggregatedHttpResponse aggregated = client.execute(RequestHeaders.of(HttpMethod.POST, "/echo"),
                                                   HttpData.ofUtf8("Hello, World!"))
                                          .aggregate()
                                          .join();
System.err.println(aggregated.contentUtf8()); // 👈 Hello, World!
```

HTTP streaming request and response are also available.
```java
ServerBuilder sb = Server.builder().port(8080);
sb.service(HttpMethod.POST, "/echo", new HttpService() {
    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        HttpResponseWriter res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(200));
        req.subscribe(new Subscriber<>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(HttpObject object) {
                if (object instanceof HttpData) {
                    res.write(object);
                }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                res.close(t);
            }

            @Override
            public void onComplete() {
                res.close();
            }
        });
        return res;
    }
});
Server server = sb.build();
server.start();

HttpClient client = HttpClient.of(server.httpUri());
HttpRequestWriter req = HttpRequest.streaming(RequestHeaders.of(HttpMethod.POST, "/echo"));
client.execute(req).subscribe(new Subscriber<>() {

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(HttpObject object) {
        if (object instanceof HttpData httpData) {
            // Hello #0
            // Hello #1
            // Hello #2
            // Hello #3
            // Hello #4
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

for (int i = 0; i < 5; i++) {
    req.write(HttpData.ofUtf8("Hello #" + i));
}
req.close();
```

By using HTTP streaming request/response, you can create a simple chat service. First, let's look at the chat server.
```java
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
                res.close(t);
            }

            @Override
            public void onComplete() {
                res.close();
            }
        });
        return res;
    }
});
Server server = sb.build();
server.start();
```

Next is the chat client. Try running multiple chat clients and see if they work.
```java
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
```

