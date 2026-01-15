package com.ecommerce.ecommerce.service;


import com.ecommerce.ecommerce.dto.CheckoutPreviewDto;
import com.ecommerce.ecommerce.dto.OrderItemResponseDto;
import com.ecommerce.ecommerce.dto.OrderRequestDto;
import com.ecommerce.ecommerce.dto.OrderResponseDto;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.exceptionHanding.*;
import com.ecommerce.ecommerce.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final UserRepo userRepo;
    private final AddressRepo addressRepo;
    private final OrderRepo orderRepo;
    private final CartItemRepo cartItemRepo;

    public OrderService(UserRepo userRepo, AddressRepo addressRepo, OrderRepo orderRepo, CartItemRepo cartItemRepo) {
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
        this.orderRepo = orderRepo;
        this.cartItemRepo = cartItemRepo;
    }

    public CheckoutPreviewDto previewOrder(Long userId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartEntity cart = user.getCart();

        if (cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        List<OrderItemResponseDto> items = cart.getCartItems()
                .stream()
                .map(ci -> {

                    ProductEntity product = ci.getProduct();

                    if (product.getStockPresent() < ci.getQuantity()) {
                        throw new BusinessException(
                                "Insufficient stock for product: " + product.getProductName()
                        );
                    }

                    return new OrderItemResponseDto(
                            product.getProductId(),
                            product.getProductName(),
                            ci.getQuantity(),
                            product.getPrice()
                    );
                })
                .toList();

        double total = items.stream()
                .mapToDouble(i -> i.price() * i.quantity())
                .sum();

        return new CheckoutPreviewDto(total, items);
    }


    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto dto, Long currentUserId)  {
        UserEntity user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AddressEntity address = addressRepo.findById(dto.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if(!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address does not belong to user");
        }

        CartEntity cart = user.getCart();
        if(cart.getCartItems().isEmpty()){
            throw new ResourceNotFoundException("Cart is empty");
        }

        OrderAddressKey orderAddressKey = new OrderAddressKey();
        orderAddressKey.setStreet(address.getStreet());
        orderAddressKey.setCity(address.getCity());
        orderAddressKey.setState(address.getState());
        orderAddressKey.setZip(address.getZipcode());
        orderAddressKey.setCountry(address.getCountry());

        OrderEntity order = new OrderEntity();
        order.setAddressKey(orderAddressKey);
        order.setUser(user);
        order.setCreatedAt(new Date());

        List<OrderItemEntity> items = new ArrayList<>();
        double total = 0;

        for (CartItemEntity ci : cart.getCartItems()) {
            OrderItemEntity oi = new OrderItemEntity();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
            items.add(oi);

            total += ci.getQuantity() * ci.getProduct().getPrice();
        }

        order.setTotalAmount(total);
        order.setOrderItems(items);

        OrderEntity savedOrder = orderRepo.save(order);

        return buildResponse(savedOrder);
    }

    public OrderResponseDto getOrder(Long orderId,Long userId)  {
        Optional<OrderEntity> order = orderRepo.findById(orderId);
        if(order.isPresent()) {
            if (!order.get().getUser().getId().equals(userId)) {
                throw new ResourceNotFoundException("Order not found for this user");
            }

        }
        return order
                .map(this::buildResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    }

    public Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable) {
        Page<OrderEntity> orders = orderRepo.findByUser_Id(userId, pageable);
        return orders.map(this::buildResponse);
    }

    @Transactional
    public OrderResponseDto updateStatus(Long orderId, OrderStatus value) {
        OrderEntity order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {;
            throw new ResourceNotFoundException("Order not found");
        }

        order.setStatus(value);
        OrderEntity save = orderRepo.save(order);
        return buildResponse(save);
    }

    public OrderResponseDto buildResponse(OrderEntity order) {
        List<OrderItemResponseDto> items = order.getOrderItems()
                .stream()
                .map(oi -> new OrderItemResponseDto(
                        oi.getProduct().getProductId(),
                        oi.getProduct().getProductName(),
                        oi.getQuantity(),
                        oi.getPrice()
                )).toList();

        return new OrderResponseDto(
                order.getOrderId(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );
    }
}

