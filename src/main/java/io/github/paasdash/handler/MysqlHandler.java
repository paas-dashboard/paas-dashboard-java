package io.github.paasdash.handler;

import io.github.paasdash.PaasConfig;
import io.vertx.ext.web.RoutingContext;

public class MysqlHandler {
    private final PaasConfig config;

    public MysqlHandler(PaasConfig config) {
        this.config = config;
    }

    public void getDatabases(RoutingContext context) {
    }
}
