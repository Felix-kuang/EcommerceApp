package com.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        product = new Product();
        product.setId(1L);
        product.setName("Product Test A");
        product.setPrice(50.0);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
    }

    @Test
    void testAddItemToCart_Success() {
        // Setup
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addToCart("testUser", 1L, 2);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals("Product Test A", cart.getItems().get(0).getProduct().getName());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.removeFromCart("testUser", 1L);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetCartForUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        CartDTO resultCartDTO = cartService.getCartForUser("testUser");

        assertNotNull(resultCartDTO);
        assertEquals("testUser", resultCartDTO.getUserName());

        // Validate items in the DTO
        assertNotNull(resultCartDTO.getItems());
        assertTrue(resultCartDTO.getItems().isEmpty()); // Cart harusnya kosong
    }

    @Test
    void testRemoveItemFromCart_UserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty()); // User not found

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", 1L);
        });

        assertEquals("User not Found", exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart_CartNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty()); // cart not found

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", 1L);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart_ProductNotFoundInCart() {
        // Product not found in cart
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", 999L); // Product ID that does not exist
        });

        // Uji bahwa exception message sesuai dengan yang diharapkan
        assertEquals("Product not found in cart with id: null for product id: 999", exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart_CartAlreadyEmpty() {
        // Cart is already empty
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", 1L); // Cart kosong
        });

        // Uji bahwa exception message sesuai dengan yang diharapkan
        assertEquals("Product not found in cart with id: null for product id: 1", exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart_ProductAlreadyRemoved() {
        // Product already removed previously
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // Remove product for the first time
        cartService.removeFromCart("testUser", product.getId());

        // Try to remove again, should throw exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", product.getId());
        });

        // Uji bahwa exception message sesuai dengan yang diharapkan
        assertEquals("Product not found in cart with id: null for product id: " + product.getId(),
                exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart_InvalidProductId() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart("testUser", -1L); // Invalid product ID
        });

        // Uji bahwa exception message sesuai dengan yang diharapkan
        assertEquals("Product not found in cart with id: null for product id: -1", exception.getMessage());
    }
}
