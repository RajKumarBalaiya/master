package com.spring.profiles;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.profiles.base.DatabaseService;

@SpringBootApplication(scanBasePackages = "com.spring.profiles")
public class SpringProfilesApplication {

	@Autowired
	private DatabaseService databaseService;

	public static void main(String[] args) {

		SpringApplication.run(SpringProfilesApplication.class, args);

	}

	@PostConstruct
	public void init() {
		databaseService.getDBConnection();
	}
}
