package io.github.paasdash.service.pulsar;

import io.github.paasdash.module.pulsar.PulsarInstance;
import io.github.paasdash.util.EnvUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class PulsarInstanceService {

    private final Map<String, PulsarInstance> pulsarInstanceMap;

    {
        /*
          Get all pulsar instances
          eg: PULSAR_DEFAULT_HOST=localhost
              PULSAR_DEFAULT_WEB_PORT=8080
              PULSAR_DEFAULT_TCP_PORT=6650
         */
        pulsarInstanceMap = new HashMap<>();
        Map<String, String> envMap = EnvUtil.getByPrefix("PULSAR_");
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int index = key.indexOf("_");
            String name = key.substring(0, index).toLowerCase(Locale.ENGLISH);
            String property = key.substring(index + 1);
            PulsarInstance pulsarInstance = pulsarInstanceMap.get(name);
            if (pulsarInstance == null) {
                pulsarInstanceMap.put(name, new PulsarInstance(name));
            }
            pulsarInstance = pulsarInstanceMap.get(name);
            switch (property) {
                case "HOST" -> pulsarInstance.setHost(value);
                case "WEB_PORT" -> pulsarInstance.setWebPort(Integer.parseInt(value));
                case "TCP_PORT" -> pulsarInstance.setTcpPort(Integer.parseInt(value));
                default -> {
                }
            }
        }
    }

    public Map<String, PulsarInstance> getPulsarInstanceMap() {
        return pulsarInstanceMap;
    }
}
