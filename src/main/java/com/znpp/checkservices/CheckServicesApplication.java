package com.znpp.checkservices;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ForestScan(basePackages = "com.znpp.checkservices.client")
public class CheckServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckServicesApplication.class, args);
    }

}
