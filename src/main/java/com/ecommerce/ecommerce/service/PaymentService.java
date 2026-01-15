package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.exceptionHanding.*;
import com.ecommerce.ecommerce.repository.AddressRepo;
import com.ecommerce.ecommerce.repository.OrderRepo;
import com.ecommerce.ecommerce.repository.PaymentRepo;
import com.ecommerce.ecommerce.repository.UserRepo;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final CartService cartService;
    private final SecurityUtil securityUtil;
    @Autowired
    private OrderService orderService;

    private final UserRepo userRepo;
    private final AddressRepo addressRepo;

    public PaymentService(PaymentRepo paymentRepo, OrderRepo orderRepo, CartService cartService, SecurityUtil securityUtil, UserRepo userRepo, AddressRepo addressRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
        this.cartService=cartService;
        this.securityUtil=securityUtil;
        this.userRepo=userRepo;
        this.addressRepo=addressRepo;
    }


    @Transactional
    public PaymentResponseDto initiatePayment(Long userId,Long addressId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AddressEntity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        CartEntity cart = user.getCart();
        if (cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(new Date());

        OrderAddressKey key = new OrderAddressKey();
                key.setStreet(address.getStreet());
        key.setCity(address.getCity());
        key.setState(address.getState());
        key.setZip(address.getZipcode());
        key.setCountry(address.getCountry());

        order.setAddressKey(key);

        double total = 0;
        List<OrderItemEntity> items = new ArrayList<>();

        for (CartItemEntity ci : cart.getCartItems()) {
            if (ci.getProduct().getStockPresent() < ci.getQuantity()) {
                throw new BusinessException(
                        "Insufficient stock for product: " + ci.getProduct().getProductName()
                );
            }

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

        OrderEntity savedOrder = orderRepo.save(order); // 🔑 ORDER ID CREATED


        // 2️⃣ Create PAYMENT
        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(savedOrder);
        payment.setAmount(total);
        payment.setStatus("INITIATED");
        payment.setTransactionId("TXN-" + System.currentTimeMillis());

        PaymentEntity savedPayment = paymentRepo.save(payment); // 🔑 PAYMENT ID CREATED

        return new PaymentResponseDto(
                savedPayment.getPaymentId(),
                savedOrder.getOrderId(),
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                savedPayment.getTransactionId()
        );

    }





    @Transactional
    public OrderResponseDto verifyPaymentAndCreateOrder(
            PaymentVerifyRequestDto dto,Long userId) {


        PaymentEntity payment = paymentRepo.findById(dto.paymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        OrderEntity order = payment.getOrder();

        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Order does not belong to user");
        }

        if (!payment.getTransactionId().equals(dto.transactionId())) {
            throw new ResourceNotFoundException("Invalid transaction");
        }


        if (dto.status()) {
            // 🔥 FINAL STOCK DECREMENT (ATOMIC)
            for (OrderItemEntity item : order.getOrderItems()) {
                ProductEntity product = item.getProduct();

                if (product.getStockPresent() < item.getQuantity()) {
                    throw new BusinessException(
                            "Insufficient stock for product: " + product.getProductName()
                    );
                }

                product.setStockPresent(
                        product.getStockPresent() - item.getQuantity()
                );
            }
            payment.setStatus("SUCCESS");
            order.setStatus(OrderStatus.CONFIRMED);
            cartService.clearCart(userId);
        }
        else{
            payment.setStatus("FAILED");
            order.setStatus(OrderStatus.CANCELLED);

            // 🔥 CLEANUP FAILED ORDER
            paymentRepo.delete(payment);
            orderRepo.delete(order);
            throw new ResourceNotFoundException("Payment failed, order cancelled");
        }



        paymentRepo.save(payment);
        orderRepo.save(order);

        return orderService.buildResponse(order);
    }




    // Step 3: Check Payment Status
    public String getPaymentStatus(Long orderId)  {

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        PaymentEntity payment = order.getPayment();

        return payment == null
                ? "NO PAYMENT FOUND"
                : payment.getStatus();
    }
}

