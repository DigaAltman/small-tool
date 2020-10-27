package com.diga.orm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.Serializable;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class App implements Serializable {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
