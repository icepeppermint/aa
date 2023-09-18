package io.aa.common;

import static java.util.Objects.requireNonNull;

abstract class AbstractHttpMessage extends AbstractStreamWriter {

    private final HttpHeaders headers;
    private final HttpData content;
    private final HttpHeaders trailers;

    protected AbstractHttpMessage(HttpHeaders headers, HttpData content, HttpHeaders trailers) {
        this.headers = requireNonNull(headers, "headers");
        this.content = requireNonNull(content, "content");
        this.trailers = requireNonNull(trailers, "trailers");
        writeAndClose();
    }

    private void writeAndClose() {
        write(headers);
        write(content);
        write(trailers);
        close();
    }

    public HttpHeaders headers() {
        return headers;
    }

    public final HttpData content() {
        return content;
    }

    public final HttpHeaders trailers() {
        return trailers;
    }
}
