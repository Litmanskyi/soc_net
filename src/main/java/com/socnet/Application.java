package com.socnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
//@EnableEurekaClient
//@PropertySource("classpath:messages_en.properties")
public class Application {

    private static ConfigurableApplicationContext run;
    //todo ++ set it in props
    public static void main(String[] args) {
        run = SpringApplication.run(Application.class, args);
    }

    public static <T> T getBean(Class<T> t) {
        return run.getBean(t);
    }

    @Bean
    public MessageSource messageSource() {//todo settings validate message
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
}
