package io.aa.common;

import com.google.common.collect.Multimap;

public interface HttpHeaders extends HttpObject, HttpHeaderGetters {

    static HttpHeaders of() {
        return builder().build();
    }

    static HttpHeaders of(String name1, String value1) {
        return builder().add(name1, value1)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2) {
        return builder().add(name1, value1)
                        .add(name2, value2)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3) {
        return builder().add(name1, value1)
                        .add(name2, value2)
                        .add(name3, value3)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4) {
        return builder().add(name1, value1)
                        .add(name2, value2)
                        .add(name3, value3)
                        .add(name4, value4)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5) {
        return builder().add(name1, value1)
                        .add(name2, value2)
                        .add(name3, value3)
                        .add(name4, value4)
                        .add(name5, value5)
                        .build();
    }

    static HttpHeaders of(Multimap<String, String> multimap) {
        return builder().addAll(multimap).build();
    }

    static HttpHeadersBuilder builder() {
        return new DefaultHttpHeadersBuilder();
    }
}
