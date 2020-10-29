package com.diga.orm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    // 可以通过 http://localost:port/swagger-ui.html    来访问 swagger 文档
    // 可以通过 http://localhost:port/doc.html          来查看 接口 信息

    @Bean
    public Docket createRestAPI() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(APIINFO()).select().apis(RequestHandlerSelectors.basePackage("com.diga.orm.controller")).paths(PathSelectors.any()).build();

    }

    private ApiInfo APIINFO() {
        return new ApiInfoBuilder().title("STool API").contact(new Contact("STool", "localhost", "15113437287@163.com")).description("API文档").version("0.0.5.RELEASE").termsOfServiceUrl("http://localhost").build();
    }
}
