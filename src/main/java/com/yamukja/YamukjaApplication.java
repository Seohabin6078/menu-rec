package com.yamukja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YamukjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YamukjaApplication.class, args);
	}

}
