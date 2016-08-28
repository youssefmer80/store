package com.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
