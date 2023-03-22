package io.github.paasdash.service.kafka;

import io.github.paasdash.module.kafka.KafkaInstance;
import io.github.paasdash.util.EnvUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class KafkaInstanceService {

    private final Map<String, KafkaInstance> kafkaInstanceMap;

    {
        /*
          Get all kafka instances
          eg: KAFKA_DEFAULT_BOOTSTRAP_SERVERS=localhost:9092
         */
        kafkaInstanceMap = new HashMap<>();
        Map<String, String> envMap = EnvUtil.getByPrefix("KAFKA_");
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int index = key.indexOf("_");
            String name = key.substring(0, index).toLowerCase(Locale.ENGLISH);
            String property = key.substring(index + 1);
            KafkaInstance kafkaInstance = kafkaInstanceMap.get(name);
            if (kafkaInstance == null) {
                kafkaInstanceMap.put(name, new KafkaInstance(name));
            }
            kafkaInstance = kafkaInstanceMap.get(name);
            switch (property) {
                case "BOOTSTRAP_SERVERS" -> kafkaInstance.setBootstrapServers(value);
                default -> {
                }
            }
        }
    }

    public Map<String, KafkaInstance> getKafkaInstanceMap() {
        return kafkaInstanceMap;
    }
}
