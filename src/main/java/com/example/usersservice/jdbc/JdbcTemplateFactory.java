package com.example.usersservice.jdbc;

import com.example.usersservice.config.DataSourceConfigProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JdbcTemplateFactory {
    private final Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<>();

    @Autowired
    public JdbcTemplateFactory(DataSourceConfigProps dataSourceConfigProps) {
        dataSourceConfigProps.getDataSources().forEach(dataSource -> jdbcTemplateMap.put(
                dataSource.getName(),
                this.createJdbcTemplate(
                        dataSource.getUrl(),
                        dataSource.getUser(),
                        dataSource.getPassword(),
                        TemplateCreationStrategy.getDriverClassName(dataSource.getStrategy())
                )));
    }

    private JdbcTemplate createJdbcTemplate(String url, String username, String password, String driverClassName) {
        var dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClassName);
        return new JdbcTemplate(dataSource);
    }

    public Map<String, JdbcTemplate> getTemplates() {
        return this.jdbcTemplateMap;
    }
}

