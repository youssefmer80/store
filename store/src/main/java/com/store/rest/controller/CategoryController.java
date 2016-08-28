package com.store.rest.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.domain.Category;
import com.store.exception.CategoryNotFoundException;
import com.store.repository.CategoryRepository;
import com.store.rest.representation.CategoryCollectionRepresentation;
import com.store.rest.representation.ProductCollectionRepresentation;


@RestController
@Transactional
public class CategoryController {
	
	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;
	
	
	@RequestMapping(value="/category/id/{id}", method= RequestMethod.GET)
	public Category getCategoryById(@PathVariable("id") long CategoryId){
		
		Category category = categoryRepository.findOne(CategoryId);
		if(category == null){
			throw new CategoryNotFoundException("the category id " + CategoryId
					+ " is not existed");
		}
		return category;
	}
	
	@RequestMapping(value="/category/name/{name}", method= RequestMethod.GET)
	public Category getCategoryByName(@PathVariable("name") String CategoryName){
		 
		Category category = categoryRepository.getCategoryByCategoryName(CategoryName);
		if(category == null){
			throw new CategoryNotFoundException("the category name " + CategoryName
					+ " is not existed");
		}
		
		return category;
	}
	
	@RequestMapping(value="/categories", method=RequestMethod.GET)
	public CategoryCollectionRepresentation getAllCategories(@RequestParam(required=false) Integer first,
			@RequestParam(required=false) Integer last){
		
		List<Category> categories = categoryRepository.findAll();
		
		if(categories == null){
			throw new CategoryNotFoundException("no Categories are available in the store");
		}
		
		if (first != null && (last != null && last < categories.size())) {
			return new CategoryCollectionRepresentation(categories.subList(
					first - 1, last));
		} else if (first != null && (last == null || last > categories.size())) {
			return new CategoryCollectionRepresentation(categories.subList(
					first - 1, categories.size()));
		}
		
		return new CategoryCollectionRepresentation(categories);
	}

}
