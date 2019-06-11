package com.hang;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hangs.zhang
 */
@RestController
@SpringBootApplication
@MapperScan(basePackages = "com.hang")
public class CcsuServiceApplication   {

    public static void main(String[] args) {
        SpringApplication.run(CcsuServiceApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
