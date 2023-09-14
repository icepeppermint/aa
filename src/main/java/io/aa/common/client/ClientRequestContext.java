package io.aa.common.client;

import static java.util.Objects.requireNonNull;

import java.net.URI;

import io.netty.channel.EventLoop;
import io.aa.common.HttpRequest;

public final class ClientRequestContext {

    private final URI uri;
    private final HttpRequest request;
    private final EventLoop eventLoop;

    ClientRequestContext(URI uri, HttpRequest request, EventLoop eventLoop) {
        this.uri = requireNonNull(uri, "uri");
        this.request = requireNonNull(request, "request");
        this.eventLoop = requireNonNull(eventLoop, "eventLoop");
    }

    public URI uri() {
        return uri;
    }

    public HttpRequest request() {
        return request;
    }

    public EventLoop eventLoop() {
        return eventLoop;
    }
}
