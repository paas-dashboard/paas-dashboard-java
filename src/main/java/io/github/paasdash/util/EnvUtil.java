package io.github.paasdash.util;

import java.util.HashMap;
import java.util.Map;

public class EnvUtil {
    private static final Map<String, String> envMap = System.getenv();

    public static Map<String, String> getByPrefix(String prefix) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                result.put(entry.getKey().substring(prefix.length()), entry.getValue());
            }
        }
        return result;
    }
}
