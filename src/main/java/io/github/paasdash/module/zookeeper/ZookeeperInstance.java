package io.github.paasdash.module.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ZookeeperInstance {
    private String name;

    private String addr;

    public ZookeeperInstance(String name) {
        this.name = name;
    }
}
