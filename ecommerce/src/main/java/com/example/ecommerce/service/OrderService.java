package com.example.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.exception.CartEmptyException;
import com.example.ecommerce.exception.CartNotFoundException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderDTO checkout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new CartNotFoundException());

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(
                    BigDecimal.valueOf(cartItem.getProduct().getPrice())
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            return orderItem; // Gak set order di sini!
        }).toList();

        orderItems.forEach(orderItem -> orderItem.setOrder(order)); // Set order di luar stream

        order.setItems(orderItems);
        BigDecimal totalPrice = orderItems.stream().map(OrderItem::getPriceAtPurchase).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        order.setTotalPrice(totalPrice);

        order.setOrderDate(LocalDateTime.now()); // FIX: Isi `createdAt`

        orderRepository.save(order); 

        cart.getItems().clear();
        cartRepository.save(cart);

        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getOrderDate());

        List<OrderItemDTO> itemsDTO = order.getItems().stream().map(orderItem -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getName());
            dto.setQuantity((double) orderItem.getQuantity());
            dto.setPrice(orderItem.getPriceAtPurchase());
            return dto;
        }).toList();

        orderDTO.setItems(itemsDTO);

        return orderDTO;
    }
}
