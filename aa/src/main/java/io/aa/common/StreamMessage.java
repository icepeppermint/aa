package io.aa.common;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.netty.util.concurrent.EventExecutor;

public interface StreamMessage<T> extends Publisher<T> {

    @Override
    default void subscribe(Subscriber<? super T> subscriber) {
        subscribe(subscriber, defaultSubscriberExecutor());
    }

    void subscribe(Subscriber<? super T> subscriber, EventExecutor executor);

    EventExecutor defaultSubscriberExecutor();
}
