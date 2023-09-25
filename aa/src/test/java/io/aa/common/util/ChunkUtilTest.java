package io.aa.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

class ChunkUtilTest {

    @Test
    void toChunk() {
        final ByteBuf byteBuf = Unpooled.copiedBuffer("Hello, World!", StandardCharsets.UTF_8);
        final ByteBuf chunk = ChunkUtil.toChunk(byteBuf);
        assertEquals("13\r\nHello, World!\r\n", chunk.toString(StandardCharsets.UTF_8));
    }

    @Test
    void toChunk_when_empty_byteBuf() {
        final ByteBuf byteBuf = Unpooled.EMPTY_BUFFER;
        final ByteBuf chunk = ChunkUtil.toChunk(byteBuf);
        assertEquals("0\r\n\r\n", chunk.toString(StandardCharsets.UTF_8));
    }

    @Test
    void fromChunk() {
        final ByteBuf chunk = Unpooled.copiedBuffer("13\r\nHello, World!\r\n", StandardCharsets.UTF_8);
        final ByteBuf byteBuf = ChunkUtil.fromChunk(chunk);
        assertEquals("Hello, World!", byteBuf.toString(StandardCharsets.UTF_8));
    }

    @Test
    void fromChunk_when_empty_byteBuf() {
        final ByteBuf chunk = Unpooled.copiedBuffer("0\r\n\r\n", StandardCharsets.UTF_8);
        final ByteBuf byteBuf = ChunkUtil.fromChunk(chunk);
        assertEquals("", byteBuf.toString(StandardCharsets.UTF_8));
    }
}
