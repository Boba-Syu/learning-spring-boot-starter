package cn.bobayu.ratelimiter.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.bobayu.ratelimiter")
public class RateLimiterTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimiterTestApplication.class, args);
    }
}