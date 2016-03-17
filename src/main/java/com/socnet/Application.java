package com.socnet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.File;

@SpringBootApplication
//@EnableEurekaClient
//@PropertySource("classpath:messages.properties")
public class Application {

    private static ConfigurableApplicationContext run;
    public final static String ROOT = "src/main/resources/upload-dir/";//todo set it in props

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


    @Bean
    public MessageSource messageSource() {//todo settings validate message
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages.properties");
        return messageSource;
    }
}
