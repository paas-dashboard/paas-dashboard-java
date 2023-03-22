package io.github.paasdash.controller.pulsar;

import io.github.paasdash.module.pulsar.PulsarInstance;
import io.github.paasdash.service.pulsar.PulsarInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pulsar")
public class PulsarInstanceController {

    private final PulsarInstanceService pulsarInstanceService;

    public PulsarInstanceController(@Autowired PulsarInstanceService pulsarInstanceService) {
        this.pulsarInstanceService = pulsarInstanceService;
    }

    @GetMapping("/instances")
    public List<PulsarInstance> getPulsarInstanceMap() {
        return pulsarInstanceService.getPulsarInstanceMap().values().stream().toList();
    }
}
