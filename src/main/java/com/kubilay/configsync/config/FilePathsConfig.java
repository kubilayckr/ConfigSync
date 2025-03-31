package com.kubilay.configsync.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-config.file-paths")
@Getter
@Setter
public class FilePathsConfig {
    private String configPath;
    private String specificConfigPath;
}
