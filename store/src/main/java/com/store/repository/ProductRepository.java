package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.domain.Product;
import com.store.exception.ProductNotFoundException;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Product findByProductSku(String productSku)
			throws ProductNotFoundException;

	public Product findByProductName(String productName)
			throws ProductNotFoundException;

}
