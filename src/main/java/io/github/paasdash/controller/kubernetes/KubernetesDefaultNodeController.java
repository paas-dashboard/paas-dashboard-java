package io.github.paasdash.controller.kubernetes;

import io.github.paasdash.service.kubernetes.KubernetesNodeService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/kubernetes/instances/default")
public class KubernetesDefaultNodeController {

    private final KubernetesNodeService kubernetesNodeService;

    public KubernetesDefaultNodeController(@Autowired KubernetesNodeService kubernetesNodeService) {
        this.kubernetesNodeService = kubernetesNodeService;
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<V1Node>> getNodes() throws Exception {
        return new ResponseEntity<>(kubernetesNodeService.getNodes(), HttpStatus.OK);
    }

    @PostMapping("/nodes/{nodeName}/patch")
    public ResponseEntity<Void> patch(@PathVariable String nodeName,
                                      @RequestBody V1Patch v1Patch) throws ApiException {
        kubernetesNodeService.patch(nodeName, v1Patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
