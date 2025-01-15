package com.example.usersservice.service;

import com.example.usersservice.config.DataSourceConfigProps;
import com.example.usersservice.jdbc.JdbcTemplateFactory;
import com.example.usersservice.util.UserDatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String GET_ALL_USERS_SQL = "SELECT * FROM users";
    private final DataSourceConfigProps dataSourceConfigProps;
    private final JdbcTemplateFactory jdbcTemplateFactory;

    @Autowired
    public UserService(JdbcTemplateFactory jdbcTemplateFactory, DataSourceConfigProps dataSourceConfigProps) {
        this.dataSourceConfigProps = dataSourceConfigProps;
        this.jdbcTemplateFactory = jdbcTemplateFactory;
    }

    // inserts mock data into all data sources
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent() {
        jdbcTemplateFactory.getTemplates().values().forEach(UserDatabaseUtil::initializeDatabase);
    }

    public List<Map<String, Object>> getUsers() {
        var result = new ArrayList<Map<String, Object>>();

        try {
            for (var dataSource : dataSourceConfigProps.getDataSources()) {
                JdbcTemplate jdbcTemplate = jdbcTemplateFactory.getTemplates().get(dataSource.getName());
                result.addAll(jdbcTemplate.queryForList(GET_ALL_USERS_SQL));

            }
        } catch (Exception e) {
            LOGGER.error("Error getting users, reason:", e);
        }
        return result;
    }
}
