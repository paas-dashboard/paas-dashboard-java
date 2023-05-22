package io.github.paasdash.module.zookeeper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteNodeReq {

    private String path;

    private int version;

    public DeleteNodeReq() {
    }
}
