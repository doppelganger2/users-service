package com.example.usersservice.service;

import com.example.usersservice.jdbc.JdbcTemplateFactory;
import com.example.usersservice.util.UserDatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String GET_ALL_USERS_SQL = "SELECT * FROM users";
    private final JdbcTemplateFactory jdbcTemplateFactory;

    @Autowired
    public UserService(JdbcTemplateFactory jdbcTemplateFactory) {
        this.jdbcTemplateFactory = jdbcTemplateFactory;
    }

    // inserts mock data into all data sources
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent() {
        jdbcTemplateFactory.getTemplates().values().forEach(UserDatabaseUtil::initializeDatabase);
    }

    public List<Map<String, Object>> getUsers(String name, Integer age) {
        if (Objects.isNull(name) && Objects.isNull(age)) {
            return getAllUsers();
        } else {
            return getByParams(name, age);
        }
    }

    private List<Map<String, Object>> getByParams(String name, Integer age) {
        StringBuilder sql = new StringBuilder("SELECT id, name, age, email FROM users WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE :name");
            params.put("name", "%" + name + "%");
        }
        if (age != null) {
            sql.append(" AND age = :age");
            params.put("age", age);
        }
        try {
            return jdbcTemplateFactory.getTemplates().values().stream()
                    .map(jdbcTemplate -> jdbcTemplate.queryForList(sql.toString(), params))
                    .flatMap(Collection::stream)
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error getting users, reason:", e);
            throw e;
        }
    }

    private List<Map<String, Object>> getAllUsers() {
        try {
            return jdbcTemplateFactory.getTemplates().values().stream()
                    .map(jdbcTemplate -> jdbcTemplate.queryForList(GET_ALL_USERS_SQL))
                    .flatMap(Collection::stream)
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error getting users, reason:", e);
            throw e;
        }
    }
}
