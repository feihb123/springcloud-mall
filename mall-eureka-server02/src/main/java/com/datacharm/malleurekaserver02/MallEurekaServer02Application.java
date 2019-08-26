package com.datacharm.malleurekaserver02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MallEurekaServer02Application {

    public static void main(String[] args) {
        SpringApplication.run(MallEurekaServer02Application.class, args);
    }

}
