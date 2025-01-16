plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.7.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
}

group = "com.example"
version = "0.0.1"
val springVersion = "3.4.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework:spring-jdbc:6.2.1")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.1")
    implementation("org.springframework.boot:spring-boot-starter:$springVersion")
    implementation("org.springframework.boot:spring-boot-configuration-processor:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-logging:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:mariadb")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

springBoot {
    mainClass.set("com.example.usersservice.UsersServiceApplication")
}

openApi {
    apiDocsUrl.set("http://localhost:8080/v3/api-docs")
    outputDir.set(file("$buildDir/openapi"))
    outputFileName.set("openapi.yaml")
    waitTimeInSeconds.set(30)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("generateOpenApiSpec") {
    dependsOn("bootRun")
    finalizedBy("openApiGenerate") 
    doLast {
        println("OpenAPI spec generated successfully")
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        eachFile {
            if (name == "java.sql.Driver") {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }
}

tasks.register<Exec>("buildDockerImage") {
    group = "docker"
    dependsOn("bootJar")
    commandLine(
        "docker",
        "build",
        "-t",
        "user-service",
        "--build-arg",
        "JAR_FILE=build/libs/${project.name}-${project.version}.jar",
        "."
    )
}

tasks.register<Exec>("runDockerContainer") {
    group = "docker"
    dependsOn("buildDockerImage")
    commandLine("docker", "run", "-p", "8080:8080", "user-service")
}
