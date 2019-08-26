package cn.datacharm.mallzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class MallZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallZuulApplication.class, args);
    }

}
