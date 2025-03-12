package com.example.ecommerce.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.PaymentCallbackDTO;
import com.example.ecommerce.dto.PaymentDTO;
import com.example.ecommerce.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
        try{
            System.out.println("Test");
            String response = paymentService.processPayment(paymentDTO.getOrderId(), paymentDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/callback")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> paymentCallback(@RequestBody PaymentCallbackDTO callbackDTO){
        try{
            paymentService.handlePaymentCallback(callbackDTO);
            return ResponseEntity.ok("Callback processed");
        } catch (RuntimeException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
}
