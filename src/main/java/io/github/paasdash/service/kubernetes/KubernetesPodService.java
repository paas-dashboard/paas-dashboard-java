package io.github.paasdash.service.kubernetes;

import com.google.common.io.CharStreams;
import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class KubernetesPodService {

    private final CoreV1Api coreV1Api;

    private static final Exec exec = new Exec();

    public KubernetesPodService(@Autowired ApiClient apiClient) {
        this.coreV1Api = new CoreV1Api(apiClient);
    }

    public void createPod(String namespace, V1Pod v1Pod) throws Exception {
        coreV1Api.createNamespacedPod(namespace, v1Pod, "true", null, null, null);
    }

    public void deletePod(String namespace, String podName) throws ApiException {
        coreV1Api.deleteNamespacedPod(podName, namespace, "true", null,
                null, null, null, null);
    }

    public List<V1Pod> getNamespacePods(String namespace) throws ApiException {
        return coreV1Api.listNamespacedPod(namespace, "true", null, null,
                null, null, null, null, null,
                null, null).getItems();
    }

    public String execCommand(String namespace, String podName, String commandStr)
            throws IOException, ApiException {
        String[] command = commandStr.split(" ");
        if (command.length == 0) {
            log.warn("empty command!");
            return "";
        }
        final Process process = exec.exec(namespace, podName, command, true, true);
        return CharStreams.toString(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
    }
}
