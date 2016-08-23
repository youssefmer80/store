package com.store.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.store.domain.Product;
import com.store.repository.ProductRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

	
	@Autowired
	private ProductRepository productRepository;

	@Test
	public void FindByIDShouldReturnProduct() {
		
		productRepository.save(new Product("Sku1", "p1"));
		productRepository.save(new Product("Sku2", "p2"));

		Product product1 = productRepository.findOne(1L);
		assertThat(product1.getProductName()).isEqualTo("p1");
		assertThat(product1.getProductSku()).isEqualTo("Sku1");

		Product product2 = productRepository.findOne(2L);
		assertThat(product2.getProductName()).isEqualTo("p2");
		assertThat(product2.getProductSku()).isEqualTo("Sku2");
		
		Product product3 = productRepository.findOne(100L);
		assertThat(product3).isEqualTo(null);

	}

	@Test
	public void FindByNameShouldReturnProduct() {

		productRepository.save(new Product("SkuA", "ProductA"));
		productRepository.save(new Product("SkuB", "ProductB"));
		productRepository.save(new Product("SkuC", "ProductC"));
		productRepository.save(new  Product("SkuD", "ProductD",LocalDate.now().minusDays(1),LocalDate.now()));

		Product product1 = productRepository.findByProductName("ProductB");
		assertThat(product1.getProductLastUpdated().compareTo(LocalDate.now())).isEqualTo(0);
		assertThat(product1.getProductSku()).isEqualTo("SkuB");

		Product product2 = productRepository.findByProductName("ProductD");
		assertThat(product2.getProductCreated().compareTo(LocalDate.now())).isEqualTo(-1);
		assertThat(product2.getProductLastUpdated().compareTo(LocalDate.now())).isEqualTo(0);
		assertThat(product2.getProductSku()).isEqualTo("SkuD");
		
		Product product3 = productRepository.findByProductName("ProductF");
		assertThat(product3).isEqualTo(null);
	}
	
	@Test
	public void FindBySKUShouldReturnProduct() {

		productRepository.save(new Product("Sku11", "Product11"));
		productRepository.save(new Product("Sku12", "Product12",LocalDate.now().minusDays(3),LocalDate.now().minusDays(1)));
		productRepository.save(new Product("Sku13", "Product13"));


		Product product1 = productRepository.findByProductSku("Sku11");
		assertThat(product1.getProductLastUpdated().compareTo(LocalDate.now())).isEqualTo(0);
		assertThat(product1.getProductName()).isEqualTo("Product11");

		Product product2 = productRepository.findByProductSku("Sku12");
		assertThat(product2.getProductCreated().compareTo(LocalDate.now())).isEqualTo(-3);
		assertThat(product2.getProductLastUpdated().compareTo(LocalDate.now())).isEqualTo(-1);
		assertThat(product2.getProductName()).isEqualTo("Product12");
		
		Product product3 = productRepository.findByProductSku("Sku180");
		assertThat(product3).isEqualTo(null);

	}
	
	@Test
	public void FindAllShouldReturnProducts() {

		productRepository.save(new Product("Sku11", "Product11"));
		productRepository.save(new Product("Sku12", "Product12",LocalDate.now().minusDays(5) ,LocalDate.now()));
		productRepository.save(new Product("Sku13", "Product13"));
		productRepository.save(new Product("Sku14", "Product14"));

		List<Product>  products = productRepository.findAll();
		
		assertThat(products.size()).isEqualTo(4);
		
		assertThat(products.get(0).getProductSku()).isEqualTo("Sku11");
		assertThat(products.get(0).getProductName()).isEqualTo("Product11");
		
		assertThat(products.get(1).getProductCreated().compareTo(LocalDate.now())).isEqualTo(-5);
		assertThat(products.get(1).getProductLastUpdated().compareTo(LocalDate.now())).isEqualTo(0);
		
		assertThat(products.get(2).getProductSku()).isEqualTo("Sku13");
		assertThat(products.get(2).getProductName()).isEqualTo("Product13");



	}

}
