package com.example.usersservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "data-sources")
public class DataSourceConfigProps {
    private List<DataSourceProperties> dataSources;

    @Data
    public static class DataSourceProperties {
        private String name;
        private String strategy;
        private String url;
        private String table;
        private String user;
        private String password;
        private Map<String, String> mapping;
    }
}
