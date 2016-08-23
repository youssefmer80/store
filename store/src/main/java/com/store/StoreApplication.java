package com.store;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.store.repository.CategoryRepository;
import com.store.repository.ProductRepository;

@SpringBootApplication
public class StoreApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreApplication.class);
	
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
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
		b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		return b;
	}
	
//	@Bean(name = "OBJECT_MAPPER_BEAN")
//	public ObjectMapper jsonObjectMapper() {
//	    return Jackson2ObjectMapperBuilder.json()
//	            .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
//	            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
//	            .modules(new JSR310Module())
//	            .build();
//	}

}
