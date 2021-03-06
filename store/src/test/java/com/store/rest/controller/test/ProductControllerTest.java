package com.store.rest.controller.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.store.domain.Product;
import com.store.exception.ProductNotFoundException;
import com.store.repository.ProductRepository;
import com.store.rest.controller.ProductController;
import com.store.rest.exception.RestErrorHandlerAdvice;
import com.store.util.test.TestUtil;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class ProductControllerTest {


	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductRepository productRepository;

	private MockMvc mockMvc;


	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(productController)
				.setControllerAdvice(new RestErrorHandlerAdvice())
				.setMessageConverters(
						new MappingJackson2HttpMessageConverter(),
						new Jaxb2RootElementHttpMessageConverter()).build();

	}

	@Test
	public void testGetProductIdNotFound() throws Exception {

		String message = "the product id 10 is not existed";
		String url = "/store/product/id/10";
		when(productRepository.findOne(10L)).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/store/product/id/{id}", 10L)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findOne(10L);
		verifyNoMoreInteractions(productRepository);
	}

	@Test
	public void testGetProductIdFound() throws Exception {

		Product product = new Product("SKU_A", "ProductA", LocalDate.of(2016,
				8, 14), LocalDate.of(2016, 8, 17));

		when(productRepository.findOne(1L)).thenReturn(product);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/store/product/id/{id}", 1L)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("productSku", is("SKU_A")))
				.andExpect(jsonPath("productName", is("ProductA")));
		// .andExpect(
		// content()
		// .string("{\"productSku\":\"SKU_C\",\"productName\":\"ProductC\",\"productLastUpdated\":{\"year\":2016,\"month\":\"AUGUST\",\"dayOfMonth\":14,\"dayOfWeek\":\"SUNDAY\",\"era\":\"CE\",\"dayOfYear\":227,\"leapYear\":true,\"monthValue\":8,\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"id\":1}"));

		verify(productRepository, times(1)).findOne(1L);
		verifyNoMoreInteractions(productRepository);

	}


	@Test
	public void testGetProductSkuNotFound() throws Exception {

		String message = "the product sku SKU_ProA is not existed";
		String url = "/store/product/sku/SKU_ProA";
		when(productRepository.findByProductSku("SKU_ProA")).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/store/product/sku/{sku}", "SKU_ProA")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findByProductSku("SKU_ProA");
		verifyNoMoreInteractions(productRepository);
	}

	@Test
	public void testGetProductSkuFound() throws Exception {

		Product product = new Product("SKU_B", "ProductB", LocalDate.of(2016,
				8, 16), LocalDate.of(2016, 8, 17));

		when(productRepository.findByProductSku("SKU_B")).thenReturn(product);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/store/product/sku/{sku}", "SKU_B")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("productSku", is("SKU_B")))
				.andExpect(jsonPath("productName", is("ProductB")));

		verify(productRepository, times(1)).findByProductSku("SKU_B");
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testGetAllProductsNotFound() throws Exception {

		String message = "no Products are existed in the store";
		String url = "/store/products";
		when(productRepository.findAll()).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/store/products")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findAll();
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testGetAllProductsFound() throws Exception {

		Product product1 = new Product(1L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.of(2016, 8, 17));
		Product product2 = new Product(2L, "SKU_B", "ProductB", LocalDate.of(
				2016, 8, 16), LocalDate.of(2016, 8, 17));
		Product product3 = new Product(3L, "SKU_C", "ProductC", LocalDate.of(
				2016, 8, 16), LocalDate.of(2016, 8, 17));
		Product product4 = new Product(4L, "SKU_F", "ProductF", LocalDate.of(
				2016, 8, 16), LocalDate.of(2016, 8, 17));
		;

		List<Product> products = new ArrayList<Product>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);

		when(productRepository.findAll()).thenReturn(products);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/store/products").accept(
						MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.products", hasSize(4)))
				.andExpect(jsonPath("$.products[0].productSku", is("SKU_A")))
				.andExpect(
						jsonPath("$.products[0].productName", is("ProductA")))
				.andExpect(jsonPath("$.products[3].productSku", is("SKU_F")))
				.andExpect(
						jsonPath("$.products[3].productName", is("ProductF")));

		verify(productRepository, times(1)).findAll();
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testUpdateProductByIdNotExisted() throws Exception {
		String message = "the product Id 25 is not existed to update it";
		String url = "/store/product/id/25";
		Product product = new Product(25L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());

		when(productRepository.findOne(25L)).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.put("/store/product/id/{id}", 25L)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findOne(25L);
		verify(productRepository, times(0)).saveAndFlush(product);

	}
	
	@Test
	public void testUpdateProductByIdNotValid() throws Exception {
		
		Product product = new Product(1L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());
		
		when(productRepository.findOne(1L)).thenReturn(product);
		
		product.setProductSku(TestUtil.createStringWithLength(46));
		product.setProductName(TestUtil.createStringWithLength(50));

		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/store/product/id/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.asJsonString(product)))
                .andExpect(status().isBadRequest());

		verify(productRepository, times(0)).findOne(1L);
		verify(productRepository, times(0)).save(product);
		verifyNoMoreInteractions(productRepository);

    }

	@Test
	public void testUpdateProductByIdExisted() throws Exception {

		Product product = new Product(1L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());

		Product updatedProduct = new Product(1L, "SKU_A", "Product_A1",
				LocalDate.of(2016, 8, 16), LocalDate.now());

		when(productRepository.findOne(1L)).thenReturn(product);

		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/store/product/id/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						// .accept((MediaType.APPLICATION_JSON))
						.content(TestUtil.asJsonString(updatedProduct)))
				.andExpect(status().isOk());
		// .andExpect(content().mimeType(contentType))
		// .andExpect(content().contentTypeCompatibleWith(contentType))
		// .andExpect(jsonPath("$.productSku", is("SKU_A")))
		// .andExpect(jsonPath("$.productName", is("Product_A1")));

		verify(productRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).saveAndFlush(updatedProduct);
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testUpdateProductBySkuNotExisted() throws Exception {
		String message = "the product sku SKU_111 is not existed to update it";
		String url = "/store/product/sku/SKU_111";
		Product product = new Product(25L, "SKU_111", "Product111",
				LocalDate.of(2016, 8, 16), LocalDate.now());

		when(productRepository.findByProductSku("SKU_111")).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.put("/store/product/sku/{sku}", "SKU_111")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findByProductSku("SKU_111");
		verify(productRepository, times(0)).saveAndFlush(product);
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testUpdateProductBySkuExisted() throws Exception {

		Product product = new Product(25L, "SKU_1", "Product1", LocalDate.of(
				2016, 8, 16), LocalDate.now());

		when(productRepository.findByProductSku("SKU_1")).thenReturn(product);

		Product updatedProduct = new Product(1L, "SKU_1", "Product_V1",
				LocalDate.of(2016, 8, 16), LocalDate.now());

		this.mockMvc.perform(
				MockMvcRequestBuilders.put("/store/product/sku/{sku}", "SKU_1")
						.contentType(MediaType.APPLICATION_JSON)
						// .accept((MediaType.APPLICATION_JSON))
						.content(TestUtil.asJsonString(updatedProduct)))
				.andExpect(status().isOk());
		// .andExpect(content().mimeType(contentType))
		// .andExpect(content().contentTypeCompatibleWith(contentType))
		// .andExpect(jsonPath("$.productSku", is("SKU_A")))
		// .andExpect(jsonPath("$.productName", is("Product_A1")));

		verify(productRepository, times(1)).findByProductSku("SKU_1");
		verify(productRepository, times(1)).saveAndFlush(updatedProduct);
		verifyNoMoreInteractions(productRepository);

	}

	@Test
	public void testCreateNewProduct() throws Exception {
		Product product = new Product("SKU_1", "Product1");
		when(productRepository.save(product)).thenReturn(product);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/store/products")
								.contentType(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.productSku", is("SKU_1")))
				.andExpect(jsonPath("$.productName", is("Product1")));

		verify(productRepository, times(1)).save(product);
		verifyNoMoreInteractions(productRepository);
	}
	
	@Test
	public void testCreateNewNonValidProduct() throws Exception {
		
		Product product = new Product();
		product.setProductSku(TestUtil.createStringWithLength(46));
		product.setProductName(TestUtil.createStringWithLength(50));
		
//		when(productRepository.save(product)).thenThrow(
//				new MethodArgumentNotValidException(new MethodParameter(null), null));

		mockMvc.perform(MockMvcRequestBuilders.post("/store/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.asJsonString(product))
        )
                .andExpect(status().isBadRequest());
                //.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
//                .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("productSku", "productName")))
//                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
//                        "The maximum length of the description is 500 characters.",
//                        "The maximum length of the title is 100 characters."
//                )));


 
		verify(productRepository, times(0)).save(product);
		verifyNoMoreInteractions(productRepository);
    }


	@Test
	public void testDeleteProductByIdNotExisted() throws Exception {

		String message = "the product Id 25 is not existed to delete it";
		String url = "/store/product/id/25";
		Product product = new Product(25L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());

		when(productRepository.findOne(25L)).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.delete("/store/product/id/{id}", 25L)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findOne(25L);
		verify(productRepository, times(0)).delete(product);

	}

	@Test
	public void testDeleteProductByIdFound() throws Exception {

		Product product = new Product(1L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());


		when(productRepository.findOne(1L)).thenReturn(product);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.delete("/store/product/id/{id}", 1L)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNoContent());


		verify(productRepository, times(1)).findOne(1L);
		verify(productRepository, times(1)).delete(product);

	}
	
	@Test
	public void testDeleteProductBySKUNotFound() throws Exception {

		String message = "the product sku SKU_A is not existed to delete it";
		String url = "/store/product/sku/SKU_A";
		Product product = new Product(25L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());

		when(productRepository.findByProductSku("SKU_A")).thenThrow(
				new ProductNotFoundException(message));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.delete("/store/product/sku/{sku}", "SKU_A")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("message", is(message)))
				.andExpect(jsonPath("url", is(url)));

		verify(productRepository, times(1)).findByProductSku("SKU_A");
		verify(productRepository, times(0)).delete(product);

	}

	@Test
	public void testDeleteProductBySKUFound() throws Exception {

		Product product = new Product(1L, "SKU_A", "ProductA", LocalDate.of(
				2016, 8, 16), LocalDate.now());


		when(productRepository.findByProductSku("SKU_A")).thenReturn(product);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.delete("/store/product/sku/{sku}", "SKU_A")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(TestUtil.asJsonString(product)))
				.andExpect(status().isNoContent());


		verify(productRepository, times(1)).findByProductSku("SKU_A");
		verify(productRepository, times(1)).delete(product);

	}

}
