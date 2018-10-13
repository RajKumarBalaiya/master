package com.spring.profiles.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.profiles.base.DatabaseService;
import com.spring.profiles.controller.DatabaseManager;

@Configuration
public class AppContext {
	@Bean
	public DatabaseService databaseService() {
		return new DatabaseManager();
	}
}
