package com.sprinboot.serverless.app.test;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.springboot.serverless.app.SpringBootServerlessApp;

@ContextConfiguration(classes = { SpringBootServerlessApp.class })
@WebAppConfiguration
public class EmployeeControllerTest {

	@Test
	void contextLoads() {
	}
}