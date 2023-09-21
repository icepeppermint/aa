package io.aa.common;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public interface HttpRequest extends HttpMessage {

    static HttpRequestWriter streaming(RequestHeaders headers) {
        return new HttpRequestWriter(headers);
    }

    static HttpRequestWriter streaming(HttpMethod method, String path) {
        return streaming(RequestHeaders.of(method, path));
    }

    static HttpRequestWriter streaming(HttpMethod method, String path, MediaType mediaType) {
        return streaming(RequestHeaders.of(method, path, mediaType));
    }

    static HttpRequest of(RequestHeaders headers) {
        return of(headers, HttpData.empty());
    }

    static HttpRequest of(RequestHeaders headers, HttpData content) {
        return of(headers, content, HttpHeaders.of());
    }

    static HttpRequest of(RequestHeaders headers, String content) {
        return of(headers, HttpData.ofUtf8(content), HttpHeaders.of());
    }

    static HttpRequest of(RequestHeaders headers, byte[] content) {
        return of(headers, HttpData.of(content), HttpHeaders.of());
    }

    static HttpRequest of(RequestHeaders headers, HttpHeaders trailers) {
        return of(headers, HttpData.empty(), trailers);
    }

    static HttpRequest of(RequestHeaders headers, HttpData content, HttpHeaders trailers) {
        return new DefaultHttpRequest(headers, content, trailers);
    }

    static HttpRequest of(RequestHeaders headers, String content, HttpHeaders trailers) {
        return of(headers, HttpData.ofUtf8(content), trailers);
    }

    static HttpRequest of(RequestHeaders headers, byte[] content, HttpHeaders trailers) {
        return of(headers, HttpData.of(content), trailers);
    }

    static HttpRequest of(HttpMethod method, String path) {
        return of(RequestHeaders.of(method, path), HttpData.empty(), HttpHeaders.of());
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, HttpData content) {
        return of(RequestHeaders.of(method, path, mediaType), content, HttpHeaders.of());
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, String content) {
        return of(RequestHeaders.of(method, path, mediaType), HttpData.ofUtf8(content), HttpHeaders.of());
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, byte[] content) {
        return of(RequestHeaders.of(method, path, mediaType), HttpData.of(content), HttpHeaders.of());
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, HttpData content,
                          HttpHeaders trailers) {
        return of(RequestHeaders.of(method, path, mediaType), content, trailers);
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, String content,
                          HttpHeaders trailers) {
        return of(RequestHeaders.of(method, path, mediaType), content, trailers);
    }

    static HttpRequest of(HttpMethod method, String path, MediaType mediaType, byte[] content,
                          HttpHeaders trailers) {
        return of(RequestHeaders.of(method, path, mediaType), content, trailers);
    }

    static HttpRequest of(HttpMethod method, String path, HttpHeaders trailers) {
        return of(RequestHeaders.of(method, path), trailers);
    }

    static HttpRequestBuilder builder() {
        return new HttpRequestBuilder();
    }

    RequestHeaders headers();

    default CompletableFuture<AggregatedHttpRequest> aggregate() {
        return aggregate(defaultSubscriberExecutor());
    }

    CompletableFuture<AggregatedHttpRequest> aggregate(EventExecutor executor);
}
