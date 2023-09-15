package io.aa.common;

public interface AggregatedHttpResponse extends AggregatedHttpMessage {

    @Override
    ResponseHeaders headers();

    default HttpStatus status() {
        return headers().status();
    }

    default int statusCode() {
        return headers().statusCode();
    }
}
