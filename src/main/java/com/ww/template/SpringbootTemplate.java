package com.ww.template;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.ww.template.mapper")
@EnableTransactionManagement
@EnableAsync
public class SpringbootTemplate {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootTemplate.class, args);
    }
}