package com.store.rest.representation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.store.domain.Category;


@XmlRootElement(name="categories")
public class CategoryCollectionRepresentation {
	
	private List<Category> categories;
	
	public CategoryCollectionRepresentation() {

	}
		
	public CategoryCollectionRepresentation(List<Category> categories) {
		super();
		this.categories = categories;
	}
	
	@XmlElement(name="category")
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}


	
	

}
