package com.example.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    private Long orderId;
    private String paymentMethod;
    private BigDecimal amount;
}
