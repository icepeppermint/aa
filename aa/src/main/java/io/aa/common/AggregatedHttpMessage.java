package io.aa.common;

public interface AggregatedHttpMessage {

    HttpHeaders headers();

    HttpData content();

    HttpHeaders trailers();

    default String contentUtf8() {
        return content().contentUtf8();
    }
}
