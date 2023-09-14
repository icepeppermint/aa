package io.aa.common;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.aa.common.server.HttpResponseSubscriber;
import io.aa.common.util.ChunkUtil;

public abstract class AbstractHttpMessageSubscriber implements Subscriber<HttpObject> {

    private static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;
    private static final String LAST_CHUNK = "0\r\n\r\n";

    private final ChannelHandlerContext ctx;
    private Subscription subscription;
    private boolean needsTrailers;

    protected AbstractHttpMessageSubscriber(ChannelHandlerContext ctx) {
        this.ctx = requireNonNull(ctx, "ctx");
        needsTrailers = true;
    }

    @Override
    public final void onSubscribe(Subscription subscription) {
        this.subscription = requireNonNull(subscription, "subscription");
        this.subscription.request(1);
    }

    @Override
    public final void onNext(HttpObject object) {
        requireNonNull(object, "object");
        final io.netty.handler.codec.http.HttpObject nettyObj;
        if (object instanceof RequestHeaders headers) {
            nettyObj = onRequestHeaders(headers);
        } else if (object instanceof ResponseHeaders headers) {
            nettyObj = onResponseHeaders(headers);
        } else if (object instanceof HttpData httpData) {
            nettyObj = new DefaultHttpContent(ChunkUtil.toChunk(httpData.content()));
        } else {
            assert object instanceof HttpHeaders;
            nettyObj = nettyTrailers((HttpHeaders) object);
        }
        ctx.writeAndFlush(nettyObj).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireExceptionCaught(future.cause());
                ctx.close();
                return;
            }
            if (nettyObj instanceof LastHttpContent && !needsTrailers) {
                future.channel().pipeline().fireExceptionCaught(
                        new IllegalStateException("trailers already sent"));
                ctx.close();
                return;
            }
            subscription.request(1);
        });
    }

    private static io.netty.handler.codec.http.HttpObject onRequestHeaders(RequestHeaders headers) {
        requireNonNull(headers, "headers");
        final var nettyObj = new DefaultHttpRequest(HTTP_VERSION, HttpMethod.valueOf(headers.method().name()),
                                                    headers.path(), headers.asNettyHeaders());
        HttpUtil.setTransferEncodingChunked(nettyObj, true);
        return nettyObj;
    }

    private io.netty.handler.codec.http.HttpObject onResponseHeaders(ResponseHeaders headers) {
        requireNonNull(headers, "headers");
        checkState(this instanceof HttpResponseSubscriber,
                   "Not an instanceof HttpResponseSubscriber (expected instanceof HttpResponseSubscriber)");
        final var nettyObj = new DefaultHttpResponse(HTTP_VERSION,
                                                     HttpResponseStatus.valueOf(headers.statusCode()));
        HttpUtil.setTransferEncodingChunked(nettyObj, true);
        HttpUtil.setKeepAlive(nettyObj, ((HttpResponseSubscriber) this).keepAlive());
        return nettyObj;
    }

    @Override
    public final void onComplete() {
        final Object trailers;
        if (needsTrailers) {
            trailers = nettyTrailers(HttpHeaders.of());
        } else {
            trailers = Unpooled.EMPTY_BUFFER;
        }
        final var future = ctx.writeAndFlush(trailers).addListener((ChannelFutureListener) future0 -> {
            if (!future0.isSuccess()) {
                future0.channel().pipeline().fireExceptionCaught(future0.cause());
                ctx.close();
                return;
            }
            if (trailers instanceof LastHttpContent) {
                if (!needsTrailers) {
                    future0.channel().pipeline().fireExceptionCaught(
                            new IllegalStateException("needsTrailers is false (expected true)"));
                    ctx.close();
                    return;
                }
                needsTrailers = false;
            }
        });
        if (this instanceof HttpResponseSubscriber subscriber && !subscriber.keepAlive()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static DefaultLastHttpContent nettyTrailers(HttpHeaders trailers) {
        requireNonNull(trailers, "trailers");
        return new DefaultLastHttpContent(Unpooled.copiedBuffer(LAST_CHUNK, StandardCharsets.UTF_8),
                                          trailers.asNettyHeaders());
    }

    @Override
    public final void onError(Throwable t) {
        ctx.pipeline().fireExceptionCaught(requireNonNull(t, "t"));
        ctx.close();
    }
}
