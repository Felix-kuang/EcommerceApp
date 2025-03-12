package com.example.ecommerce.service;

import com.example.ecommerce.dto.PaymentCallbackDTO;
import com.example.ecommerce.dto.PaymentDTO;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(new BigDecimal("100.00"));

        paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(1L);
        paymentDTO.setAmount(new BigDecimal("100.00"));
    }

    @Test
    void processPayment_SuccessfulPayment() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = paymentService.processPayment(1L, paymentDTO);

        assertEquals("Processing Payment", result);
        assertEquals(OrderStatus.PAYMENT_PENDING, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void processPayment_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, paymentDTO));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void processPayment_AmountMismatch() {
        paymentDTO.setAmount(new BigDecimal("50.00"));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, paymentDTO));

        assertEquals("Amount does not match total price", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void processPayment_OrderNotPending() {
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, paymentDTO));

        assertEquals("Order cannot be paid because it is not in PENDING status", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void handlePaymentCallback_Success() {
        PaymentCallbackDTO callbackDTO = new PaymentCallbackDTO();
        callbackDTO.setOrderId(1L);
        callbackDTO.setStatus("SUCCESS");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        paymentService.handlePaymentCallback(callbackDTO);

        assertEquals(OrderStatus.PAID, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void handlePaymentCallback_Failure() {
        PaymentCallbackDTO callbackDTO = new PaymentCallbackDTO();
        callbackDTO.setOrderId(1L);
        callbackDTO.setStatus("FAILED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        paymentService.handlePaymentCallback(callbackDTO);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void handlePaymentCallback_OrderNotFound() {
        PaymentCallbackDTO callbackDTO = new PaymentCallbackDTO();
        callbackDTO.setOrderId(1L);
        callbackDTO.setStatus("SUCCESS");

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.handlePaymentCallback(callbackDTO));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
