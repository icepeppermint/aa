package io.aa.common;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public final class DefaultHttpRequest extends AbstractHttpMessage
        implements HttpRequest, AggregationSupport {

    DefaultHttpRequest(RequestHeaders headers, HttpData content, HttpHeaders trailers) {
        super(headers, content, trailers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<AggregatedHttpRequest> aggregate(EventExecutor executor) {
        return (CompletableFuture<AggregatedHttpRequest>) aggregate(this, executor);
    }

    @Override
    public RequestHeaders headers() {
        return (RequestHeaders) super.headers();
    }
}
