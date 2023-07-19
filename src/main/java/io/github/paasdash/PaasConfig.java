package io.github.paasdash;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PaasConfig {
    private Map<String, PaasEnvConfig> env;

    public PaasConfig() {
    }
}
