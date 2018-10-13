package com.spring.profiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.profiles.base.DatabaseService;

@RestController
public class DatabaseController {
	@Autowired
	private DatabaseService databaseService;

	@GetMapping("/test")
	public void test() {
		databaseService.getDBConnection();
	}
}
