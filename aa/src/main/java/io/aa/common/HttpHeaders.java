package io.aa.common;

import java.util.Collection;
import java.util.Map.Entry;

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
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5, String name6, String value6) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5).add(name6, value6)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5, String name6, String value6,
                          String name7, String value7) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5).add(name6, value6)
                        .add(name7, value7)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5, String name6, String value6,
                          String name7, String value7, String name8, String value8) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5).add(name6, value6)
                        .add(name7, value7).add(name8, value8)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5, String name6, String value6,
                          String name7, String value7, String name8, String value8,
                          String name9, String value9) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5).add(name6, value6)
                        .add(name7, value7).add(name8, value8)
                        .add(name9, value9)
                        .build();
    }

    static HttpHeaders of(String name1, String value1, String name2, String value2,
                          String name3, String value3, String name4, String value4,
                          String name5, String value5, String name6, String value6,
                          String name7, String value7, String name8, String value8,
                          String name9, String value9, String name10, String value10) {
        return builder().add(name1, value1).add(name2, value2)
                        .add(name3, value3).add(name4, value4)
                        .add(name5, value5).add(name6, value6)
                        .add(name7, value7).add(name8, value8)
                        .add(name9, value9).add(name10, value10)
                        .build();
    }

    static HttpHeaders of(Collection<Entry<String, String>> entries) {
        return builder().add(entries).build();
    }

    static HttpHeadersBuilder builder() {
        return new DefaultHttpHeadersBuilder();
    }
}
