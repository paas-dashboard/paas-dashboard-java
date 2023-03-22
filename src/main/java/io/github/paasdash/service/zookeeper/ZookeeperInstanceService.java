package io.github.paasdash.service.zookeeper;

import io.github.paasdash.module.zookeeper.ZookeeperInstance;
import io.github.paasdash.util.EnvUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class ZookeeperInstanceService {

    private final Map<String, ZookeeperInstance> zookeeperInstanceMap;

    {
        /*
          Get all pulsar instances
          eg: ZOOKEEPER_DEFAULT_ADDR=localhost:2181
         */
        zookeeperInstanceMap = new HashMap<>();
        Map<String, String> envMap = EnvUtil.getByPrefix("ZOOKEEPER_");
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int index = key.indexOf("_");
            String name = key.substring(0, index).toLowerCase(Locale.ENGLISH);
            String property = key.substring(index + 1);
            ZookeeperInstance zookeeperInstance = zookeeperInstanceMap.get(name);
            if (zookeeperInstance == null) {
                zookeeperInstanceMap.put(name, new ZookeeperInstance(name));
            }
            zookeeperInstance = zookeeperInstanceMap.get(name);
            switch (property) {
                case "ADDR" -> zookeeperInstance.setAddr(value);
                default -> {
                }
            }
        }
    }

    public Map<String, ZookeeperInstance> getZookeeperInstanceMap() {
        return zookeeperInstanceMap;
    }
}
