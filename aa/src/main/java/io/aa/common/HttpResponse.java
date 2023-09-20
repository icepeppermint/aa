package io.aa.common;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.EventExecutor;

public interface HttpResponse extends HttpMessage {

    static HttpResponseWriter streaming() {
        return new HttpResponseWriter();
    }

    static HttpResponse of(HttpStatus status) {
        return of(status, HttpData.empty());
    }

    static HttpResponse of(int statusCode) {
        return of(statusCode, HttpData.empty());
    }

    static HttpResponse of(HttpData content) {
        return of(ResponseHeaders.of(HttpStatus.OK), content, HttpHeaders.of());
    }

    static HttpResponse of(String content) {
        return of(ResponseHeaders.of(HttpStatus.OK), HttpData.ofUtf8(content), HttpHeaders.of());
    }

    static HttpResponse of(byte[] content) {
        return of(ResponseHeaders.of(HttpStatus.OK), HttpData.of(content), HttpHeaders.of());
    }

    static HttpResponse of(MediaType mediaType, HttpData content) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(MediaType mediaType, String content) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(MediaType mediaType, byte[] content) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK), content, trailers);
    }

    static HttpResponse of(String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK), content, trailers);
    }

    static HttpResponse of(byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK), content, trailers);
    }

    static HttpResponse of(MediaType mediaType, HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, trailers);
    }

    static HttpResponse of(MediaType mediaType, String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, trailers);
    }

    static HttpResponse of(MediaType mediaType, byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(HttpStatus.OK, mediaType), content, trailers);
    }

    static HttpResponse of(HttpStatus status, HttpData content) {
        return of(ResponseHeaders.of(status), content, HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, String content) {
        return of(ResponseHeaders.of(status), HttpData.ofUtf8(content), HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, byte[] content) {
        return of(ResponseHeaders.of(status), HttpData.of(content), HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, HttpData content) {
        return of(ResponseHeaders.of(status, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, String content) {
        return of(ResponseHeaders.of(status, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, byte[] content) {
        return of(ResponseHeaders.of(status, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(HttpStatus status, HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status), content, trailers);
    }

    static HttpResponse of(HttpStatus status, String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status), content, trailers);
    }

    static HttpResponse of(HttpStatus status, byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status), content, trailers);
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status, mediaType), content, trailers);
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status, mediaType), content, trailers);
    }

    static HttpResponse of(HttpStatus status, MediaType mediaType, byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status, mediaType), content, trailers);
    }

    static HttpResponse of(HttpStatus status, HttpHeaders trailers) {
        return of(ResponseHeaders.of(status), HttpData.empty(), trailers);
    }

    static HttpResponse of(int statusCode, HttpData content) {
        return of(ResponseHeaders.of(statusCode), content, HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, String content) {
        return of(ResponseHeaders.of(statusCode), HttpData.ofUtf8(content), HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, byte[] content) {
        return of(ResponseHeaders.of(statusCode), HttpData.of(content), HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, MediaType mediaType, HttpData content) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, MediaType mediaType, String content) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, MediaType mediaType, byte[] content) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, HttpHeaders.of());
    }

    static HttpResponse of(int statusCode, HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode), content, trailers);
    }

    static HttpResponse of(int statusCode, String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode), content, trailers);
    }

    static HttpResponse of(int statusCode, byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode), content, trailers);
    }

    static HttpResponse of(int statusCode, MediaType mediaType, HttpData content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, trailers);
    }

    static HttpResponse of(int statusCode, MediaType mediaType, String content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, trailers);
    }

    static HttpResponse of(int statusCode, MediaType mediaType, byte[] content, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode, mediaType), content, trailers);
    }

    static HttpResponse of(int statusCode, HttpHeaders trailers) {
        return of(ResponseHeaders.of(statusCode), HttpData.empty(), trailers);
    }

    static HttpResponse of(ResponseHeaders headers) {
        return of(headers, HttpData.empty(), HttpHeaders.of());
    }

    static HttpResponse of(ResponseHeaders headers, HttpData content) {
        return of(headers, content, HttpHeaders.of());
    }

    static HttpResponse of(ResponseHeaders headers, HttpData content, HttpHeaders trailers) {
        return new DefaultHttpResponse(headers, content, trailers);
    }

    static HttpResponse of(ResponseHeaders headers, String content, HttpHeaders trailers) {
        return of(headers, HttpData.ofUtf8(content), trailers);
    }

    static HttpResponse of(ResponseHeaders headers, byte[] content, HttpHeaders trailers) {
        return of(headers, HttpData.of(content), trailers);
    }

    static HttpResponse of(ResponseHeaders headers, HttpHeaders trailers) {
        return of(headers, HttpData.empty(), trailers);
    }

    static HttpResponse of(CompletableFuture<? extends HttpResponse> future) {
        return new DeferredHttpResponseWriter(future);
    }

    static HttpResponse of(CompletableFuture<? extends HttpResponse> future, EventExecutor executor) {
        return new DeferredHttpResponseWriter(future, executor);
    }

    static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    default CompletableFuture<AggregatedHttpResponse> aggregate() {
        return aggregate(defaultSubscriberExecutor());
    }

    CompletableFuture<AggregatedHttpResponse> aggregate(EventExecutor executor);
}
