package com.diga.orm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.Serializable;

@SpringBootApplication
@EnableAspectJAutoProxy
public class App implements Serializable {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
