package ru.practicum.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum"})
public class EwmMainService {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }
}
