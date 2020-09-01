package com.example.wp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.wbs")
public class WpApplication {

    public static void main(String[] args) {
        SpringApplication.run(WpApplication.class, args);
    }

}
