package io.aa.common.server;

import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;

@FunctionalInterface
public interface HttpService {

    HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception;

    default HttpService decorate(DecoratingHttpServiceFunction function) {
        return new DecoratingHttpService(this, function);
    }
}
