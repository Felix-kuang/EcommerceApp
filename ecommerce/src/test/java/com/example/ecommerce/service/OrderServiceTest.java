package com.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

public class OrderServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // set mock product
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(25.0);

        // set mock user
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        // set mock cart with item
        cart = new Cart();
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setItems(new ArrayList<>(List.of(cartItem))); // Mutable list
    }

    @Test
    void testCheckout_Success() {
        // Setup
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order()); // Save order and return a mock order

        // Execute
        OrderDTO orderDTO = orderService.checkout("testUser");

        // Assert
        assertNotNull(orderDTO);
        assertEquals(1L, orderDTO.getUserId());
        assertEquals(OrderStatus.PENDING, orderDTO.getStatus()); // Initial status should be PENDING
    }

    @Test
    void testCheckout_CartEmpty() {
        // Setup
        cart.setItems(List.of()); // Set cart to empty
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // Execute & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.checkout("testUser");
        });
        assertEquals("Cart is empty", exception.getMessage());
    }

    @Test
    void testCheckout_UserNotFound() {
        // Setup
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Execute & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.checkout("testUser");
        });
        assertEquals("User not Found", exception.getMessage());
    }
}
