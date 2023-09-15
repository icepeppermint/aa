package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

public interface StreamWriter<T> extends StreamMessage<T> {

    void write(T object);

    default void write(Supplier<? extends T> object) {
        requireNonNull(object, "object");
        write(requireNonNull(object.get(), "object.get()"));
    }

    void close();

    void close(Throwable e);

    default void close(Supplier<? extends Throwable> e) {
        requireNonNull(e, "e");
        close(requireNonNull(e.get(), "e.get()"));
    }
}
