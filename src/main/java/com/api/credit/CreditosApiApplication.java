package com.api.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.api.credit.repository")
public class CreditosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditosApiApplication.class, args);
    }

}