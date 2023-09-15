package io.aa.common.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.aa.common.HttpData;
import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.HttpResponse;
import io.aa.common.ResponseHeaders;
import io.aa.common.server.Server;

class HttpClientTest {

    @Test
    void options_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.OPTIONS, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.options("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void get_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.get("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void get_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.get("/", HttpData.ofUtf8("Content")).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void get_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.get("/", HttpData.ofUtf8("Content"),
                                          HttpHeaders.of()).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void head_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.HEAD, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.head("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void post_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.post("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void post_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.post("/", HttpData.ofUtf8("Content")).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void post_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.post("/", HttpData.ofUtf8("Content"),
                                           HttpHeaders.of()).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void put_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.put("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void put_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.put("/", HttpData.ofUtf8("Content")).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void put_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.put("/", HttpData.ofUtf8("Content"),
                                          HttpHeaders.of()).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void patch_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.patch("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void patch_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.patch("/", HttpData.ofUtf8("Content")).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void patch_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.patch("/", HttpData.ofUtf8("Content"),
                                            HttpHeaders.of()).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void delete_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.delete("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void delete_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.delete("/", HttpData.ofUtf8("Content")).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void delete_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.delete("/", HttpData.ofUtf8("Content"),
                                             HttpHeaders.of()).aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void trace_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.TRACE, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.trace("/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_options_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.OPTIONS, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.OPTIONS, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_get_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_get_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_get_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.GET, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_head_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.HEAD, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.HEAD, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_post_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.POST, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_post_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.POST, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_post_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.POST, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.POST, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_put_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PUT, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_put_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PUT, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_put_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PUT, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PUT, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_patch_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PATCH, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_patch_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PATCH, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_patch_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.PATCH, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.PATCH, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_delete_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.DELETE, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_delete_with_request_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8)));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.DELETE, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_delete_with_request_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.DELETE, "/", (ctx, req) -> {
            final var future = new CompletableFuture<HttpResponse>();
            req.aggregate().thenAccept(aggregatedHttpRequest -> {
                final var contentUtf8 = aggregatedHttpRequest.contentUtf8();
                final var trailers = aggregatedHttpRequest.trailers();
                future.complete(HttpResponse.of(200, HttpData.ofUtf8(contentUtf8), trailers));
            });
            return HttpResponse.of(future);
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.DELETE, "/", HttpData.ofUtf8("Content"))
                                     .aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_trace_with_request_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service(HttpMethod.TRACE, "/", (ctx, req) -> HttpResponse.of(200));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.TRACE, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_response_with_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> HttpResponse.of(ResponseHeaders.of(200)));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_response_with_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> HttpResponse.of(ResponseHeaders.of(200), HttpData.ofUtf8("Content")));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_response_with_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> HttpResponse.of(ResponseHeaders.of(200), HttpData.ofUtf8("Content"),
                                                      HttpHeaders.of()));
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.close();
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content"));
            streaming.close();
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            streaming.write(HttpData.ofUtf8("Content2"));
            streaming.close();
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content"));
            streaming.write(HttpHeaders.of());
            streaming.close();
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers() throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            streaming.write(HttpData.ofUtf8("Content2"));
            streaming.write(HttpHeaders.of());
            streaming.close();
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_before_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(ResponseHeaders.of(200));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_after_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertTrue(aggregated.content().isEmpty());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_before_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(ResponseHeaders.of(200));
                streaming.write(HttpData.ofUtf8("Content"));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_after_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content"));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_after_content_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_before_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(ResponseHeaders.of(200));
                streaming.write(HttpData.ofUtf8("Content1"));
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_after_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content1"));
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_after_content1_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_after_content2_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            streaming.write(HttpData.ofUtf8("Content2"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_trailers_before_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(ResponseHeaders.of(200));
                streaming.write(HttpData.ofUtf8("Content"));
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_trailers_after_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content"));
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_trailers_after_content_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_content_trailers_after_trailers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content"));
            streaming.write(HttpHeaders.of());
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers_before_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(ResponseHeaders.of(200));
                streaming.write(HttpData.ofUtf8("Content1"));
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers_after_headers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content1"));
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers_after_content1_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpData.ofUtf8("Content2"));
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers_after_content2_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            streaming.write(HttpData.ofUtf8("Content2"));
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.write(HttpHeaders.of());
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }

    @Test
    void execute_and_aggregate_streaming_response_with_headers_contents_trailers_after_trailers_wrote()
            throws InterruptedException {
        final var sb = Server.builder().port(0);
        sb.service("/", (ctx, req) -> {
            final var streaming = HttpResponse.streaming();
            streaming.write(ResponseHeaders.of(200));
            streaming.write(HttpData.ofUtf8("Content1"));
            streaming.write(HttpData.ofUtf8("Content2"));
            streaming.write(HttpHeaders.of());
            ctx.blockingTaskExecutor().execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                streaming.close();
            });
            return streaming;
        });
        final var server = sb.build();
        server.start();

        final var client = HttpClient.of(server.httpUri());
        final var aggregated = client.execute(HttpMethod.GET, "/").aggregate().join();
        assertEquals(200, aggregated.statusCode());
        assertEquals("Content1Content2", aggregated.contentUtf8());
        assertTrue(aggregated.trailers().isEmpty());

        server.stop();
    }
}
