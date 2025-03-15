package com.alex.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@Slf4j
public class PostApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostApplication.class, args);
		log.info("Post service started successfully...");
	}

}
