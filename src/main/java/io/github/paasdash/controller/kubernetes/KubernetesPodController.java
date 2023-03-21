package io.github.paasdash.controller.kubernetes;

import io.github.paasdash.service.kubernetes.KubernetesPodService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/kubernetes/instances/default")
public class KubernetesPodController {

    private final KubernetesPodService kubernetesPodService;

    public KubernetesPodController(@Autowired KubernetesPodService kubernetesPodService) {
        this.kubernetesPodService = kubernetesPodService;
    }

    @PostMapping("/namespaces/{namespace}/pods")
    public ResponseEntity<Void> createPod(@PathVariable String namespace,
                                          @RequestBody V1Pod v1Pod) throws Exception {
        kubernetesPodService.createPod(namespace, v1Pod);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("namespaces/{namespace}/pods/{podName}")
    public ResponseEntity<Void> deletePod(@PathVariable String namespace,
                                          @PathVariable String podName) throws ApiException {
        kubernetesPodService.deletePod(namespace, podName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/namespaces/{namespace}/pods")
    public ResponseEntity<List<V1Pod>> getPodList(@PathVariable String namespace) throws ApiException {
        return new ResponseEntity<>(kubernetesPodService.getNamespacePods(namespace), HttpStatus.OK);
    }

    @PostMapping("/namespaces/{namespace}/pods/{podName}/execCommand")
    public ResponseEntity<String> execCommand(@PathVariable String namespace,
                                              @PathVariable String podName,
                                              @RequestBody String commands)
            throws IOException, ApiException {
        return new ResponseEntity<>(kubernetesPodService.execCommand(namespace, podName, commands),
                HttpStatus.OK);
    }

}
