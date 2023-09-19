package io.aa.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.netty.buffer.Unpooled;

class ChunkUtilTest {

    @Test
    void toChunk() {
        final var byteBuf = Unpooled.copiedBuffer("Hello, World!", StandardCharsets.UTF_8);
        final var chunk = ChunkUtil.toChunk(byteBuf);
        assertEquals("13\r\nHello, World!\r\n", chunk.toString(StandardCharsets.UTF_8));
    }

    @Test
    void toChunk_when_empty_byteBuf() {
        final var byteBuf = Unpooled.EMPTY_BUFFER;
        final var chunk = ChunkUtil.toChunk(byteBuf);
        assertEquals("0\r\n\r\n", chunk.toString(StandardCharsets.UTF_8));
    }

    @Test
    void fromChunk() {
        final var chunk = Unpooled.copiedBuffer("13\r\nHello, World!\r\n", StandardCharsets.UTF_8);
        final var byteBuf = ChunkUtil.fromChunk(chunk);
        assertEquals("Hello, World!", byteBuf.toString(StandardCharsets.UTF_8));
    }

    @Test
    void fromChunk_when_empty_byteBuf() {
        final var chunk = Unpooled.copiedBuffer("0\r\n\r\n", StandardCharsets.UTF_8);
        final var byteBuf = ChunkUtil.fromChunk(chunk);
        assertEquals("", byteBuf.toString(StandardCharsets.UTF_8));
    }
}
