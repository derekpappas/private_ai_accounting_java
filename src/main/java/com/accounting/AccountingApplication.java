package com.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.accounting.model.entity")
@EnableJpaRepositories("com.accounting.repository")
@ComponentScan(basePackages = {
    "com.accounting.api",
    "com.accounting.api.mapper",
    "com.accounting.service",
    "com.accounting.config",
    "com.accounting.repository"
})
public class AccountingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }
} 