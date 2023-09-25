package io.aa.common.client;

import static java.util.Objects.requireNonNull;

import io.aa.common.HttpData;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponseWriter;
import io.aa.common.util.ChunkUtil;
import io.aa.common.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

final class HttpClientHandler extends SimpleChannelInboundHandler<Object> {

    private final ClientRequestContext cctx;
    private final HttpResponseWriter res;

    HttpClientHandler(ClientRequestContext cctx, HttpResponseWriter res) {
        this.cctx = requireNonNull(cctx, "cctx");
        this.res = requireNonNull(res, "res");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final HttpRequest req = cctx.request();
        req.subscribe(new HttpRequestSubscriber(req, ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse nettyRes) {
            res.write(NettyUtil.toResponseHeaders(nettyRes));
        }
        if (msg instanceof HttpContent nettyBody) {
            res.write(HttpData.of(ChunkUtil.fromChunk(nettyBody.content())));
            if (msg instanceof LastHttpContent nettyTrailer) {
                res.write(NettyUtil.toHttpHeaders(nettyTrailer.trailingHeaders()));
                res.close();
            }
        }
    }
}
