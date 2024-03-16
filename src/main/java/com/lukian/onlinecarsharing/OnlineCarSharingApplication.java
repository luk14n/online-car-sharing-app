package com.lukian.onlinecarsharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.lukian.onlinecarsharing.repository")
public class OnlineCarSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCarSharingApplication.class, args);
    }

}
