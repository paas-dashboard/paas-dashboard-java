package io.github.paasdash.service.kafka;

import io.github.paasdash.module.kafka.KafkaInstance;
import io.github.paasdash.util.EnvUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class KafkaInstanceService {

    /**
     * Get all kafka instances
     * eg: KAFKA_DEFAULT_BOOTSTRAP_SERVERS=localhost:9092
     * @return
     */
    public Map<String, KafkaInstance> getKafkaInstances() {
        HashMap<String, KafkaInstance> result = new HashMap<>();
        Map<String, String> envMap = EnvUtil.getByPrefix("KAFKA_");
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int index = key.indexOf("_");
            String name = key.substring(0, index).toLowerCase(Locale.ENGLISH);
            String property = key.substring(index + 1);
            KafkaInstance kafkaInstance = result.get(name);
            if (kafkaInstance == null) {
                kafkaInstance = new KafkaInstance();
                kafkaInstance.setName(name);
                result.put(name, kafkaInstance);
            }
            kafkaInstance = result.get(name);
            switch (property) {
                case "BOOTSTRAP_SERVERS":
                    kafkaInstance.setBootstrapServers(value);
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
