package com.example.usersservice.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                description = "APIs for retrieving users",
                version = "1.0.0",
                contact = @Contact(
                        name = "John Doe",
                        email = "support@example.com",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Server"
                ),
                @Server(
                        url = "https://userservice.example.com",
                        description = "Production Server"
                )
        }
)
public class OpenApiConfig {
}