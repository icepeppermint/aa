package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.netty.buffer.Unpooled;

class HttpDataTest {

    @Test
    void of_byteBuf() {
        final HttpData httpData = HttpData.of(Unpooled.copiedBuffer("Content", StandardCharsets.UTF_8));
        assertFalse(httpData.isEmpty());
        assertEquals("Content", httpData.contentUtf8());
    }

    @Test
    void of_byteArray() {
        final HttpData httpData = HttpData.of("Content".getBytes(StandardCharsets.UTF_8));
        assertFalse(httpData.isEmpty());
        assertEquals("Content", httpData.contentUtf8());
    }

    @Test
    void ofUtf8() {
        final HttpData httpData = HttpData.ofUtf8("Content");
        assertFalse(httpData.isEmpty());
        assertEquals("Content", httpData.contentUtf8());
    }
}
