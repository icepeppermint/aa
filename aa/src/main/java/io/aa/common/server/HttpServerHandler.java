package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import io.aa.common.HttpData;
import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequestWriter;
import io.aa.common.RequestHeaders;
import io.aa.common.util.ChunkUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;

final class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private final Route route;
    @Nullable
    private HttpRequestWriter req;

    HttpServerHandler(Route route) {
        this.route = requireNonNull(route, "route");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest nettyReq) {
            req = io.aa.common.HttpRequest.streaming(
                    RequestHeaders.of(HttpMethod.valueOf(nettyReq.method().name()), nettyReq.uri())
                                  .putAll(nettyReq.headers()));
            final var reqCtx = new ServiceRequestContext(req, ctx.channel().eventLoop());
            final var service = route.get(req.headers());
            if (service == null) {
                ctx.pipeline().fireExceptionCaught(
                        new IllegalStateException("service is null (expected not null)"));
                ctx.close();
                return;
            }
            final var res = service.serve(reqCtx, req);
            res.subscribe(new HttpResponseSubscriber(res, ctx, HttpUtil.isKeepAlive(nettyReq)),
                          reqCtx.eventLoop());
        }
        if (msg instanceof HttpContent nettyBody) {
            assert req != null;
            var content = nettyBody.content();
            if (io.aa.common.util.HttpUtil.isTransferEncodingChunked(req.headers())) {
                content = ChunkUtil.fromChunk(content);
            }
            req.write(HttpData.of(content));
            if (msg instanceof LastHttpContent nettyTrailer) {
                req.write(HttpHeaders.of(nettyTrailer.trailingHeaders()));
                req.close();
                req = null;
            }
        }
    }
}
