package io.github.paasdash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.paasdash.handler.EnvHandler;
import io.github.paasdash.handler.IgniteHandler;
import io.github.paasdash.handler.MysqlHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Configurator.setRootLevel(Level.INFO);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        String homePath = System.getProperty("user.home");
        String configFilePath = Paths.get(homePath, ".pd", "config.yaml").toString();
        PaasConfig config;
        try {
            config = mapper.readValue(new File(configFilePath), PaasConfig.class);
        } catch (IOException e) {
            log.error("Failed to read config file: " + configFilePath, e);
            config = new PaasConfig();
        }
        EnvHandler envHandler = new EnvHandler(config);
        IgniteHandler igniteHandler = new IgniteHandler(config);
        MysqlHandler mysqlHandler = new MysqlHandler(config);
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            log.info("Hello, paasdashboard");
            routingContext.response().end("Hello World!");
        });
        router.route("/static/*").handler(StaticHandler.create("static"));
        router.get("/api/env").handler(envHandler::getEnvList);
        router.get("/api/env/:env/ignite/:instance/schemas").handler(igniteHandler::getSchemas);
        router.get("/api/env/:env/mysql/:instance/databases").handler(mysqlHandler::getDatabases);
        vertx.createHttpServer().requestHandler(router).listen(11111);
    }
}
