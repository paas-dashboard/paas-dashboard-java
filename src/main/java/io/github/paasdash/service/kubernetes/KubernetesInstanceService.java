package io.github.paasdash.service.kubernetes;

import io.github.paasdash.module.kubernetes.KubernetesInstance;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class KubernetesInstanceService {

    @Bean
    ApiClient createApiClient() throws IOException {
        String kubeConfigPath = System.getenv("KUBERNETES_DEFAULT_KUBE_CONFIG_PATH");
        final ApiClient apiClient;
        if (StringUtils.isNotEmpty(kubeConfigPath)) {
            apiClient = ClientBuilder.kubeconfig(
                            KubeConfig.loadKubeConfig(
                                    new FileReader(kubeConfigPath, StandardCharsets.UTF_8)))
                    .build();
        } else {
            apiClient = Config.defaultClient();
        }
        Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }

    public List<KubernetesInstance> getKubernetesInstances() {
        ArrayList<KubernetesInstance> list = new ArrayList<>();
        list.add(new KubernetesInstance("default"));
        return list;
    }
}
