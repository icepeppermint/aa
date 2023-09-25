package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Queue;

abstract class AbstractAggregatedHttpMessage implements AggregatedHttpMessage {

    private final HttpHeaders headers;
    private final HttpData content;
    private final HttpHeaders trailers;

    protected AbstractAggregatedHttpMessage(HttpHeaders headers, Queue<HttpData> queue, HttpHeaders trailers) {
        this.headers = requireNonNull(headers, "headers");
        content = aggregated(queue);
        this.trailers = requireNonNull(trailers, "trailers");
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public HttpData content() {
        return content;
    }

    @Override
    public HttpHeaders trailers() {
        return trailers;
    }

    private static HttpData aggregated(Queue<HttpData> queue) {
        requireNonNull(queue, "queue");
        final StringBuilder aggregated = new StringBuilder();
        for (HttpData httpData : queue) {
            aggregated.append(httpData.contentUtf8());
        }
        return HttpData.ofUtf8(aggregated.toString());
    }
}
