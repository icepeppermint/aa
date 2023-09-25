package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class HttpData implements HttpObject {

    private static final HttpData EMPTY = new HttpData(Unpooled.EMPTY_BUFFER.asReadOnly());

    private final ByteBuf content;

    private HttpData(ByteBuf content) {
        requireNonNull(content, "content");
        this.content = Unpooled.copiedBuffer(content).asReadOnly();
    }

    public static HttpData of(ByteBuf content) {
        return new HttpData(Unpooled.copiedBuffer(requireNonNull(content, "content")));
    }

    public static HttpData of(byte[] content) {
        return new HttpData(Unpooled.copiedBuffer(requireNonNull(content, "content")));
    }

    public static HttpData ofUtf8(String content) {
        return new HttpData(Unpooled.copiedBuffer(requireNonNull(content, "content"), StandardCharsets.UTF_8));
    }

    public static HttpData empty() {
        return EMPTY;
    }

    public ByteBuf content() {
        return content;
    }

    public String contentUtf8() {
        return content.toString(StandardCharsets.UTF_8);
    }

    public boolean isEmpty() {
        return !content.isReadable();
    }
}
