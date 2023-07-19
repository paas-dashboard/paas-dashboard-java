package io.github.paasdash.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IgniteInstanceConfig {
    private String igniteAddress;

    private String username;

    private String password;

    public IgniteInstanceConfig() {
    }
}
