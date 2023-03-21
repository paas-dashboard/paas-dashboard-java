package io.github.paasdash.controller.kubernets;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NodeList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kubernetes/instances/default")
public class KubernetesDefaultNodesController {

    private final CoreV1Api coreV1Api;

    public KubernetesDefaultNodesController(ApiClient apiClient) {
        this.coreV1Api = new CoreV1Api(apiClient);
    }

    @GetMapping("/nodes")
    public V1NodeList getNodes() throws ApiException {
        return coreV1Api.listNode("true", null,
                null, null, null, null, null,
                null, 30, false);
    }
}
