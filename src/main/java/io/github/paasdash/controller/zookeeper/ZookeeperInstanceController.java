package io.github.paasdash.controller.zookeeper;

import io.github.paasdash.module.zookeeper.ZookeeperInstance;
import io.github.paasdash.service.zookeeper.ZookeeperInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/zookeeper")
public class ZookeeperInstanceController {

    private final ZookeeperInstanceService zookeeperInstanceService;

    public ZookeeperInstanceController(@Autowired ZookeeperInstanceService zookeeperInstanceService) {
        this.zookeeperInstanceService = zookeeperInstanceService;
    }

    @GetMapping("/instances")
    public List<ZookeeperInstance> getZookeeperInstances() {
        return zookeeperInstanceService.getZookeeperInstanceMap().values().stream().toList();
    }
}
