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
                              String name1, String value1, String name2, String value2) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5, String name6, String value6) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5).add(name6, value6)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5, String name6, String value6,
                              String name7, String value7) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5).add(name6, value6)
                              .add(name7, value7)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5, String name6, String value6,
                              String name7, String value7, String name8, String value8) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5).add(name6, value6)
                              .add(name7, value7).add(name8, value8)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5, String name6, String value6,
                              String name7, String value7, String name8, String value8,
                              String name9, String value9) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5).add(name6, value6)
                              .add(name7, value7).add(name8, value8)
                              .add(name9, value9)
                              .build();
    }

    static ResponseHeaders of(HttpStatus status,
                              String name1, String value1, String name2, String value2,
                              String name3, String value3, String name4, String value4,
                              String name5, String value5, String name6, String value6,
                              String name7, String value7, String name8, String value8,
                              String name9, String value9, String name10, String value10) {
        return builder(status).add(name1, value1).add(name2, value2)
                              .add(name3, value3).add(name4, value4)
                              .add(name5, value5).add(name6, value6)
                              .add(name7, value7).add(name8, value8)
                              .add(name9, value9).add(name10, value10)
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
