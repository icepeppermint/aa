package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public final class HttpRequestWriter extends AbstractStreamWriter
        implements HttpRequest, AggregationSupport {

    private final RequestHeaders headers;

    HttpRequestWriter(RequestHeaders headers) {
        this.headers = requireNonNull(headers, "headers");
        write(headers);
    }

    @Override
    public RequestHeaders headers() {
        return headers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<AggregatedHttpRequest> aggregate(EventExecutor executor) {
        return (CompletableFuture<AggregatedHttpRequest>) aggregate(this, executor);
    }

    @Override
    public HttpVersion protocolVersion() {
        return HttpVersion.HTTP_1_1;
    }
}
