package cn.bobasyu.methodext.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.bobasyu.methodext")
public class MethodextListTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MethodextListTestApplication.class, args);
    }

}
