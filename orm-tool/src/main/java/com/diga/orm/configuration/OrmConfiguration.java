package com.diga.orm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "db")
@EnableAutoConfiguration
public class OrmConfiguration {

    @Value("${resultMapLocation}")
    private String resultMapLocation;

    @Bean
    public ResultMapFactoryBean resultMapFactoryBean() {
        return new ResultMapFactoryBean(resultMapLocation);
    }
}
