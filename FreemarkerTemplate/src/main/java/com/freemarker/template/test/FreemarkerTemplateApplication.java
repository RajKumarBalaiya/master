package com.freemarker.template.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.freemarker.template.test")
public class FreemarkerTemplateApplication {
	@Autowired
	private static FreemarkerUtils freemarkerUtils;

	public static void main(String[] args) {
		SpringApplication.run(FreemarkerTemplateApplication.class, args);

		Student studentModel = new Student();
		studentModel.setUserName("raj kumar balaiya");
		studentModel.setFirstName("raj");
		studentModel.setLastName("kumar");
		studentModel.setEmail("aaaa@123");
		studentModel.setPhone(123456789);
		Map<String, Object> templateObject = new HashMap<>();
		templateObject.put("student", studentModel);
		FreemarkerUtils.readTemplate(templateObject);
	}

}
