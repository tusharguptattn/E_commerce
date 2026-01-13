package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CheckoutPreviewDto;
import com.ecommerce.ecommerce.dto.OrderRequestDto;
import com.ecommerce.ecommerce.dto.OrderResponseDto;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import com.ecommerce.ecommerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private SecurityUtil securityUtil;

    public OrderController(OrderService service, SecurityUtil securityUtil) {
        this.service = service;
        this.securityUtil = securityUtil;
    }


    @GetMapping("/checkout/preview")
    public ResponseEntity<CheckoutPreviewDto> preview() {
        return ResponseEntity.ok(
                service.previewOrder(securityUtil.getCurrentUserId())
        );
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId)  {
        return ResponseEntity.ok(service.getOrder(orderId,securityUtil.getCurrentUserId()));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<OrderResponseDto>> getUserOrders(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size)  {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getUserOrders(securityUtil.getCurrentUserId(),pageRequest));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus value) {
        return ResponseEntity.ok(service.updateStatus(orderId, value));
    }
}

