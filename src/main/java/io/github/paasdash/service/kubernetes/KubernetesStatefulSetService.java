package io.github.paasdash.service.kubernetes;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Scale;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KubernetesStatefulSetService {

    private final AppsV1Api appsV1Api;

    public KubernetesStatefulSetService(@Autowired ApiClient apiClient) {
        this.appsV1Api = new AppsV1Api(apiClient);
    }

    public void createNamespacedStatefulSet(String namespace, V1StatefulSet v1StatefulSet) throws ApiException {

        appsV1Api.createNamespacedStatefulSet(namespace, v1StatefulSet,
                "true", null, null, null);
    }

    public void deleteStatefulSet(String namespace, String statefulSetName) throws ApiException {
        appsV1Api.deleteNamespacedStatefulSet(statefulSetName, namespace, "true",
                null, 30, false, null, null);
    }

    public List<V1StatefulSet> getNamespaceStatefulSets(String namespace) throws ApiException {
        return appsV1Api.listNamespacedStatefulSet(namespace, "true",
                null, null, null, null,
                null, null, null, null, null).getItems();
    }

    public void patch(String namespace, String statefulSetName, V1Patch v1Patch) throws ApiException {
        appsV1Api.patchNamespacedStatefulSet(statefulSetName, namespace, v1Patch, "true", null, null, null, null);
    }

    public void scaleStatefulSet(String namespace, String statefulSetName, V1Scale v1Scale) throws ApiException {
        appsV1Api.replaceNamespacedStatefulSetScale(statefulSetName, namespace, v1Scale, "true",
                null, null, null);
    }
}
