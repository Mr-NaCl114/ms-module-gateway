package com.lods.gateway.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "group")
public class TestConfig {

    private String opt1;
}
