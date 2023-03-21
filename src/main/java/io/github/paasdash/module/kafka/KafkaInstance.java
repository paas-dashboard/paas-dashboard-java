package io.github.paasdash.module.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaInstance  {
    private String name;

    private String bootstrapServers;

    public KafkaInstance(String name) {
        this.name = name;
    }
}
