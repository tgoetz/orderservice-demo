package com.roche.orderservice;

import com.roche.orderservice.controller.ProductResource;
import com.roche.orderservice.model.Product;
import com.roche.orderservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductResourceTest extends BaseTest {

	@Autowired
	private ProductResource productResource;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {
		assertThat(productResource).isNotNull();
	}

	@Test
	public void testGetProducts() throws Exception {
		mvc.perform(get("/products")).
				andDo(print()).
				andExpect(status().isOk()).
				andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
				andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void testGetProductBySku() throws Exception {
		mvc.perform(get("/products/" + banana)).
				andExpect(status().isOk()).
				andExpect(jsonPath("name", is("Banana"))).
				andExpect(jsonPath("price", is(1.99)));
	}

	@Test
	public void testGetUnknownProduct() throws Exception {
		mvc.perform(get("/products/12345")).andExpect(status().isNotFound());
	}

	@Test
	public void testCreateProduct() throws Exception {

		int amount = productResource.getAllProducts().size();

		Product product = new Product();
		product.setSku("1234567");
		product.setName("Cherry");
		product.setPrice(new BigDecimal("1.29"));

		mvc.perform(post("/products").
				contentType(APPLICATION_JSON).content(toJson(product)));

		assertThat(productResource.getAllProducts()).size().isEqualTo(amount + 1);
	}

	@Test
	public void testCreateInvalidProduct() throws Exception {

		// Create product without SKU
		Product product = new Product();
		product.setName("Cherry");
		product.setPrice(new BigDecimal("1.29"));

		mvc.perform(post("/products").contentType(APPLICATION_JSON).content(toJson(product))).
				andDo(print());

	}

	@Test
	public void testUpdateProduct() throws Exception {

		Product product = productResource.getProduct(banana);
		product.setPrice(new BigDecimal("1.89"));

		mvc.perform(put("/products/" + banana).
				contentType(APPLICATION_JSON).content(toJson(product)));

		product = productResource.getProduct(banana);
		assertThat(product.getPrice()).isEqualTo(new BigDecimal("1.89"));
	}

	@Test
	public void testDeleteProduct() throws Exception {

		int amount = productResource.getAllProducts().size();

		mvc.perform(delete("/products/" + banana));
		assertThat(productRepository.findAllActive()).hasSize(amount - 1);

		Optional<Product> product = productRepository.findById(banana);
		assertThat(product).isPresent();
		assertThat(product.get().isDeleted()).isTrue();
	}

	@Test
	public void testDeleteUnknownProduct() throws Exception {
		mvc.perform(delete("/products/1234567")).andExpect(status().isNotFound());
	}

}
