package com.roche.orderservice.repository;

import com.roche.orderservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Find all active products, i.e. those that are not deleted
     * @return list of active products
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    List<Product> findAllActive();

    /**
     * Deletes a product by setting a flag
     * @param sku SKU of product to delete
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.sku = :sku")
    void delete(String sku);

}
