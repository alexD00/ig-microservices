package com.alex.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class ActionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionApplication.class, args);
		log.info("Action service started successfully...");
	}

}
