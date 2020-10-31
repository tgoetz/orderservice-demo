package com.roche.orderservice;

import com.roche.orderservice.controller.OrderResource;
import com.roche.orderservice.exception.ProductNotFoundException;
import com.roche.orderservice.model.Order;
import com.roche.orderservice.model.OrderItem;
import com.roche.orderservice.repository.OrderRepository;
import com.roche.orderservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.time.LocalDate.now;
import static java.time.LocalTime.MIDNIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderResourceTest extends BaseTest {

    @Autowired
    private OrderResource orderResource;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        assertThat(orderResource).isNotNull();
    }

    @Test
    public void testFindAllOrders() throws Exception {
        mvc.perform(get("/orders")).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testFindOrdersFromTo() throws Exception {

        // Create 2 new orders
        Order order1 = createOrder(banana, 3);
        Order order2 = createOrder(apple, 5);

        long count = orderRepository.count();
        orderRepository.save(order1);
        orderRepository.save(order2);
        assertThat(orderRepository.count()).isEqualTo(count + 2);

        // Now retrieve orders of today (should be 2)
        String from = LocalDateTime.of(now(), MIDNIGHT).toString();
        String to = LocalDateTime.of(now().plusDays(1), MIDNIGHT).toString();

        mvc.perform(get("/orders").param("from", from).param("to", to)).
                andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPlaceOrder() throws Exception {
        long count = orderRepository.count();
        Order order = createOrder(banana, 5);
        mvc.perform(post("/orders").contentType(APPLICATION_JSON).content(toJson(order)));
        assertThat(orderRepository.count()).isEqualTo(count + 1);
    }

    private Order createOrder(String sku, int amount) {
        Order order = new Order();
        order.setBuyerEmail("tom@decoded.de");
        Set<OrderItem> items = new HashSet<>();
        OrderItem item1 = new OrderItem();
        items.add(item1);
        item1.setProduct(productRepository.findById(sku).orElseThrow(() -> new ProductNotFoundException(sku)));
        item1.setAmount(amount);
        order.setItems(items);

        return order;
    }

}
