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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
