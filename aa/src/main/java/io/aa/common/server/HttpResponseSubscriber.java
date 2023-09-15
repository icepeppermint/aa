package io.aa.common.server;

import io.netty.channel.ChannelHandlerContext;
import io.aa.common.AbstractHttpMessageSubscriber;

public final class HttpResponseSubscriber extends AbstractHttpMessageSubscriber {

    private final boolean keepAlive;

    public HttpResponseSubscriber(ChannelHandlerContext ctx, boolean keepAlive) {
        super(ctx);
        this.keepAlive = keepAlive;
    }

    public boolean keepAlive() {
        return keepAlive;
    }
}
