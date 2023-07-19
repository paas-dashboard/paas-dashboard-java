package io.github.paasdash.handler;

import io.github.paasdash.PaasConfig;
import io.vertx.ext.web.RoutingContext;

public class EnvHandler {
    private final PaasConfig config;

    public EnvHandler(PaasConfig config) {
        this.config = config;
    }

    public void getEnvList(RoutingContext context) {
    }
}
