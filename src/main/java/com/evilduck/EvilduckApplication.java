package com.evilduck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EvilduckApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvilduckApplication.class, args);
    }

}
