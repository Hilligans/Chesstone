package dev.hilligans.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringHandler {

    public static void run(String[] args) {
        SpringApplication.run(SpringHandler.class, args);
    }
}
