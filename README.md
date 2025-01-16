# **Users Service**

### **Overview**
The **Users Service** is a RESTful API that allows querying and aggregating user data from **multiple data sources**. The service currently supports **PostgreSQL**, **MySQL**, and **MariaDB** databases. It provides the capability to filter users by fields like `name` and `age`. It can be easily ran using **Docker Compose**.

---

## **Features**
- Aggregate user data from multiple data sources.
- Filter users based on:
    - `name` (minimum 2 characters, maximum 20 characters).
    - `age` (valid range: 16 - 120).
- Built-in support for:
    - **PostgreSQL**
    - **MySQL**
    - **MariaDB**
- OpenAPI specification for documentation.
- Easily deployable with **Docker Compose**.

---

## **Getting Started**

### **Prerequisites**
- **Java 21** (required to build and run the application).
- **Gradle** (to manage dependencies and run OpenAPI tasks).
- **Docker Compose** (to run the service with Docker).
- At least one supported database (`PostgreSQL`, `MySQL`, or `MariaDB`) configured in the `.env` file.

---

### **Running the Service**

#### Step 1: Configure `.env` File
1. Locate the file `.env.local` in the root directory.
2. Rename `.env.local` to `.env`:
   ```bash
   mv .env.local.local .env.local
   ```
3. Edit the `.env` file to add connection details for your databases (e.g., PostgreSQL, MySQL, MariaDB).

---

#### Step 2: Start the Service with Docker Compose
Run the following command to start the service:
```bash
docker-compose up --build
```
This will:
- Build the application.
- Start the APIs and all necessary database containers described in the `docker-compose.yml`.

---

#### Step 3: Access the Service
Once the service is up and running, the following endpoints will be available:

- **Swagger UI**: View API documentation and test endpoints.
#### Using the generated spec:
- Import it into tools like **Postman** or **Swagger Editor** for testing.
- Share it with consumers of the API for integration.

---

### **Adding a New Data Source**

The Users Service uses a custom format for defining data sources, supporting **relational databases** such as **PostgreSQL**, **MySQL**, and **MariaDB**. To add a new data source, both configuration and minimal code updates are required. Follow the steps below:

---

#### 1. Update `application.yaml`

Add the new data source configuration under the `data-sources` section in your `application.yaml` file. For example:

```yaml
data-sources:
  - name: newdb                                  # Unique name for the new data source
    strategy: newdb                              # Custom strategy (must match step 5)
    url: ${DATASOURCE_NEWDB_URL}                 # Database connection URL
    table: users                                 # Name of the table to query
    user: ${DATASOURCE_NEWDB_USERNAME}           # Username from environment variable
    password: ${DATASOURCE_NEWDB_PASSWORD}       # Password from environment variable
    mapping:                                     # Column mapping for the `users` table
      user_id,
      username,
      name,
      lastname
```

> **Note**: Replace `strategy` and other properties according to your database type and structure.

---

#### 2. Add to `docker-compose.yml`

Include the new database in the `docker-compose.yml` file. For example:

```yaml
services:
  newdb:
    image: mysql:8.0                               # Use the appropriate database image
    container_name: newdb
    environment:
      MYSQL_DATABASE: newdb
      MYSQL_USER: newuser
      MYSQL_PASSWORD: newpassword
      MYSQL_ROOT_PASSWORD: rootpassword
    ports:
      - "3308:3306"                                # Ensure the ports do not conflict with other containers
```

---

#### 3. Update `.env`

Add the necessary connection details to the `.env` file:
```DATASOURCE_NEWDB_URL=jdbc:mysql://newdb:3306/newdb DATASOURCE_NEWDB_USERNAME=newuser DATASOURCE_NEWDB_PASSWORD=newpassword```

---

#### 4. Add the Driver Dependency to `build.gradle`

Add the JDBC driver for the new data source to the `build.gradle` file. For example:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc' // This is already included
    implementation 'mysql:mysql-connector-java:8.0.34' // Replace with the appropriate driver dependency
}
```

> **Note**: The dependency should match the database type and version you are using.

After adding the dependency, run the following command to update your project:

```bash
./gradlew build --refresh-dependencies
```

---

#### 5. Add the Enum Value for the Strategy

Navigate to **`TemplateCreationStrategy.java`**, where the supported database strategies are defined. Add a new enum value for your custom strategy. For example:

```java
public enum TemplateCreationStrategy {
    POSTGRESQL("org.postgresql.Driver"),
    MYSQL("com.mysql.cj.jdbc.Driver"),
    MARIADB("org.mariadb.jdbc.Driver"),
    NEWDB("com.mysql.cj.jdbc.Driver"); // Replace with the correct driver for your database

    private final String driverClassName;

    TemplateCreationStrategy(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
```

Here:
- The `NEWDB` enum value represents the new data source strategy (must match the `strategy` field specified in `application.yaml`).
- Add the corresponding **JDBC driver class name** for your database.

---

#### 6. Restart the Application

After updating the configuration, dependencies, and code, restart the application and containers to apply the changes:

```bash
docker-compose down && docker-compose up --build
```

The new data source will now be included in the aggregation operations.

---

#### **Important Notes**
- Ensure that the `driverClassName` for the new enum value in `TemplateCreationStrategy.java` is the correct fully-qualified name of the JDBC driver for your database.
- Add the correct dependency for the JDBC driver to `build.gradle`.
- The `name` field in each data source must be unique to prevent conflicts.
- Only **relational databases** are supported.
- The `mapping` property must accurately reflect the column names in the database table that correspond to the `users` structure in the service.
- Avoid port conflicts when adding new services to `docker-compose.yml` (e.g., map `3306` to `3308` for MySQL).

---

## **Project Structure**

```plaintext
usersservice
│
├── src/main/java/com/example/usersservice
│   ├── api
│   │   ├── UserApi.java          # Defines the API interface and contracts.
│   │   └── OpenApiConfig.java    # Configuration for global OpenAPI metadata.
│   ├── controller
│   │   └── UserController.java   # Implementation of the User API.
│   ├── service
│   │   └── UserService.java      # Business logic for user management.
│   ├── config
│   │   └── DataSourceConfigProps.java # Configuration of database sources.
│   ├── jdbc
│   │   └── JdbcTemplateFactory.java   # Factory for creating named JDBC templates.
│   ├── util
│   │   └── UserDatabaseUtil.java      # Utility class for database operations.
│   └── UsersServiceApplication.java   # Spring Boot entry point.
│
├── .env.local                     # Local environment example file.
├── docker-compose.yml             # Docker Compose configuration file.
├── build.gradle                   # Gradle configuration.
└── README.md                      # Documentation file (you are here).
```

---

## **Technologies Used**
- **Spring Framework**:
    - Spring Boot
    - Spring MVC
- **Jakarta EE** for annotations and validation.
- **OpenAPI/Swagger** for API documentation.
- **Databases**:
    - PostgreSQL
    - MySQL
    - MariaDB
- **Docker Compose** for containerization.
- **Gradle** for build and dependency management.

---

## **License**
This project is licensed under the Apache License 2.0 - see the `LICENSE` file for details.
