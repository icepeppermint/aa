package io.aa.common.client;

import io.netty.channel.ChannelHandlerContext;
import io.aa.common.AbstractHttpMessageSubscriber;

public final class HttpRequestSubscriber extends AbstractHttpMessageSubscriber {

    public HttpRequestSubscriber(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
