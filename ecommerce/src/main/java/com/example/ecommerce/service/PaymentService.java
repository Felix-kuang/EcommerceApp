package com.example.ecommerce.service;

import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.PaymentCallbackDTO;
import com.example.ecommerce.dto.PaymentDTO;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public String processPayment(Long orderId, PaymentDTO paymentDTO) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOptional.get();

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new RuntimeException("Order cannot be paid because it is not in PENDING status");
        }

        if (order.getTotalPrice().compareTo(paymentDTO.getAmount()) != 0) {
            throw new RuntimeException("Amount does not match total price");
        }

        if (processPaymentSimulated()) {
            order.setStatus(OrderStatus.PAYMENT_PENDING);
            orderRepository.save(order);
            return "Processing Payment";
        }

        return "Payment Failed";
    }

    private boolean processPaymentSimulated() {
        return Math.random() > 0.2;
    }

    @Transactional
    public void handlePaymentCallback(PaymentCallbackDTO callbackDTO) {
        Optional<Order> orderOptional = orderRepository.findById(callbackDTO.getOrderId());

        if (!orderOptional.isPresent()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOptional.get();

        if (callbackDTO.getStatus().equalsIgnoreCase("SUCCESS")) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
    }
}
