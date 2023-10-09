package io.aa.common.client;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import io.aa.common.HttpData;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponseWriter;
import io.aa.common.ResponseHeaders;
import io.aa.common.util.ChunkUtil;
import io.aa.common.util.HttpUtil;
import io.aa.common.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

final class HttpClientHandler extends SimpleChannelInboundHandler<Object> {

    private final ClientRequestContext cctx;
    private final HttpResponseWriter res;
    @Nullable
    private ResponseHeaders headers;

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
            res.write(headers = NettyUtil.toResponseHeaders(nettyRes));
        }
        if (msg instanceof HttpContent nettyBody) {
            ByteBuf content = nettyBody.content();
            if (HttpUtil.isTransferEncodingChunked(headers)) {
                content = ChunkUtil.fromChunk(content);
            }
            res.write(HttpData.of(content));
            if (msg instanceof LastHttpContent nettyTrailer) {
                res.write(NettyUtil.toHttpHeaders(nettyTrailer.trailingHeaders()));
                res.close();
                headers = null;
            }
        }
    }
}
