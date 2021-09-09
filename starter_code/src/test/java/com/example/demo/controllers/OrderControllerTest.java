package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final OrderRepository orderRepo = mock(OrderRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController(null, null);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("Tesla model S");
        BigDecimal price = BigDecimal.valueOf(7.19);
        item.setPrice(price);
        item.setDescription("Electric");
        List<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("bob");
        user.setPassword("password");
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepo.findByUsername("bob")).thenReturn(user);
        when(userRepo.findByUsername("bobo")).thenReturn(null);

    }

    @Test
    public void submit_order_Test() {
        ResponseEntity<UserOrder> response = orderController.submit("bob");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }
    
}
