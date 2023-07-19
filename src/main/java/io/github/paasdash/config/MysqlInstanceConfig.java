package io.github.paasdash.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MysqlInstanceConfig {
    private String username;

    private String password;

    public MysqlInstanceConfig() {
    }
}
