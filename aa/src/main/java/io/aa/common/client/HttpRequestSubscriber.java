package io.aa.common.client;

import io.aa.common.AbstractHttpMessageSubscriber;
import io.aa.common.HttpRequest;
import io.netty.channel.ChannelHandlerContext;

public final class HttpRequestSubscriber extends AbstractHttpMessageSubscriber {

    public HttpRequestSubscriber(HttpRequest req, ChannelHandlerContext ctx) {
        super(req, ctx);
    }
}
