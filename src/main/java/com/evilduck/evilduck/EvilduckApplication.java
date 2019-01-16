package com.evilduck.evilduck;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableScheduling
public class EvilduckApplication {
    public static void main(String[] args) {
        run(EvilduckApplication.class, args);
    }
}
