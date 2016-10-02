package com.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

import com.store.repository.CategoryRepository;
import com.store.repository.ProductRepository;

@SpringBootApplication
@EnableSwagger2
public class StoreApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(StoreApplication.class);

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);

	}

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
