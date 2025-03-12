package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.dto.CartItemsDTO;
import com.example.ecommerce.exception.*;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private CartDTO convertToDTO(Cart cart) {
        List<CartItemsDTO> itemDTOs = cart.getItems().stream().map(item -> {
            CartItemsDTO dto = new CartItemsDTO();
            dto.setProductId(item.getProduct().getId());
            dto.setQuantity(item.getQuantity());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice() * item.getQuantity());
            return dto;
        }).toList();

        double totalPrice = itemDTOs.stream().mapToDouble(CartItemsDTO::getPrice).sum();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setItems(itemDTOs);
        cartDTO.setTotalPrice(totalPrice);
        cartDTO.setUserName(cart.getUser().getUsername());

        return cartDTO;
    }

    public CartDTO getCartForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return convertToDTO(cart);
    }

    public CartDTO addToCart(String username, Long productId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return convertToDTO(cartRepository.save(cart));
    }

    public void updateCartItem(String username, Long productId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingItem.isPresent()) {
            if (quantity == 0) {
                cart.getItems().remove(existingItem.get());
            } else {
                existingItem.get().setQuantity(quantity);
            }
        } else {
            throw new ProductNotInCartException(productId, cart.getId());
        }

        cartRepository.save(cart);
    }

    public void removeFromCart(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException());

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            cart.getItems().remove(existingItem.get());
            cartRepository.save(cart);
        } else {
            throw new ProductNotInCartException(productId, cart.getId());
        }
    }
}
