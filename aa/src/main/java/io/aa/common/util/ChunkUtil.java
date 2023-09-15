package io.aa.common.util;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;

import com.google.common.base.Splitter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class ChunkUtil {

    private static final String SEPARATOR = "\r\n";
    private static final Splitter SPLITTER = Splitter.on(SEPARATOR).omitEmptyStrings();

    public static ByteBuf toChunk(ByteBuf byteBuf) {
        requireNonNull(byteBuf, "byteBuf");
        final var chunk = byteBuf.readableBytes() + SEPARATOR + toStringUtf8(byteBuf) + SEPARATOR;
        return Unpooled.copiedBuffer(chunk, StandardCharsets.UTF_8);
    }

    public static ByteBuf fromChunk(ByteBuf byteBuf) {
        requireNonNull(byteBuf, "byteBuf");
        final var split = SPLITTER.splitToList(toStringUtf8(byteBuf));
        if (split.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        final var length = Integer.parseInt(split.get(0));
        if (length <= 0) {
            checkState(split.size() == 1, "split.size() != 1 (expected == 1)");
            return Unpooled.EMPTY_BUFFER;
        }
        checkState(split.size() == 2, "split.size() != 2 (expected == 2)");
        return Unpooled.copiedBuffer(split.get(1), StandardCharsets.UTF_8);
    }

    private static String toStringUtf8(ByteBuf byteBuf) {
        requireNonNull(byteBuf, "byteBuf");
        return byteBuf.toString(StandardCharsets.UTF_8);
    }

    private ChunkUtil() {}
}
