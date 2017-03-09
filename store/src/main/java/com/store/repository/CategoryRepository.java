package com.store.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.domain.Category;
import com.store.domain.Product;


@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category getCategoryByCategoryName(String categoryName);
	
	 @Query("SELECT category FROM Category category where :product in elements(category.products)")
	 public List<Category> findCategoriesByProducts(@Param("product") Product product);

}
