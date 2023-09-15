package io.aa.common;

import java.util.Queue;

final class DefaultAggregatedHttpResponse extends AbstractAggregatedHttpMessage
        implements AggregatedHttpResponse {

    DefaultAggregatedHttpResponse(ResponseHeaders headers, Queue<HttpData> queue, HttpHeaders trailers) {
        super(headers, queue, trailers);
    }

    @Override
    public ResponseHeaders headers() {
        return (ResponseHeaders) super.headers();
    }
}
