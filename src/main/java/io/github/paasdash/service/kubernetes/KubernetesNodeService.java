package io.github.paasdash.service.kubernetes;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KubernetesNodeService {

    private final CoreV1Api k8sClient;

    public KubernetesNodeService(@Autowired ApiClient apiClient) {
        this.k8sClient = new CoreV1Api(apiClient);
    }

    public List<V1Node> getNodes() throws Exception {
        return k8sClient.listNode("true", null,
                null, null, null, null, null,
                null, 30, false).getItems();
    }

    public void patch(String nodeName, V1Patch v1Patch) throws ApiException {
        k8sClient.patchNode(nodeName, v1Patch, "true",
                null, null, null, null);
    }

}
