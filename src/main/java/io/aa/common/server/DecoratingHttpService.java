package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;

public class DecoratingHttpService implements HttpService {

    private final HttpService delegate;
    private final DecoratingHttpServiceFunction function;

    public DecoratingHttpService(HttpService delegate, DecoratingHttpServiceFunction function) {
        this.delegate = requireNonNull(delegate, "delegate");
        this.function = requireNonNull(function, "function");
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return function.serve(delegate, ctx, req);
    }
}
