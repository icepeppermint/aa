package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import io.netty.channel.EventLoop;
import io.aa.common.CommonPools;
import io.aa.common.HttpRequest;
import io.aa.common.util.BlockingTaskExecutor;

public final class ServiceRequestContext {

    private final HttpRequest request;
    private final EventLoop eventLoop;

    ServiceRequestContext(HttpRequest request, EventLoop eventLoop) {
        this.request = requireNonNull(request, "request");
        this.eventLoop = requireNonNull(eventLoop, "eventLoop");
    }

    public HttpRequest request() {
        return request;
    }

    public EventLoop eventLoop() {
        return eventLoop;
    }

    public BlockingTaskExecutor blockingTaskExecutor() {
        return CommonPools.blockingTaskExecutor();
    }
}
