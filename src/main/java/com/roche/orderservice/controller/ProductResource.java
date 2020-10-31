package com.roche.orderservice.controller;

import com.roche.orderservice.exception.ProductNotFoundException;
import com.roche.orderservice.model.Product;
import com.roche.orderservice.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Transactional
public class ProductResource {

    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Get all active products (i.e. not deleted)
     *
     * @return active products
     */
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAllActive();
    }

    /**
     * Get a single product with given SKU
     *
     * @param sku SKU
     * @return single product
     */
    @GetMapping("/products/{sku}")
    public Product getProduct(@PathVariable String sku) {
        Optional<Product> product = productRepository.findById(sku);

        if (!product.isPresent())
            throw new ProductNotFoundException(sku);

        return product.get();
    }

    /**
     * Creates a new product
     *
     * @param product new product to create
     * @return status 201 (CREATED) and uri to new product
     */
    @PostMapping("/products")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product) {
        product.setDeleted(false);
        Product savedProduct = productRepository.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getSku()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Updates an existing product
     *
     * @param product new product values
     * @param sku SKU of existing product
     * @return status 204 (NO_CONTENT)
     */
    @PutMapping("/products/{sku}")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product, @PathVariable String sku) {

        Optional<Product> productOptional = productRepository.findById(sku);

        if (!productOptional.isPresent())
            throw new ProductNotFoundException(sku);

        product.setSku(sku);
        productRepository.save(product);

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes the given product by setting the 'isDeleted' flag
     *
     * @param sku SKU of product to delete
     */
    @DeleteMapping("/products/{sku}")
    public void deleteProduct(@PathVariable String sku) {
        Optional<Product> product = productRepository.findById(sku);
        if (product.isPresent() && !product.get().isDeleted()) {
            productRepository.delete(sku);
        } else {
            throw new ProductNotFoundException(sku);
        }
    }

}
