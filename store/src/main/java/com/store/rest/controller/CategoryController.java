package com.store.rest.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.domain.Category;
import com.store.domain.Product;
import com.store.exception.CategoryNotFoundException;
import com.store.exception.ProductNotFoundException;
import com.store.repository.CategoryRepository;
import com.store.repository.ProductRepository;
import com.store.rest.representation.CategoryCollectionRepresentation;


@RestController
@Transactional
public class CategoryController {
	
	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;
	
	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;
	
	
	@RequestMapping(value="/category/id/{id}", method= RequestMethod.GET)
	public Category getCategoryById(@PathVariable("id") long categoryId){
		
		Category category = categoryRepository.findOne(categoryId);
		if(category == null){
			throw new CategoryNotFoundException("the category id " + categoryId
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
	
	@RequestMapping(value="/categories",  method=RequestMethod.POST)
	public ResponseEntity<Category> createNewCategory(@RequestBody Category category){
		
		if(category.getCategoryCreated() == null){
			category.setCategoryCreated(LocalDate.now());
		}
		
		if(category.getCategoryUpdated()== null){
			category.setCategoryUpdated(LocalDate.now());
		}
		
		categoryRepository.save(category);
		
		return new ResponseEntity<Category>(category, null, HttpStatus.CREATED);

	}
	
	@RequestMapping(value="/category/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(@PathVariable("id") long categoryId){
		
		Category deletedCategory = categoryRepository.findOne(categoryId);
		
		if(deletedCategory == null){
			throw new CategoryNotFoundException("the category id " + categoryId
					+ " is not existed to be deleted");
		}
		
		categoryRepository.delete(deletedCategory);
		
		return new ResponseEntity<Category>( HttpStatus.NO_CONTENT);
		
	}
	
	@RequestMapping(value="/category/{categoryId}/products/{productIds}", method=RequestMethod.POST)
	public ResponseEntity<Category> addExistedProductToCategory(@PathVariable("categoryId") long catId,
			@PathVariable("productIds") List<String> productIds){
		
		Category foundCategory = categoryRepository.findOne(catId);
		if(foundCategory == null){
			throw new CategoryNotFoundException("the category id " + catId
					+ " is not existed");
		}
		
		Set<Product> foundCategoryProducts = foundCategory.getProducts();
		
		for(String productId:productIds){
			Product foundProduct = productRepository.findOne(Long.parseLong(productId));
			if(foundProduct == null){
					throw new ProductNotFoundException("the product id " + productId
							+ " is not existed");
			}			
			foundCategoryProducts.add(foundProduct);			
		}
		
		foundCategory.setProducts(foundCategoryProducts);
		
		categoryRepository.saveAndFlush(foundCategory);
		
		return new ResponseEntity<Category>(foundCategory,null,HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value="/category/{categoryId}/products", method=RequestMethod.POST)
	public ResponseEntity<Category> addNewProductsToCategory(@PathVariable("categoryId") long catId,
			@RequestBody List<Product> products){
		
		Category foundCategory = categoryRepository.findOne(catId);
		if(foundCategory == null){
			throw new CategoryNotFoundException("the category id " + catId
					+ " is not existed");
		}
		
		if(products == null){
			return new ResponseEntity<Category>(foundCategory,null);
		}
		
		Set<Product> foundCategoryProducts = foundCategory.getProducts();
		for(Product product:products){
			if(product.getProductCreated() == null){
				product.setProductCreated(LocalDate.now());
			}
			
			if(product.getProductLastUpdated() == null){
				product.setProductLastUpdated(LocalDate.now());
			}
			foundCategoryProducts.add(product);
		}
		
		foundCategory.setProducts(foundCategoryProducts);
		
		categoryRepository.saveAndFlush(foundCategory);
		
		return new ResponseEntity<Category>(foundCategory,null,HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value="/category/{categoryId}/product/{productIds}",  method=RequestMethod.DELETE )
	public ResponseEntity<Category> deleteProductFromCategory(@PathVariable("categoryId") long categoryId,
			@PathVariable("productIds") List<String> productIds){
		
		Category foundCategory = categoryRepository.findOne(categoryId);
		if(foundCategory == null){
			throw new CategoryNotFoundException("the category id " + categoryId
					+ " is not existed");
		}
		Set<Product> categoriesProducts = foundCategory.getProducts();
		
		for(String productId:productIds){
			Product foundProduct = productRepository.findOne(Long.parseLong(productId));
			if(foundProduct == null){
					throw new ProductNotFoundException("the product id " + productId
							+ " is not existed");
			}			
			categoriesProducts.remove(foundProduct);			
		}
		
		foundCategory.setProducts(categoriesProducts);
		
		categoryRepository.saveAndFlush(foundCategory);
		
		return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
		
	}

}
