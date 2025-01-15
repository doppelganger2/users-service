package com.example.usersservice.jdbc;

public enum TemplateCreationStrategy {
    POSTGRESQL("org.postgresql.Driver"),
    MYSQL("com.mysql.cj.jdbc.Driver"),
    MARIADB("org.mariadb.jdbc.Driver");

    final String driverClassName;


    TemplateCreationStrategy(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public static String getDriverClassName(String strategyName) {
        for (TemplateCreationStrategy strategy : TemplateCreationStrategy.values()) {
            if (strategy.toString().toLowerCase().equals(strategyName)) {
                return strategy.driverClassName;
            }
        }
        throw new IllegalArgumentException("Unknown template creation strategy: " + strategyName);
    }
}
