package com.hang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hangs.zhang
 */
@RestController
@SpringBootApplication
@MapperScan(basePackages = "com.hang")
public class CcsuServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CcsuServiceApplication.class, args);
    }
}
