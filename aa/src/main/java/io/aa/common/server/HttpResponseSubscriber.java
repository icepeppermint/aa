package io.aa.common.server;

import io.aa.common.AbstractHttpMessageSubscriber;
import io.aa.common.HttpResponse;
import io.netty.channel.ChannelHandlerContext;

public final class HttpResponseSubscriber extends AbstractHttpMessageSubscriber {

    private final boolean keepAlive;

    public HttpResponseSubscriber(HttpResponse res, ChannelHandlerContext ctx, boolean keepAlive) {
        super(res, ctx);
        this.keepAlive = keepAlive;
    }

    public boolean keepAlive() {
        return keepAlive;
    }
}
