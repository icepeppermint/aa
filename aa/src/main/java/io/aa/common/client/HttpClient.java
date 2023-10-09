package io.aa.common.client;

import static java.util.Objects.requireNonNull;

import java.net.URI;

import io.aa.common.HttpData;
import io.aa.common.HttpHeaders;
import io.aa.common.HttpMethod;
import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;
import io.aa.common.HttpResponseWriter;
import io.aa.common.RequestHeaders;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;

public final class HttpClient {

    private final URI uri;

    private HttpClient(URI uri) {
        this.uri = requireNonNull(uri, "uri");
    }

    public static HttpClient of(URI uri) {
        return new HttpClient(uri);
    }

    public static HttpClient of(String uri) {
        return of(URI.create(requireNonNull(uri, "uri")));
    }

    public HttpResponse execute(HttpRequest req) throws InterruptedException {
        requireNonNull(req, "req");
        final HttpResponseWriter streaming = HttpResponse.streaming();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1))
                 .channel(NioSocketChannel.class)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         final ChannelPipeline pipeline = ch.pipeline();
                         pipeline.addLast(new HttpClientCodec());
                         pipeline.addLast(new HttpClientHandler(
                                 new ClientRequestContext(uri, req, ch.eventLoop()), streaming));
                     }
                 });
        bootstrap.connect(uri.getHost(), uri.getPort()).sync();
        return streaming;
    }

    public HttpResponse execute(RequestHeaders headers) throws InterruptedException {
        return execute(headers, HttpData.empty(), HttpHeaders.of());
    }

    public HttpResponse execute(RequestHeaders headers, HttpData content) throws InterruptedException {
        return execute(headers, content, HttpHeaders.of());
    }

    public HttpResponse execute(RequestHeaders headers, String content) throws InterruptedException {
        return execute(headers, HttpData.ofUtf8(content), HttpHeaders.of());
    }

    public HttpResponse execute(RequestHeaders headers, byte[] content) throws InterruptedException {
        return execute(headers, HttpData.of(content), HttpHeaders.of());
    }

    public HttpResponse execute(RequestHeaders headers, HttpData content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(HttpRequest.of(headers, content, trailers));
    }

    public HttpResponse execute(RequestHeaders headers, String content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(HttpRequest.of(headers, content, trailers));
    }

    public HttpResponse execute(RequestHeaders headers, byte[] content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(HttpRequest.of(headers, content, trailers));
    }

    public HttpResponse execute(HttpMethod method, String path) throws InterruptedException {
        return execute(method, path, HttpData.empty(), HttpHeaders.of());
    }

    public HttpResponse execute(HttpMethod method, String path, HttpData content) throws InterruptedException {
        return execute(method, path, content, HttpHeaders.of());
    }

    public HttpResponse execute(HttpMethod method, String path, String content) throws InterruptedException {
        return execute(method, path, HttpData.ofUtf8(content), HttpHeaders.of());
    }

    public HttpResponse execute(HttpMethod method, String path, byte[] content) throws InterruptedException {
        return execute(method, path, HttpData.of(content), HttpHeaders.of());
    }

    public HttpResponse execute(HttpMethod method, String path, HttpData content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(RequestHeaders.of(method, path), content, trailers);
    }

    public HttpResponse execute(HttpMethod method, String path, String content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(RequestHeaders.of(method, path), content, trailers);
    }

    public HttpResponse execute(HttpMethod method, String path, byte[] content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(RequestHeaders.of(method, path), content, trailers);
    }

    public HttpResponse options(String path) throws InterruptedException {
        return execute(RequestHeaders.of(HttpMethod.OPTIONS, path));
    }

    public HttpResponse get(String path) throws InterruptedException {
        return execute(HttpMethod.GET, path);
    }

    public HttpResponse get(String path, HttpData content) throws InterruptedException {
        return execute(HttpMethod.GET, path, content);
    }

    public HttpResponse get(String path, String content) throws InterruptedException {
        return execute(HttpMethod.GET, path, content);
    }

    public HttpResponse get(String path, byte[] content) throws InterruptedException {
        return execute(HttpMethod.GET, path, content);
    }

    public HttpResponse get(String path, HttpData content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.GET, path, content, trailers);
    }

    public HttpResponse get(String path, String content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.GET, path, content, trailers);
    }

    public HttpResponse get(String path, byte[] content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.GET, path, content, trailers);
    }

    public HttpResponse head(String path) throws InterruptedException {
        return execute(RequestHeaders.of(HttpMethod.HEAD, path));
    }

    public HttpResponse post(String path) throws InterruptedException {
        return execute(HttpMethod.POST, path);
    }

    public HttpResponse post(String path, HttpData content) throws InterruptedException {
        return execute(HttpMethod.POST, path, content);
    }

    public HttpResponse post(String path, String content) throws InterruptedException {
        return execute(HttpMethod.POST, path, content);
    }

    public HttpResponse post(String path, byte[] content) throws InterruptedException {
        return execute(HttpMethod.POST, path, content);
    }

    public HttpResponse post(String path, HttpData content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.POST, path, content, trailers);
    }

    public HttpResponse post(String path, String content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.POST, path, content, trailers);
    }

    public HttpResponse post(String path, byte[] content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.POST, path, content, trailers);
    }

    public HttpResponse put(String path) throws InterruptedException {
        return execute(HttpMethod.PUT, path);
    }

    public HttpResponse put(String path, HttpData content) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content);
    }

    public HttpResponse put(String path, String content) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content);
    }

    public HttpResponse put(String path, byte[] content) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content);
    }

    public HttpResponse put(String path, HttpData content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content, trailers);
    }

    public HttpResponse put(String path, String content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content, trailers);
    }

    public HttpResponse put(String path, byte[] content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PUT, path, content, trailers);
    }

    public HttpResponse patch(String path) throws InterruptedException {
        return execute(HttpMethod.PATCH, path);
    }

    public HttpResponse patch(String path, HttpData content) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content);
    }

    public HttpResponse patch(String path, String content) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content);
    }

    public HttpResponse patch(String path, byte[] content) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content);
    }

    public HttpResponse patch(String path, HttpData content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content, trailers);
    }

    public HttpResponse patch(String path, String content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content, trailers);
    }

    public HttpResponse patch(String path, byte[] content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.PATCH, path, content, trailers);
    }

    public HttpResponse delete(String path) throws InterruptedException {
        return execute(HttpMethod.DELETE, path);
    }

    public HttpResponse delete(String path, HttpData content) throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content);
    }

    public HttpResponse delete(String path, String content) throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content);
    }

    public HttpResponse delete(String path, byte[] content) throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content);
    }

    public HttpResponse delete(String path, HttpData content, HttpHeaders trailers)
            throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content, trailers);
    }

    public HttpResponse delete(String path, String content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content, trailers);
    }

    public HttpResponse delete(String path, byte[] content, HttpHeaders trailers) throws InterruptedException {
        return execute(HttpMethod.DELETE, path, content, trailers);
    }

    public HttpResponse trace(String path) throws InterruptedException {
        return execute(RequestHeaders.of(HttpMethod.TRACE, path));
    }
}
