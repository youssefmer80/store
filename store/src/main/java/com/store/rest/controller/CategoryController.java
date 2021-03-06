package com.store.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

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
@RequestMapping("/store")
public class CategoryController {

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@ApiOperation(value = "get Category By Id", notes = "Returns a single category", response = Category.class)
	@RequestMapping(value = "/category/id/{id}", method = RequestMethod.GET)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Category.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	public Category getCategoryById(@ApiParam(value = "ID of Category to return", required = true) 
									@PathVariable("id") long categoryId) {

		Category category = categoryRepository.findOne(categoryId);
		if (category == null) {
			throw new CategoryNotFoundException("the category id " + categoryId
					+ " is not existed");
		}
		return category;
	}

	@RequestMapping(value = "/category/name/{name}", method = RequestMethod.GET)
	public Category getCategoryByName(@PathVariable("name") String CategoryName) {

		Category category = categoryRepository
				.getCategoryByCategoryName(CategoryName);
		if (category == null) {
			throw new CategoryNotFoundException("the category name "
					+ CategoryName + " is not existed");
		}

		return category;
	}
	
	@ApiOperation(value = "get all the categories", notes = "Returns all the categories", response = CategoryCollectionRepresentation.class)
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public CategoryCollectionRepresentation getAllCategories(
			@ApiParam(value = "first Category to return", required = false) @RequestParam(required = false) Integer first,
			@ApiParam(value = "last Category to return", required = false) @RequestParam(required = false) Integer last) {

		List<Category> categories = categoryRepository.findAll();

		if (categories == null) {
			throw new CategoryNotFoundException(
					"no Categories are available in the store");
		}

		if (first != null
				&& ((last != null && !(first > last) && last < categories
						.size()))) {
			return new CategoryCollectionRepresentation(categories.subList(
					first - 1, last));
		} else if (first != null && first <= categories.size()
				&& (last == null || last > categories.size())) {
			return new CategoryCollectionRepresentation(categories.subList(
					first - 1, categories.size()));
		} else {
			return new CategoryCollectionRepresentation(categories);
		}
	}

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Category> createNewCategory(
			@RequestBody @Valid Category category) {

		if (category.getCategoryCreated() == null) {
			category.setCategoryCreated(LocalDate.now());
		}

		if (category.getCategoryUpdated() == null) {
			category.setCategoryUpdated(LocalDate.now());
		}

		categoryRepository.save(category);

		return new ResponseEntity<Category>(category, null, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(
			@PathVariable("id") long categoryId) {

		Category deletedCategory = categoryRepository.findOne(categoryId);

		if (deletedCategory == null) {
			throw new CategoryNotFoundException("the category id " + categoryId
					+ " is not existed to be deleted");
		}
		
		deletedCategory.setProducts(null);
		
		categoryRepository.delete(deletedCategory);

		return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);

	}

	@RequestMapping(value = "/category/{categoryId}/products/{productIds}", method = RequestMethod.PUT)
	public ResponseEntity<Category> addExistedProductToCategory(
			@PathVariable("categoryId") long catId,
			@PathVariable("productIds") List<String> productIds) {

		Category foundCategory = categoryRepository.findOne(catId);
		if (foundCategory == null) {
			throw new CategoryNotFoundException("the category id " + catId
					+ " is not existed");
		}

		Set<Product> foundCategoryProducts = foundCategory.getProducts();

		for (String productId : productIds) {
			Product foundProduct = productRepository.findOne(Long
					.parseLong(productId));
			if (foundProduct == null) {
				throw new ProductNotFoundException("the product id "
						+ productId + " is not existed");
			}
			foundCategoryProducts.add(foundProduct);
		}

		foundCategory.setProducts(foundCategoryProducts);

		return new ResponseEntity<Category>(foundCategory, null, HttpStatus.OK);

	}

	@RequestMapping(value = "/category/{categoryId}/products", method = RequestMethod.PUT)
	public ResponseEntity<Category> addNewProductsToCategory(
			@PathVariable("categoryId") long catId,
			@RequestBody List<Product> products) {

		Category foundCategory = categoryRepository.findOne(catId);
		if (foundCategory == null) {
			throw new CategoryNotFoundException("the category id " + catId
					+ " is not existed");
		}

		if (products == null) {
			return new ResponseEntity<Category>(foundCategory, null);
		}

		Set<Product> foundCategoryProducts = foundCategory.getProducts();
		for (Product product : products) {
			if (product.getProductCreated() == null) {
				product.setProductCreated(LocalDate.now());
			}

			if (product.getProductLastUpdated() == null) {
				product.setProductLastUpdated(LocalDate.now());
			}
			foundCategoryProducts.add(product);
		}

		foundCategory.setProducts(foundCategoryProducts);

		return new ResponseEntity<Category>(foundCategory, null, HttpStatus.OK);

	}

	@RequestMapping(value = "/category/{categoryId}/products/{productIds}", method = RequestMethod.DELETE)
	public ResponseEntity<Category> deleteProductsFromCategory(
			@PathVariable("categoryId") long categoryId,
			@PathVariable("productIds") List<String> productIds) {

		Category foundCategory = categoryRepository.findOne(categoryId);
		if (foundCategory == null) {
			throw new CategoryNotFoundException("the category id " + categoryId
					+ " is not existed");
		}
		Set<Product> categoriesProducts = foundCategory.getProducts();

		for (String productId : productIds) {

			long prdId = Long.parseLong(productId);
			Product product = productRepository.findOne(prdId);
			if (product == null) {
				throw new ProductNotFoundException("the product id "
						+ productId + " is not existed in the store");
			}
			if (!categoriesProducts.contains(product)) {
				throw new ProductNotFoundException("the product id "
						+ productId
						+ " is not existed in the category whose name "
						+ foundCategory.getCategoryName());
			}

			categoriesProducts.remove(product);
		}

		foundCategory.setProducts(categoriesProducts);

		return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);

	}

}
