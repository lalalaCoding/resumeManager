package com.my.resumeManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ResumeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeManagerApplication.class, args);
	}

}
