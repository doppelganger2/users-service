package com.example.usersservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        return dataSource;
    }

//    @Bean
//    public MySQLContainer<?> mySQLContainer() {
//        MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
//                .withDatabaseName("testdb")
//                .withUsername("testuser")
//                .withPassword("testpass");
//        mySQLContainer.start();
//        return mySQLContainer;
//    }
//
//    @Bean
//    public GenericContainer<?> mariaDBContainer() {
//        MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:latest")
//                .withEnv("MARIADB_DATABASE", "testdb")
//                .withEnv("MARIADB_USER", "testuser")
//                .withEnv("MARIADB_PASSWORD", "testpass")
//                .withEnv("MARIADB_ROOT_PASSWORD", "rootpass")
//                .withExposedPorts(3306);
//        mariaDBContainer.start();
//        return mariaDBContainer;
//    }

    @DynamicPropertySource
    public static void configure(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
//        MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
//                .withDatabaseName("testdb")
//                .withUsername("testuser")
//                .withPassword("testpass");
//        MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:latest")
//                .withEnv("MARIADB_DATABASE", "testdb")
//                .withEnv("MARIADB_USER", "testuser")
//                .withEnv("MARIADB_PASSWORD", "testpass")
//                .withEnv("MARIADB_ROOT_PASSWORD", "rootpass")
//                .withExposedPorts(3306);

        registry.add("DATASOURCE_POSTGRES_URL", postgreSQLContainer::getJdbcUrl);
        registry.add("DATASOURCE_POSTGRES_USERNAME", postgreSQLContainer::getUsername);
        registry.add("DATASOURCE_POSTGRES_PASSWORD", postgreSQLContainer::getPassword);

//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//
//        registry.add("spring.datasource.url", () -> "jdbc:mariadb://localhost:" + mariaDBContainer.getFirstMappedPort() + "/testdb");
//        registry.add("spring.datasource.username", () -> "testuser");
//        registry.add("spring.datasource.password", () -> "testpass");
    }
}

