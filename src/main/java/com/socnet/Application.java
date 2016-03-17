package com.socnet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
@EnableEurekaClient
public class Application {

	private static ConfigurableApplicationContext run;
	public static String ROOT = "src/main/resources/upload-dir/";//todo set it in props

	public static void main(String[] args) {
		run = SpringApplication.run(Application.class, args);
	}
	public static <T> T getBean(Class<T> t) {
		return run.getBean(t);
	}
	@Bean
	CommandLineRunner init() {
		return (String[] args) -> {
			new File(ROOT).mkdir();
		};
	}
}
