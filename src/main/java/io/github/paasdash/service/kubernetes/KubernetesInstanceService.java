package io.github.paasdash.service.kubernetes;

import io.github.paasdash.module.kubernetes.KubernetesInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KubernetesInstanceService {

    public List<KubernetesInstance> getKubernetesInstances() {
        ArrayList<KubernetesInstance> list = new ArrayList<>();
        list.add(new KubernetesInstance("default"));
        return list;
    }
}
