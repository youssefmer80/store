package com.store.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.groupName("store")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())              
                //.paths(PathSelectors.any())
                .paths(regex("/store/.*"))
                .build();

    }
        
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Store REST Application")
                .description("Store REST Application with Swagger")
                .license("Apache License Version 2.0")
                .licenseUrl("")
                .version("2.0")
                .build();
    }

}
