package io.github.paasdash.controller.kubernetes;

import io.github.paasdash.service.kubernetes.KubernetesStatefulSetService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Scale;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/kubernetes/instances/default")
public class KubernetesDefaultStatefulSetController {

    public KubernetesStatefulSetService kubernetesStatefulSetService;

    public KubernetesDefaultStatefulSetController(
            @Autowired KubernetesStatefulSetService kubernetesStatefulSetService) {
        this.kubernetesStatefulSetService = kubernetesStatefulSetService;
    }

    @PutMapping("/namespaces/{namespace}/stateful-sets")
    public ResponseEntity<Void> createStatefulSet(@PathVariable String namespace, V1StatefulSet v1StatefulSet)
            throws Exception {
        kubernetesStatefulSetService.createNamespacedStatefulSet(namespace, v1StatefulSet);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/namespaces/{namespace}/stateful-sets/{statefulSetName}")
    public ResponseEntity<Void> deleteDeploy(@PathVariable String namespace,
                                             @PathVariable String statefulSetName) throws ApiException {
        kubernetesStatefulSetService.deleteStatefulSet(namespace, statefulSetName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/namespaces/{namespace}/stateful-sets")
    public ResponseEntity<List<V1StatefulSet>> getDeployList(@PathVariable String namespace) throws ApiException {
        return new ResponseEntity<>(kubernetesStatefulSetService.getNamespaceStatefulSets(namespace), HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/stateful-sets/{statefulSetName}/scale")
    public ResponseEntity<Void> scaleDeployment(@PathVariable String namespace,
                                                @PathVariable String statefulSetName,
                                                @RequestBody V1Scale v1Scale) throws ApiException {
        kubernetesStatefulSetService.scaleStatefulSet(namespace, statefulSetName, v1Scale);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/stateful-sets/{statefulSetName}")
    public ResponseEntity<Void> patchDeployment(@PathVariable String namespace,
                                                @PathVariable String statefulSetName,
                                                @RequestBody V1Patch v1Patch) throws ApiException {
        kubernetesStatefulSetService.patch(namespace, statefulSetName, v1Patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/namespaces/{namespace}/stateful-sets/{statefulSetName}/ready-check")
    public ResponseEntity<Boolean> readyCheck(@PathVariable String namespace,
                                              @PathVariable String statefulSetName,
                                              @RequestParam(required = false) String image)
            throws ApiException {
        V1StatefulSet v1StatefulSet;
        List<V1StatefulSet> statefulSets = kubernetesStatefulSetService.getNamespaceStatefulSets(namespace)
                .stream()
                .filter(statefulSet -> Objects.equals(
                        Objects.requireNonNull(statefulSet.getMetadata()).getName(), statefulSetName))
                .toList();
        if (statefulSets.size() > 0) {
            v1StatefulSet = statefulSets.get(0);
            if (image != null && !image.equals(Objects.requireNonNull(Objects.requireNonNull(v1StatefulSet.getSpec())
                    .getTemplate().getSpec()).getContainers().get(0).getImage())) {
                return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
            }
            if (v1StatefulSet.getStatus() != null && !Objects.equals(v1StatefulSet.getStatus().getAvailableReplicas(),
                    v1StatefulSet.getStatus().getReplicas())) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
