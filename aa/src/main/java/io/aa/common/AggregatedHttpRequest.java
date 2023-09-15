package io.aa.common;

public interface AggregatedHttpRequest extends AggregatedHttpMessage {

    @Override
    RequestHeaders headers();

    default HttpMethod method() {
        return headers().method();
    }

    default String path() {
        return headers().path();
    }
}
