package com.example.usersservice;

import org.springframework.boot.SpringApplication;

public class TestUsersserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(UsersServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
