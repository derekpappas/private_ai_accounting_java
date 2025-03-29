package com.accounting.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableTransactionManagement
public class IntegrationTestConfig {

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }
} 