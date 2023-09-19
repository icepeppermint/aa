package io.aa.common;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public final class HttpResponseWriter extends AbstractStreamWriter
        implements HttpResponse, AggregationSupport {

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<AggregatedHttpResponse> aggregate(EventExecutor executor) {
        return (CompletableFuture<AggregatedHttpResponse>) aggregate(this, executor);
    }

    @Override
    public HttpVersion protocolVersion() {
        return HttpVersion.HTTP_1_1;
    }
}
