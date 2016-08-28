package com.store.rest.controller.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.store.domain.Category;
import com.store.domain.Product;
import com.store.exception.CategoryNotFoundException;
import com.store.repository.CategoryRepository;
import com.store.rest.controller.CategoryController;
import com.store.rest.exception.RestErrorHandlerAdvice;


@RunWith(SpringRunner.class)
@WebAppConfiguration
public class CategoryControllerTest {
	
	@InjectMocks
	private CategoryController categoryController;
	
	@Mock
	private CategoryRepository categoryRepository;
		
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
				.setControllerAdvice(new RestErrorHandlerAdvice()).setMessageConverters(new MappingJackson2HttpMessageConverter(),
                        new Jaxb2RootElementHttpMessageConverter()).build();
		
	}
	
	@Test
	public void testCategoryIdNotFound() throws Exception{
		
		String message = "the category id 10 is not existed";
		String url = "/category/id/10";
		
		when(categoryRepository.findOne(1000L)).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/category/id/{id}",10L)
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(10L);
		verifyNoMoreInteractions(categoryRepository);
		 
		
	}
	
	@Test
	public void testCategoryIdFound() throws Exception{
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 17));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		products.add(new Product("sku3","p3"));
		category.setProducts(products);
		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/category/id/{id}",1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.categoryName", is("cat1")))
	            .andExpect(jsonPath("$.products", hasSize(3)))
	            .andExpect(jsonPath("$.categoryCreated",is("2016-08-17")))
	             .andExpect(jsonPath("$.categoryUpdated",is("2016-08-17")));
	
		verify(categoryRepository, times(1)).findOne(1L);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testCategoryNameNotFound() throws Exception{
		
		String message = "the category name cat_10 is not existed";
		String url = "/category/name/cat_10";
		
		when(categoryRepository.getCategoryByCategoryName("cat_10")).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/category/name/{name}","cat_10")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).getCategoryByCategoryName("cat_10");
		verifyNoMoreInteractions(categoryRepository);
		 
		
	}
	
	@Test
	public void testCategoryNameFound() throws Exception{
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		category.setProducts(products);
		
		when(categoryRepository.getCategoryByCategoryName("cat1")).thenReturn(category);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/category/name/{name}","cat1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.categoryName", is("cat1")))
	            .andExpect(jsonPath("$.products", hasSize(2)))
	            .andExpect(jsonPath("$.categoryCreated",is("2016-08-22")))
	             .andExpect(jsonPath("$.categoryUpdated",is("2016-08-22")));
	
		verify(categoryRepository, times(1)).getCategoryByCategoryName("cat1");
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testCategoriesNotFound() throws Exception{
		
		String message = "no Categories are available in the store";
		String url = "/categories";
		
		when(categoryRepository.findAll()).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/categories")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findAll();
		verifyNoMoreInteractions(categoryRepository);
		 
		
	}
	
	@Test
	public void testCategoriesFound() throws Exception{
		
		Category category1 = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products1 = new HashSet<Product>();
		products1.add(new Product("sku1","p1"));
		products1.add(new Product("sku2","p2"));
		category1.setProducts(products1);
		
		Category category2 = new Category("cat2",LocalDate.of(2016, 8, 23));
		Set<Product> products2 = new HashSet<Product>();
		products2.add(new Product("sku3","p3"));
		products2.add(new Product("sku4","p4"));
		category2.setProducts(products2);
		
		Category category3 = new Category("cat3",LocalDate.of(2016, 8, 24));
		Set<Product> products3 = new HashSet<Product>();
		products3.add(new Product("sku5","p5"));
		products3.add(new Product("sku6","p6"));
		products3.add(new Product("sku7","p7"));
		category3.setProducts(products3);
		
		List<Category> categories = new ArrayList<Category>();
		categories.add(category1);
		categories.add(category2);
		categories.add(category3);
		
		
		when(categoryRepository.findAll()).thenReturn(categories);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/categories")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.categories",  hasSize(3)))
		            .andExpect(jsonPath("$.categories[0].categoryName", is("cat1")))
		            .andExpect(jsonPath("$.categories[0].categoryCreated",is("2016-08-22")))
		            .andExpect(jsonPath("$.categories[0].categoryUpdated",is("2016-08-22")))
		            .andExpect(jsonPath("$.categories[1].products", hasSize(2)))
		            .andExpect(jsonPath("$.categories[2].products", hasSize(3)))
		            .andExpect(jsonPath("$.categories[2].categoryName", is("cat3")));
		            
		
		verify(categoryRepository, times(1)).findAll();
		verifyNoMoreInteractions(categoryRepository);
		 
		
	}
	
	
}
