package io.github.paasdash.service.kubernetes;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KubernetesDeployService {

    private final AppsV1Api appsV1Api;

    public KubernetesDeployService(@Autowired ApiClient apiClient) {
        this.appsV1Api = new AppsV1Api(apiClient);
    }

    public void createNamespacedDeploy(String namespace, V1Deployment v1Deployment) throws ApiException {
        appsV1Api.createNamespacedDeployment(namespace, v1Deployment,
                "true", null, null, null);
    }

    public void deleteDeploy(String namespace, String deployName) throws ApiException {
        appsV1Api.deleteNamespacedDeployment(deployName, namespace, "true",
                null, 30, false, null, null);
    }

    public List<V1Deployment> getNamespaceDeployments(String namespace) throws ApiException {
        return appsV1Api.listNamespacedDeployment(namespace, "true",
                null, null, null, null,
                null, null, null, null, null).getItems();
    }

    public void patchDeployment(String namespace, String deployName, V1Patch v1Patch) throws ApiException {
        appsV1Api.patchNamespacedDeployment(deployName, namespace, v1Patch, "true", null, null, null, false);
    }

    public void scaleDeploy(String namespace, String deploymentName, V1Scale v1Scale) throws ApiException {
        appsV1Api.replaceNamespacedDeploymentScale(deploymentName, namespace, v1Scale, "true",
                null, null, null);
    }

}
