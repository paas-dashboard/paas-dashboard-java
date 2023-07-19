package io.github.paasdash;

import io.github.paasdash.config.IgniteInstanceConfig;
import io.github.paasdash.config.MysqlInstanceConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PaasEnvConfig {

    private String host;

    private int port;

    private Map<String, IgniteInstanceConfig> ignite;

    private Map<String, MysqlInstanceConfig> mysql;

    public PaasEnvConfig() {
    }
}
