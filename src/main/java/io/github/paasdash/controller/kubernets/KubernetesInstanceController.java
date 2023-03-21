package io.github.paasdash.controller.kubernets;

import io.github.paasdash.module.kubernetes.KubernetesInstance;
import io.github.paasdash.service.kubernetes.KubernetesInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kubernetes")
public class KubernetesInstanceController {

    private final KubernetesInstanceService kubernetesInstanceService;

    public KubernetesInstanceController(@Autowired KubernetesInstanceService kubernetesInstanceService) {
        this.kubernetesInstanceService = kubernetesInstanceService;
    }

    @GetMapping("/instances")
    public List<KubernetesInstance> getKubernetesInstances() {
        return kubernetesInstanceService.getKubernetesInstances();
    }
}
