package io.aa.common;

import java.util.Queue;

final class DefaultAggregatedHttpRequest extends AbstractAggregatedHttpMessage
        implements AggregatedHttpRequest {

    DefaultAggregatedHttpRequest(RequestHeaders headers, Queue<HttpData> queue, HttpHeaders trailers) {
        super(headers, queue, trailers);
    }

    @Override
    public RequestHeaders headers() {
        return (RequestHeaders) super.headers();
    }
}
