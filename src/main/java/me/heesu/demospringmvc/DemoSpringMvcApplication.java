package me.heesu.demospringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class DemoSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringMvcApplication.class, args);
    }

}
