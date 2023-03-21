package io.github.paasdash.controller.kafka;

import io.github.paasdash.module.kafka.KafkaInstance;
import io.github.paasdash.service.kafka.KafkaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kafka")
public class KafkaInstanceController {

    private final KafkaInstanceService kafkaInstanceService;

    public KafkaInstanceController(@Autowired KafkaInstanceService kafkaInstanceService) {
        this.kafkaInstanceService = kafkaInstanceService;
    }

    @GetMapping("/instances")
    public List<KafkaInstance> getKafkaInstances() {
        return kafkaInstanceService.getKafkaInstances().values().stream().toList();
    }
}
