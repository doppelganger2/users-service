package com.example.usersservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

@UtilityClass
public class UserDatabaseUtil {

    private static final String CREATE_DATABASE_SQL = "CREATE DATABASE IF NOT EXISTS Db";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users (" +
            "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "user_id VARCHAR(50) UNIQUE, " +
            "username VARCHAR(50), " +
            "name VARCHAR(50), " +
            "lastname VARCHAR(50))";
    private static final String INSERT_USER_SQL = "INSERT INTO users (user_id, username, name, lastname) VALUES (?, ?, ?, ?)";

    public static void initializeDatabase(JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.execute(CREATE_DATABASE_SQL);
            jdbcTemplate.execute(CREATE_DATABASE_SQL);
        } catch (Exception e) {
            System.out.println("Database creation failed or already exists: " + e.getMessage());
        }

        jdbcTemplate.execute(CREATE_TABLE_SQL);

        for (int i = 1; i <= 10; i++) {
            String userId = UUID.randomUUID().toString();
            String username = "user" + i;
            String name = "Name" + i;
            String lastname = "Lastname" + i;
            jdbcTemplate.update(INSERT_USER_SQL, userId, username, name, lastname);
        }

        System.out.println("Database and table created, and mock data inserted successfully!");
    }
}


