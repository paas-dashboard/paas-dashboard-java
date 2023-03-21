package io.github.paasdash.controller.kubernetes;

import io.github.paasdash.service.kubernetes.KubernetesDeployService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Scale;
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
public class KubernetesDeployController {

    public KubernetesDeployService deployService;

    public KubernetesDeployController(@Autowired KubernetesDeployService deployService) {
        this.deployService = deployService;
    }

    @PutMapping("/namespaces/{namespace}/deployments")
    public ResponseEntity<Void> createDeployment(@PathVariable String namespace,
                                                 @RequestBody V1Deployment v1Deployment) throws Exception {
        deployService.createNamespacedDeploy(namespace, v1Deployment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/namespaces/{namespace}/deployments/{deployName}")
    public ResponseEntity<Void> deleteDeploy(@PathVariable String namespace,
                                             @PathVariable String deployName) throws ApiException {
        deployService.deleteDeploy(namespace, deployName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/namespaces/{namespace}/deployments")
    public ResponseEntity<List<V1Deployment>> getDeployList(@PathVariable String namespace) throws ApiException {
        return new ResponseEntity<>(deployService.getNamespaceDeployments(namespace), HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/deployments/{deployName}/scale")
    public ResponseEntity<Void> scaleDeployment(@PathVariable String namespace,
                                                @PathVariable String deployName,
                                                @RequestBody V1Scale v1Scale) throws ApiException {
        deployService.scaleDeploy(namespace, deployName, v1Scale);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/deployments/{deployName}")
    public ResponseEntity<Void> patchDeployment(@PathVariable String namespace,
                                                @PathVariable String deployName,
                                                @RequestBody V1Patch v1Patch) throws ApiException {
        deployService.patchDeployment(namespace, deployName, v1Patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/deployments/{deployName}/ready-check")
    public ResponseEntity<Boolean> readyCheck(@PathVariable String namespace,
                                              @PathVariable String deployName,
                                              @RequestParam(required = false) String image)
            throws ApiException {
        V1Deployment v1Deployment;
        List<V1Deployment> deployments = deployService.getNamespaceDeployments(namespace)
                .stream()
                .filter(v1Deployment1 -> Objects.equals(
                        Objects.requireNonNull(v1Deployment1.getMetadata()).getName(), deployName))
                .toList();
        if (deployments.size() > 0) {
            v1Deployment = deployments.get(0);
            if (image != null && !image.equals(Objects.requireNonNull(Objects.requireNonNull(v1Deployment.getSpec())
                    .getTemplate().getSpec()).getContainers().get(0).getImage())) {
                return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
            }
            if (v1Deployment.getStatus() != null && !Objects.equals(v1Deployment.getStatus().getAvailableReplicas(),
                    v1Deployment.getStatus().getReplicas())) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
