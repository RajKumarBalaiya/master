package com.springboot.serverless.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Raj
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.springboot.serverless.app")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE,
		RequestMethod.OPTIONS })
public class SpringBootServerlessApp extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootServerlessApp.class, args);
	}
}
