package com.josetesan.spring.integration.messagingmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class MessagingMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingMonitorApplication.class, args);
	}
}
