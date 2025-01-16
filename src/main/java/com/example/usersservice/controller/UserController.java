package com.example.usersservice.controller;

import com.example.usersservice.api.UserApi;
import com.example.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> getUsers(String name, Integer age) {
        return ResponseEntity.ok(userService.getUsers(name, age));
    }
}
