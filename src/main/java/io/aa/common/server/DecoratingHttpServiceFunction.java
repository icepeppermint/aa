package io.aa.common.server;

import io.aa.common.HttpRequest;
import io.aa.common.HttpResponse;

@FunctionalInterface
public interface DecoratingHttpServiceFunction {

    HttpResponse serve(HttpService delegate, ServiceRequestContext ctx, HttpRequest req) throws Exception;
}
