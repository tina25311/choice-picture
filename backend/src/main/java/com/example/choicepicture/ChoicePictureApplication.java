package com.example.choicepicture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.example.choicepicture.mapper")
public class ChoicePictureApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ChoicePictureApplication.class, args);
    }
} 