package io.aa.common;

import static java.util.Objects.requireNonNull;

public interface ResponseHeaders extends HttpHeaders {

    static ResponseHeaders of(HttpStatus status) {
        return builder(status).build();
    }

    static ResponseHeaders of(int statusCode) {
        return builder(statusCode).build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1) {
        return builder(status).add(name1, value1)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1,
                              String name2, String value2) {
        return builder(status).add(name1, value1)
                              .add(name2, value2)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1,
                              String name2, String value2,
                              String name3, String value3) {
        return builder(status).add(name1, value1)
                              .add(name2, value2)
                              .add(name3, value3)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1,
                              String name2, String value2,
                              String name3, String value3,
                              String name4, String value4) {
        return builder(status).add(name1, value1)
                              .add(name2, value2)
                              .add(name3, value3)
                              .add(name4, value4)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1,
                              String name2, String value2,
                              String name3, String value3,
                              String name4, String value4,
                              String name5, String value5) {
        return builder(status).add(name1, value1)
                              .add(name2, value2)
                              .add(name3, value3)
                              .add(name4, value4)
                              .add(name5, value5)
                              .build();
    }

    static ResponseHeadersBuilder builder() {
        return new DefaultResponseHeadersBuilder();
    }

    static ResponseHeadersBuilder builder(HttpStatus status) {
        requireNonNull(status, "status");
        return builder().status(status);
    }

    static ResponseHeadersBuilder builder(int statusCode) {
        return builder().statusCode(statusCode);
    }

    HttpStatus status();

    int statusCode();
}
