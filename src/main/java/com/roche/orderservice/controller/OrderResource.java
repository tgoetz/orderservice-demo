package com.roche.orderservice.controller;

import com.roche.orderservice.exception.OrderInvalidException;
import com.roche.orderservice.model.Order;
import com.roche.orderservice.model.Product;
import com.roche.orderservice.repository.OrderRepository;
import com.roche.orderservice.repository.OrderSpecification;
import com.roche.orderservice.repository.ProductRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderResource {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderResource(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/orders")
    public List<Order> findOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to) {
        return orderRepository.findAll(new OrderSpecification(from, to));
    }

    @PostMapping("/orders")
    public ResponseEntity<Object> placeOrder(@RequestBody Order order) {

        // Check if products are valid (product exists and is not deleted)
        order.getItems().forEach(item -> {
            String sku = item.getProduct().getSku();
            Optional<Product> product = productRepository.findById(sku);
            if (!product.isPresent() || product.get().isDeleted())
                throw new OrderInvalidException("Product not found with SKU: " + sku);
        });

        Order savedOrder = orderRepository.save(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedOrder.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
