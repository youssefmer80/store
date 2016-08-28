package com.store.rest.controller;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.domain.Product;
import com.store.exception.ProductNotFoundException;
import com.store.repository.ProductRepository;
import com.store.rest.representation.ProductCollectionRepresentation;

@RestController
@Transactional
public class ProductController {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@RequestMapping(value = "/product/id/{id}", method = RequestMethod.GET)
	public Product getProductById(@PathVariable("id") Long productId)
			throws ProductNotFoundException {
		Product product = productRepository.findOne(productId);
		if (product == null) {
			throw new ProductNotFoundException("the product id " + productId
					+ " is not existed");
		}
		return product;
	}

	@RequestMapping(value = "/product/name/{name}", method = RequestMethod.GET)
	public Product getProductByName(@PathVariable("name") String productName)
			throws ProductNotFoundException {

		Product product = productRepository.findByProductName(productName);
		if (product == null) {
			throw new ProductNotFoundException("the product name "
					+ productName + " is not existed");
		}
		return product;
	}

	@RequestMapping(value = "/product/sku/{sku}", method = RequestMethod.GET)
	public Product getProductBySKU(@PathVariable("sku") String productSKU)
			throws ProductNotFoundException {

		Product product = productRepository.findByProductSku(productSKU);
		if (product == null) {
			throw new ProductNotFoundException("the product Sku" + productSKU
					+ " is not existed");
		}
		return product;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ProductCollectionRepresentation getAllProducts(
			@RequestParam(required = false) Integer first,
			@RequestParam(required = false) Integer last) {

		List<Product> products = productRepository.findAll();

		if (products == null) {
			throw new ProductNotFoundException(
					"no Products are available in the store");
		}

		if (first != null && (last != null && last < products.size())) {
			return new ProductCollectionRepresentation(products.subList(
					first - 1, last));
		} else if (first != null && (last == null || last > products.size())) {
			return new ProductCollectionRepresentation(products.subList(
					first - 1, products.size()));
		}

		return new ProductCollectionRepresentation(products);
	}

	@RequestMapping(value = "/product/id/{id}", method = RequestMethod.PUT)
	public Product updateProductById(@RequestBody Product product,
			@PathVariable("id") long productId) {

		Product foundProduct = productRepository.findOne(productId);
		if (foundProduct == null) {
			throw new ProductNotFoundException("the product Id " + productId
					+ " is not existed to update it");
		}
		
		String productSku = product.getProductSku() != null?product.getProductSku():foundProduct.getProductSku();
		String productName = product.getProductName() != null?product.getProductName():foundProduct.getProductName();
		LocalDate CreatedDate = foundProduct.getProductCreated();
		
		Product UpdatedProduct = new Product(foundProduct.getProductId(),
				productSku, productName,CreatedDate,
				LocalDate.now());
		
		UpdatedProduct = productRepository.saveAndFlush(UpdatedProduct);
		
		return UpdatedProduct;
	}
	
	@RequestMapping(value = "/product/sku/{sku}", method = RequestMethod.PUT)
	public Product updateProductBySku(@RequestBody Product product,
			@PathVariable("sku") String sku) {

		Product foundProduct = productRepository.findByProductSku(sku);
		if (foundProduct == null) {
			throw new ProductNotFoundException("the product sku " + sku
					+ " is not existed to update it");
		}
		
		String productSku = product.getProductSku() != null?product.getProductSku():foundProduct.getProductSku();
		String productName = product.getProductName() != null?product.getProductName():foundProduct.getProductName();
		LocalDate CreatedDate = foundProduct.getProductCreated();
		
		Product UpdatedProduct = new Product(foundProduct.getProductId(),
				productSku, productName,CreatedDate,
				LocalDate.now());
		
		UpdatedProduct = productRepository.saveAndFlush(UpdatedProduct);
		
		return UpdatedProduct;
	}

}
