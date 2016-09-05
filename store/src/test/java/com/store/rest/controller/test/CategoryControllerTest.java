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
import com.store.exception.ProductNotFoundException;
import com.store.repository.CategoryRepository;
import com.store.repository.ProductRepository;
import com.store.rest.controller.CategoryController;
import com.store.rest.exception.RestErrorHandlerAdvice;
import com.store.util.test.TestUtil;


@RunWith(SpringRunner.class)
@WebAppConfiguration
public class CategoryControllerTest {
	
	@InjectMocks
	private CategoryController categoryController;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductRepository productRepository;
		
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
	
	@Test
	public void testCreateNewCategory() throws Exception{
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		category.setProducts(products);
		
		when(categoryRepository.save(category)).thenReturn(category);
		this.mockMvc
		.perform(
				MockMvcRequestBuilders.post("/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.asJsonString(category)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.categoryName", is("cat1")))
		.andExpect(jsonPath("$.products[0].productName", is("p1")));

		verify(categoryRepository, times(1)).save(category);
		verifyNoMoreInteractions(categoryRepository);
	 
		
	}
	
	@Test
	public void testDeleteCategoryNotFound() throws Exception{
		
		String message = "category Id 25 is not available in the store to delete";
		String url = "/category/25";
		
		when(categoryRepository.findOne(25L)).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", 25L)
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(25L);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testDeleteCategoryFound() throws Exception{
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		category.setProducts(products);
		category.setCategoryId(1L);

		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", 1L)
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNoContent());
		          
		
		verify(categoryRepository, times(1)).findOne(1L);
		verify(categoryRepository, times(1)).delete(category);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testaddExistedProductToCategoryNotFound() throws Exception{
		
		String message = "category Id 25 is not available in the store to add product to";
		String url = "/category/25/products/23,24,25";
		
		when(categoryRepository.findOne(25L)).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products/{productIds}", 25L,"23,24,25")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(25L);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testaddNontExistedProductToCategory() throws Exception{
		
		String message = "product Id 23 is not available in the store to add it to category";
		String url = "/category/1/products/23";
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		category.setProducts(products);
		category.setCategoryId(1L);
		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		when(productRepository.findOne(23L)).thenThrow(new ProductNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products/{productIds}", 1L,"23")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).findOne(23L);
		verifyNoMoreInteractions(productRepository);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testaddExistedProductToCategory() throws Exception{
		
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		category.setCategoryId(1L);
		
		Product product = new Product(1L,"sku_1", "p1",LocalDate.now(), LocalDate.now());
		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		when(productRepository.findOne(1L)).thenReturn(product);
		
		Set<Product> products = new HashSet<Product>();
		products.add(product);
		category.setProducts(products);
		
		when(categoryRepository.save(category)).thenReturn(category);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products/{productIds}", 1L,"1")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		             .andExpect(status().isOk())
		            .andExpect(jsonPath("$.categoryName", is("cat1")))
		            .andExpect(jsonPath("$.products[0].productSku", is("sku_1")))
		            .andExpect(jsonPath("$.products[0].productName", is("p1")));
		
		verify(categoryRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).findOne(1L);
		verify(categoryRepository, times(1)).saveAndFlush(category);
		verifyNoMoreInteractions(productRepository);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testAddNewProductsToCategoryNotFound() throws Exception{
		
		String message = "category Id 25 is not available in the store to add product to";
		String url = "/category/25/products";
		
		when(categoryRepository.findOne(25L)).thenThrow(new CategoryNotFoundException(message));
		
		Product product = new Product("SKU_1", "Product1");
		List<Product> products = new ArrayList<Product>();
		products.add(product);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products", 25L)
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(TestUtil.asJsonString(products))
		            .accept(MediaType.APPLICATION_JSON))		           
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(25L);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	@Test
	public void testAddNewProductsToCategory() throws Exception{
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		category.setCategoryId(1L);
		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		
		Product product1 = new Product("SKU_1", "Product1");
		Product product2 = new Product("SKU_2", "Product2");
		List<Product> products = new ArrayList<Product>();
		products.add(product1);
		products.add(product2);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/products", 1L)
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(TestUtil.asJsonString(products))
		            .accept(MediaType.APPLICATION_JSON))		           
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.categoryName", is("cat1")))
		            .andExpect(jsonPath("$.products[0].productSku", is("SKU_1")))
		            .andExpect(jsonPath("$.products[0].productName", is("Product1")))
		            .andExpect(jsonPath("$.products[1].productSku", is("SKU_2")))
		            .andExpect(jsonPath("$.products[1].productName", is("Product2")));
		
		verify(categoryRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).save(product1);
		verify(productRepository, times(1)).save(product2);
		verify(categoryRepository, times(1)).saveAndFlush(category);
		verifyNoMoreInteractions(productRepository);
		verifyNoMoreInteractions(categoryRepository);
		
	}
	
	
	@Test
	public void testDeleteProductFromCategoryNotFound() throws Exception{
		
		String message = "category Id 25 is not available in the store to delete products from";
		String url = "/category/25/products/12,13,15";
		
		when(categoryRepository.findOne(25L)).thenThrow(new CategoryNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/category/{categoryId}/products/{productIds}", 25L,"12,13,15")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(25L);
		verifyNoMoreInteractions(categoryRepository);
	}
	
	@Test
	public void testDeleteNonExistedProductsFromCategory() throws Exception{
		
		String message = "product Id 12 is not available in the store to add it to category";
		String url = "/category/1/products/12,24,23";
		
		Category category = new Category("cat1",LocalDate.of(2016, 8, 22));
		Set<Product> products = new HashSet<Product>();
		products.add(new Product("sku1","p1"));
		products.add(new Product("sku2","p2"));
		category.setProducts(products);
		category.setCategoryId(1L);
		
		when(categoryRepository.findOne(1L)).thenReturn(category);
		when(productRepository.findOne(12L)).thenThrow(new ProductNotFoundException(message));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/category/{categoryId}/products/{productIds}", 1L,"12,24,23")
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("message", is(message)))
		            .andExpect(jsonPath("url", is(url)));
		
		verify(categoryRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).findOne(12L);
		verifyNoMoreInteractions(productRepository);
		verifyNoMoreInteractions(categoryRepository);
	}
	
	
}
