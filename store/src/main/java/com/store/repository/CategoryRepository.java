package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.domain.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category getCategoryByCategoryName(String categoryName);

}
