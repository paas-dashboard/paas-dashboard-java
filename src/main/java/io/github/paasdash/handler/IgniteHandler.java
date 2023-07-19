package io.github.paasdash.handler;

import io.github.paasdash.PaasConfig;
import io.vertx.ext.web.RoutingContext;

public class IgniteHandler {
    private final PaasConfig config;

    public IgniteHandler(PaasConfig config) {
        this.config = config;
    }

    public void getSchemas(RoutingContext context) {
    }
}
