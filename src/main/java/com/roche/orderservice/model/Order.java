package com.roche.orderservice.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigDecimal.ZERO;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String buyerEmail;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_order")
    private Set<OrderItem> items = new HashSet<>();

    /**
     * Calculate the order's total price
     * @return total price
     */
    public BigDecimal getTotalPrice() {
        return items.stream().
                map(item -> {
                    BigDecimal price = item.getProduct().getPrice();
                    return price == null ? ZERO : price.multiply(new BigDecimal(item.getAmount()));
                }).
                reduce(ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime creationTime) {
        this.createdAt = creationTime;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }
}
