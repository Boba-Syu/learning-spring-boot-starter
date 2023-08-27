package cn.bobayu.hystrix.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.bobayu.hystrix")
public class HystrixListTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixListTestApplication.class, args);
    }

}
