package com.store.rest.representation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.store.domain.Product;

@XmlRootElement(name="products")
public class ProductCollectionRepresentation {
	
	private List<Product> products;
	
	public ProductCollectionRepresentation() {

	}

	public ProductCollectionRepresentation(List<Product> products) {

		this.products = products;
	}
	
	@XmlElement(name="product")
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	

}
