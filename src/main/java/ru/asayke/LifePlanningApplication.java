package ru.asayke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LifePlanningApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifePlanningApplication.class, args);
    }

}
