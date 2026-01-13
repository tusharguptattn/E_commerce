package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.OrderResponseDto;
import com.ecommerce.ecommerce.dto.PaymentResponseDto;
import com.ecommerce.ecommerce.dto.PaymentVerifyRequestDto;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController {

    private final PaymentService service;

    private final SecurityUtil securityUtil;

    public PaymentController(PaymentService service,SecurityUtil securityUtil) {
        this.service = service;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDto> initiate(@RequestParam @NotNull Long addressId) {
        return ResponseEntity.ok(
                service.initiatePayment(securityUtil.getCurrentUserId(),addressId)
        );
    }


    @PostMapping("/verify")
    public ResponseEntity<OrderResponseDto> verify(@RequestBody @Valid PaymentVerifyRequestDto dto)  {

        return ResponseEntity.ok(service.verifyPaymentAndCreateOrder(dto,securityUtil.getCurrentUserId()));
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<String> getStatus(@PathVariable @NotNull Long orderId)  {
        return ResponseEntity.ok(service.getPaymentStatus(orderId));
    }
}
