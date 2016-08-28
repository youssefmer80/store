package com.store.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.store.domain.Category;
import com.store.repository.CategoryRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	
	@Test
	public void FindByIDShouldReturnCategory() {
		
		Category category1 = new Category("cat1", LocalDate.now().minusDays(3));
		Category category2 = new Category("cat2", LocalDate.now().minusDays(2));
		
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		
		Category cat1 = categoryRepository.findOne(category1.getCategoryId());
		assertThat(cat1.getCategoryName()).isEqualTo("cat1");
		assertThat(cat1.getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(-3);
		assertThat(cat1.getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(-3);

		Category cat2 = categoryRepository.findOne(category2.getCategoryId());
		assertThat(cat2.getCategoryName()).isEqualTo("cat2");
		assertThat(cat2.getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(-2);
		assertThat(cat2.getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(-2);
		
		Category cat3 = categoryRepository.findOne(100L);
		assertThat(cat3).isEqualTo(null);
	}
	
	@Test
	public void FindByNameShouldReturnCategory() {
		
		Category category1 = new Category("cat3", LocalDate.now().minusDays(1));
		Category category2 = new Category("cat4", LocalDate.now());
		
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		
		Category cat1 = categoryRepository.getCategoryByCategoryName("cat3");
		assertThat(cat1.getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(-1);
		assertThat(cat1.getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(-1);

		Category cat2 = categoryRepository.getCategoryByCategoryName("cat4");
		assertThat(cat2.getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(0);
		assertThat(cat2.getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(0);
		
		Category cat3 = categoryRepository.getCategoryByCategoryName("cat7");
		assertThat(cat3).isEqualTo(null);
	}
	
	@Test
	public void findAllShouldReturnCategories(){
		
		Category category1 = new Category("cat1", LocalDate.now().minusDays(3));
		Category category2 = new Category("cat2", LocalDate.now().minusDays(2));
		Category category3 = new Category("cat3", LocalDate.now().minusDays(1));
		Category category4 = new Category("cat4", LocalDate.now());
		
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		categoryRepository.save(category3);
		categoryRepository.save(category4);
		
		List<Category> categories = categoryRepository.findAll();
		
		assertThat(categories).isNotEqualTo(null);
		assertThat(categories.size()).isEqualTo(4);
		
		
		assertThat(categories.get(0).getCategoryName()).isEqualTo("cat1");
		assertThat(categories.get(0).getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(-3);
		
		assertThat(categories.get(1).getCategoryName()).isEqualTo("cat2");
		assertThat(categories.get(1).getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(-2);
		
		assertThat(categories.get(2).getCategoryName()).isEqualTo("cat3");
		assertThat(categories.get(2).getCategoryUpdated().compareTo(LocalDate.now())).isEqualTo(-1);
		
		assertThat(categories.get(3).getCategoryName()).isEqualTo("cat4");
		assertThat(categories.get(3).getCategoryCreated().compareTo(LocalDate.now())).isEqualTo(0);
		
		
	}

}
