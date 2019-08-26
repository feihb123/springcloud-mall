package cn.datacharm.mallimageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MallImageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallImageServiceApplication.class, args);
    }

}
