package io.github.paasdash.service.bookkeeper;

import io.github.paasdash.module.bookkeeper.BookkeeperInstance;
import io.github.paasdash.util.EnvUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class BookkeeperInstanceService {

    private final Map<String, BookkeeperInstance> bookkeeperInstanceMap;

    {
        /*
          Get all pulsar instances
          eg: BOOKKEEPER_DEFAULT_ZK_INSTANCE=default
         */
        bookkeeperInstanceMap = new HashMap<>();
        Map<String, String> envMap = EnvUtil.getByPrefix("BOOKKEEPER_");
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int index = key.indexOf("_");
            String name = key.substring(0, index).toLowerCase(Locale.ENGLISH);
            String property = key.substring(index + 1);
            BookkeeperInstance bookkeeperInstance = bookkeeperInstanceMap.get(name);
            if (bookkeeperInstance == null) {
                bookkeeperInstanceMap.put(name, new BookkeeperInstance(name));
            }
            bookkeeperInstance = bookkeeperInstanceMap.get(name);
            switch (property) {
                case "ZK_INSTANCE" -> bookkeeperInstance.setZkInstanceName(value);
                default -> {
                }
            }
        }
    }

    public Map<String, BookkeeperInstance> getBookkeeperInstanceMap() {
        return bookkeeperInstanceMap;
    }
}
