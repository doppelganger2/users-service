package com.example.usersservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "Users API", description = "Aggregate user data from multiple data sources")
@RequestMapping("api/v1/users")
public interface UserApi {

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users, optionally filtered by name or age",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid query parameters",
                            content = @Content
                    )
            }
    )
    ResponseEntity<List<Map<String, Object>>> getUsers(
            @Parameter(description = "Filter by user name", example = "john")
            @RequestParam(required = false)
            @Size(min = 2, max = 20, message = "Name cannot contain less than 2 or more than 20 letters")
            String name,

            @Parameter(description = "Filter by user age", example = "25")
            @RequestParam(required = false)
            @Min(value = 16, message = "Age cannot be less than 16")
            @Max(value = 120, message = "Age cannot be greater than 120")
            Integer age
    );
}
