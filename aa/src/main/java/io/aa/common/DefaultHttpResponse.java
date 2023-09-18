package io.aa.common;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public final class DefaultHttpResponse extends AbstractHttpMessage
        implements HttpResponse, AggregationSupport {

    DefaultHttpResponse(ResponseHeaders headers, HttpData content, HttpHeaders trailers) {
        super(headers, content, trailers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<AggregatedHttpResponse> aggregate(EventExecutor executor) {
        return (CompletableFuture<AggregatedHttpResponse>) aggregate(this, executor);
    }

    @Override
    public ResponseHeaders headers() {
        return (ResponseHeaders) super.headers();
    }
}
