package io.github.paasdash.service.zookeeper;

import io.github.paasdash.module.zookeeper.DeleteNodeReq;
import io.github.paasdash.module.zookeeper.UpdateInf;
import io.github.paasdash.module.zookeeper.ZookeeperInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZkService {

    private final Map<String, ZookeeperInstance> zookeeperInstanceMap;

    public ZkService(@Autowired ZookeeperInstanceService configService) {
        this.zookeeperInstanceMap = configService.getZookeeperInstanceMap();
    }

    public ZooKeeper newZookeeper(String instanceId) throws IOException {
        return new ZooKeeper(zookeeperInstanceMap.get(instanceId).getAddr(), 30000, null);
    }

    public ZooKeeper newZookeeper(String instanceId, Watcher watcher) throws IOException {
        return new ZooKeeper(zookeeperInstanceMap.get(instanceId).getAddr(), 30000, watcher);
    }

    public void putZnodeContent(String instanceId, String path, byte[] content, boolean createIfNotExists)
            throws Exception {
        try (ZooKeeper zooKeeper = newZookeeper(instanceId)) {
            if (zooKeeper.exists(path, false) == null) {
                if (createIfNotExists) {
                    zooKeeper.create(path, content, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {
                    throw new RuntimeException("path not exists");
                }
            } else {
                zooKeeper.setData(path, content, -1);
            }
        }
    }

    public void updateZnodeContentCas(String instanceId, String path, UpdateInf<byte[]> updateInf) {
        try (ZooKeeper zooKeeper = newZookeeper(instanceId)) {
            Stat stat = zooKeeper.exists(path, false);
            if (stat == null) {
                throw new RuntimeException("path not exists");
            }
            byte[] oldContent = zooKeeper.getData(path, false, stat);
            byte[] newContent = updateInf.update(oldContent);
            if (newContent == null) {
                return;
            }
            zooKeeper.setData(path, newContent, stat.getVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getZnodeContent(String instanceId, String path) throws Exception {
        try (ZooKeeper zooKeeper = newZookeeper(instanceId)) {
            return zooKeeper.getData(path, false, new Stat());
        }
    }

    public byte[] getZnodeContent(ZooKeeper zooKeeper, String path) throws Exception {
        return zooKeeper.getData(path, false, new Stat());
    }

    public List<String> getChildren(String instanceId, String path) throws Exception {
        try (ZooKeeper zooKeeper = newZookeeper(instanceId)) {
            return zooKeeper.getChildren(path, false);
        }
    }

    public List<String> getChildren(ZooKeeper zooKeeper, String path) throws Exception {
        return zooKeeper.getChildren(path, false);
    }

    public void deleteNode(String instanceId, DeleteNodeReq req) throws Exception {
        try (ZooKeeper zk = newZookeeper(instanceId,
                watchedEvent -> log.info("zk process : {}", watchedEvent))
        ) {
            zk.delete(req.getPath(), req.getVersion());
        }
    }

    public List<String> getZnodesRecursive(String instanceId, String rootPath) throws Exception {
        try (ZooKeeper zk = newZookeeper(instanceId,
                watchedEvent -> log.info("zk process : {}", watchedEvent))
        ) {
            final Deque<String> stack = new ArrayDeque<>();
            List<String> children = zk.getChildren(rootPath, null);
            if ("/".equals(rootPath)) {
                for (String child : children) {
                    stack.push(rootPath + child);
                }
            } else {
                for (String child : children) {
                    stack.push(rootPath + "/" + child);
                }
            }

            String path = "";
            List<String> znodes = new ArrayList<>();
            while ((path = stack.pollFirst()) != null) {
                znodes.add(path);
                List<String> childrens = zk.getChildren(path, null);
                for (String child : childrens) {
                    stack.push(path + "/" + child);
                }
            }
            return znodes;
        }
    }
}
