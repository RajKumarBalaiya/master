package com.aws.cognito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration // it includes @SpringBootApplication
@ComponentScan("com.aws.cognito") // it includes @SpringBootApplication
public class AwscognitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwscognitoApplication.class, args);
	}

}
