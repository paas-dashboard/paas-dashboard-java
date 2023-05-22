package io.github.paasdash.module.bookkeeper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookkeeperInstance {

    private String name;

    private String zkInstanceName;

    public BookkeeperInstance(String name) {
        this.name = name;
    }
}
