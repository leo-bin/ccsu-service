package com.hang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hangs.zhang
 */
@RestController
@SpringBootApplication
@MapperScan(basePackages = "com.hang")
@EnableScheduling
public class CcsuServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CcsuServiceApplication.class, args);
    }
}
