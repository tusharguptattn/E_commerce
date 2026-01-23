package com.ecommerce.ecommerce.service;


import com.ecommerce.ecommerce.dto.OrderProductDtoForSeller;
import com.ecommerce.ecommerce.dto.OrderProductDtoResponse;
import com.ecommerce.ecommerce.dto.OrderResponseDto;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.entity.embeddable.OrderAddress;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.exceptionHanding.*;
import com.ecommerce.ecommerce.repository.*;
import com.ecommerce.ecommerce.securityConfig.SecurityUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class OrderService {

  AddressRepo addressRepo;
  OrderRepo orderRepo;
  CartItemRepo cartItemRepo;
  SecurityUtil securityUtil;
  CartRepo cartRepo;
  CustomerRepo customerRepo;
  ProductVariationRepo productVariationRepo;
  OrderProductRepo orderProductRepo;
  OrderStatusRepo orderStatusRepo;
  ProductRepo productRepo;


  @Transactional
  public void addAllCartToOrder(Long addressID) {
    Long userId = securityUtil.getCurrentUserId();
    CartEntity cart = cartRepo.findByCustomer_User_Id(userId);

    if (cart == null) {
      throw new BadRequest("Cart is null");
    }
    CustomerEntity customerEntity = customerRepo.findByUserId(userId)
        .orElseThrow(() -> new BadRequest("No customer found with this id"));

    BigDecimal totalAmount = BigDecimal.ZERO;

    List<OrderProduct> orderItems = new ArrayList<>();

    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setCustomer(customerEntity);
    orderEntity.setPaymentMethod("Online");

    AddressEntity addressEntity = addressRepo.findAllAddressesByUserId(userId).stream()
        .filter(a -> a.getAddressId().equals(addressID)).findFirst()
        .orElseThrow(() -> new BadRequest("Address not found"));

    OrderAddress orderAddress = new OrderAddress();
    orderAddress.setCity(addressEntity.getCity());
    orderAddress.setCountry(addressEntity.getCountry());
    orderAddress.setLabel(addressEntity.getLabel());
    orderAddress.setZipCode(addressEntity.getZipcode());
    orderAddress.setState(addressEntity.getState());
    orderAddress.setAddressLine(addressEntity.getStreet());

    orderEntity.setAddress(orderAddress);

    List<OrderStatusEntity> orderStatusList = new ArrayList<>();

    for (CartItemEntity cartItem : cart.getItems()) {

      ProductVariation variation = cartItem.getProductVariation();

      if (!variation.isActive() || variation.getProduct().isDeleted()) {
        throw new BadRequest("Product not available");
      }

      if (cartItem.getQuantity() > variation.getQuantityAvailable()) {
        throw new BadRequest("Insufficient stock for product variation: "
            + variation.getId());
      }

      BigDecimal itemTotal =
          variation.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

      totalAmount = totalAmount.add(itemTotal);

      OrderProduct orderItem = new OrderProduct();
      orderItem.setOrder(orderEntity);
      orderItem.setProduct(variation);
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setPrice(variation.getPrice());

      orderItems.add(orderItem);
      orderProductRepo.save(orderItem);

      // reduce stock
      variation.setQuantityAvailable(
          variation.getQuantityAvailable() - cartItem.getQuantity()
      );

      productVariationRepo.save(variation);
      OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
      orderStatusEntity.setFromStatus(null);
      orderStatusEntity.setOrderProduct(orderItem);
      orderStatusEntity.setToStatus(OrderStatus.ORDER_CONFIRMED);
      orderStatusEntity.setTransitionNotesComments("Order Placed By Customer");
      orderStatusEntity.setTimestamp(LocalDateTime.now());
      orderStatusEntity.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));
    }
    orderEntity.setAmountPaid(totalAmount);

    orderProductRepo.saveAll(orderItems);

    orderRepo.save(orderEntity);
    cartItemRepo.deleteAll(cart.getItems());


  }


  @Transactional
  public void addPartialCartToOrder(List<Long> cartItemIds, Long addressId) {
    Long userId = securityUtil.getCurrentUserId();

    CartEntity cart = cartRepo.findByCustomer_User_Id(userId);

    if (cart == null) {
      throw new BadRequest("Cart is null");
    }

    List<CartItemEntity> cartItems = cartItemRepo.findAllById(cartItemIds);
    BigDecimal totalAmount = BigDecimal.ZERO;

    CustomerEntity customerEntity = customerRepo.findByUserId(userId)
        .orElseThrow(() -> new BadRequest("No customer found with this id"));

    OrderEntity orderEntity = new OrderEntity();

    List<OrderProduct> orderItems = new ArrayList<>();

    for (CartItemEntity cartItem : cartItems) {

      if (!cartItem.getCart().getCustomer().getUser().getId().equals(userId)) {
        throw new BadRequest("Invalid cart item selection");
      }

      ProductVariation variation = cartItem.getProductVariation();

      if (!variation.isActive() || variation.getProduct().isDeleted()) {
        throw new BadRequest("Product not available");
      }

      if (cartItem.getQuantity() > variation.getQuantityAvailable()) {
        throw new BadRequest("Insufficient stock for product variation: "
            + variation.getId());
      }

      BigDecimal itemTotal =
          variation.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

      totalAmount = totalAmount.add(itemTotal);

      OrderProduct orderItem = new OrderProduct();
      orderItem.setOrder(orderEntity);
      orderItem.setProduct(variation);
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setPrice(variation.getPrice());

      orderItems.add(orderItem);

      // reduce stock
      variation.setQuantityAvailable(
          variation.getQuantityAvailable() - cartItem.getQuantity()
      );
      productVariationRepo.save(variation);

      OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
      orderStatusEntity.setFromStatus(null);
      orderStatusEntity.setOrderProduct(orderItem);
      orderStatusEntity.setToStatus(OrderStatus.ORDER_PLACED);
      orderStatusEntity.setTransitionNotesComments("Order Placed By Customer");
      orderStatusEntity.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));
    }
    orderProductRepo.saveAll(orderItems);

    AddressEntity addressEntity = addressRepo.findAllAddressesByUserId(userId).stream()
        .filter(a -> a.getAddressId().equals(addressId)).findFirst()
        .orElseThrow(() -> new BadRequest("Address not found"));

    OrderAddress orderAddress = new OrderAddress();
    orderAddress.setCity(addressEntity.getCity());
    orderAddress.setCountry(addressEntity.getCountry());
    orderAddress.setLabel(addressEntity.getLabel());
    orderAddress.setZipCode(addressEntity.getZipcode());
    orderAddress.setState(addressEntity.getState());
    orderAddress.setAddressLine(addressEntity.getStreet());

    orderEntity.setAmountPaid(totalAmount);
    orderEntity.setCustomer(customerEntity);
    orderEntity.setPaymentMethod("Online");
    orderEntity.setAddress(orderAddress);

    orderRepo.save(orderEntity);
    cartItemRepo.deleteAll(cartItems);

  }


  @Transactional
  public void directlyOrderFromProductVariation(Long productVariationId, int quantity,
      Long addressId) {
    Long userId = securityUtil.getCurrentUserId();
    ProductVariation productVariation = productVariationRepo.findById(productVariationId)
        .orElseThrow(() -> new BadRequest("Product not found"));

    if (quantity > productVariation.getQuantityAvailable()) {
      throw new BadRequest("Quantity is not avialable");
    }

    if (!productVariation.isActive() || productVariation.getProduct().isDeleted()) {
      throw new BadRequest("Product not available");
    }

    BigDecimal totalAmount = productVariation.getPrice().multiply(BigDecimal.valueOf(quantity));

    AddressEntity addressEntity = addressRepo.findAllAddressesByUserId(userId).stream()
        .filter(a -> a.getAddressId().equals(addressId)).findFirst()
        .orElseThrow(() -> new BadRequest("Address not found"));

    CustomerEntity customerEntity = customerRepo.findByUserId(userId)
        .orElseThrow(() -> new BadRequest("Customer Not found"));

    OrderAddress orderAddress = new OrderAddress();
    orderAddress.setCity(addressEntity.getCity());
    orderAddress.setCountry(addressEntity.getCountry());
    orderAddress.setLabel(addressEntity.getLabel());
    orderAddress.setZipCode(addressEntity.getZipcode());
    orderAddress.setState(addressEntity.getState());
    orderAddress.setAddressLine(addressEntity.getStreet());

    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setAddress(orderAddress);
    orderEntity.setAmountPaid(totalAmount);
    orderEntity.setCustomer(customerEntity);
    orderEntity.setPaymentMethod("ONLINE");

    orderRepo.save(orderEntity);

    OrderProduct orderProduct = new OrderProduct();

    orderProduct.setProduct(productVariation);
    orderProduct.setPrice(productVariation.getPrice());
    orderProduct.setOrder(orderEntity);
    orderProduct.setQuantity(quantity);

    orderProductRepo.save(orderProduct);

    productVariation.setQuantityAvailable(productVariation.getQuantityAvailable() - quantity);
    productVariationRepo.save(productVariation);

    OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
    orderStatusEntity.setFromStatus(null);
    orderStatusEntity.setOrderProduct(orderProduct);
    orderStatusEntity.setToStatus(OrderStatus.ORDER_PLACED);
    orderStatusEntity.setTransitionNotesComments("Order Placed By Customer");
    orderStatusEntity.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));

  }

  @Transactional
  public void cancelOrder(Long orderProductId) {
    Long userId = securityUtil.getCurrentUserId();
    OrderProduct orderProduct = orderProductRepo.findById(orderProductId)
        .orElseThrow(() -> new BadRequest("Order Product not found"));

    if (!orderProduct.getOrder().getCustomer().getUser().getId().equals(userId)) {
      throw new UnauthorizedException("Product is not of current User");
    }
    OrderStatusEntity orderStatusEntity = orderStatusRepo.findTopByOrderProduct_ItemIdOrderByTimestampDesc(
            orderProductId)
        .orElseThrow(() -> new BadRequest("Order status not found"));

    OrderStatus currentStatus = orderStatusEntity.getToStatus();

    if (currentStatus != OrderStatus.ORDER_CONFIRMED && currentStatus != OrderStatus.ORDER_PLACED) {
      throw new BadRequest("Order status is not cancellable");
    }

    OrderStatusEntity cancelStatus = new OrderStatusEntity();
    cancelStatus.setOrderProduct(orderProduct);
    cancelStatus.setFromStatus(currentStatus);
    cancelStatus.setToStatus(OrderStatus.CANCELLED);
    cancelStatus.setTransitionNotesComments("Order cancelled by customer");
    cancelStatus.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));

    orderStatusRepo.save(cancelStatus);

  }

  @Transactional
  public void returnOrder(Long orderProductId) {
    Long userId = securityUtil.getCurrentUserId();
    OrderProduct orderProduct = orderProductRepo.findById(orderProductId)
        .orElseThrow(() -> new BadRequest("Order Product not found"));

    if (!orderProduct.getOrder().getCustomer().getUser().getId().equals(userId)) {
      throw new UnauthorizedException("Product is not of current User");
    }
    OrderStatusEntity orderStatusEntity = orderStatusRepo.findTopByOrderProduct_ItemIdOrderByTimestampDesc(
            orderProductId)
        .orElseThrow(() -> new BadRequest("Order status not found"));

    OrderStatus currentStatus = orderStatusEntity.getToStatus();

    if (currentStatus != OrderStatus.DELIVERED) {
      throw new BadRequest("Order status is not returnable");
    }

    OrderStatusEntity returnStatus = new OrderStatusEntity();
    returnStatus.setOrderProduct(orderProduct);
    returnStatus.setFromStatus(currentStatus);
    returnStatus.setToStatus(OrderStatus.RETURN_REQUESTED);
    returnStatus.setTransitionNotesComments("Order cancelled by customer");
    returnStatus.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));

    orderStatusRepo.save(returnStatus);

  }


  public OrderResponseDto getOrderDetails(Long orderId) {
    Long userId = securityUtil.getCurrentUserId();
    OrderEntity orderEntity = orderRepo.findById(orderId)
        .orElseThrow(() -> new BadRequest("Order Not found"));

    if (!orderEntity.getCustomer().getUser().getId().equals(userId)) {
      throw new UnauthorizedException("Order Not accessible");
    }

    List<OrderProduct> allOrderedProducts = orderProductRepo.findAllByOrder_OrderId(orderId);
    List<OrderProductDtoResponse> orderProductDtoResponseStream = allOrderedProducts.stream().map(
            orderProduct -> new OrderProductDtoResponse(orderProduct.getPrice(),
                orderProduct.getQuantity(), orderProduct.getProduct().getProduct().getProductName()))
        .toList();

    return new OrderResponseDto(orderEntity.getAmountPaid(), orderEntity.getPaymentMethod(),
        orderEntity.getAddress(), orderEntity.getOrderId(), orderEntity.getCreatedAt(),
        orderProductDtoResponseStream);

  }


  public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
    Long userId = securityUtil.getCurrentUserId();

    Page<OrderEntity> orders = orderRepo.findByCustomer_User_Id(userId, pageable);
    List<Long> orderIds = new ArrayList<>();
    for(OrderEntity order : orders){
      orderIds.add(order.getOrderId());
    }
    List<OrderProduct> allByOrderIdIn = orderProductRepo.findAllByOrder_OrderIdIn(orderIds);

    Map<Long, List<OrderProduct>> orderProductMap =
        allByOrderIdIn.stream()
            .collect(Collectors.groupingBy(
                op -> op.getOrder().getOrderId()
            ));


    return orders.map(order -> {
      List<OrderProductDtoResponse> orderProductDtoResponseStream = orderProductMap.getOrDefault(order.getOrderId(),List.of()).stream().map(
              orderProduct -> new OrderProductDtoResponse(orderProduct.getPrice(),
                  orderProduct.getQuantity(), orderProduct.getProduct().getProduct().getProductName()))
          .toList();

      return new OrderResponseDto(order.getAmountPaid(), order.getPaymentMethod(),
          order.getAddress(), order.getOrderId(), order.getCreatedAt(),
          orderProductDtoResponseStream);
    });

  }




  public Page<OrderProductDtoForSeller> getAllOrderOfSellerProduct(Pageable pageable) {

    Long userId = securityUtil.getCurrentUserId();

    Page<OrderProduct> orderProducts =
        orderProductRepo.findByProduct_Product_Seller_User_Id(userId, pageable);

    return orderProducts.map(op ->
        new OrderProductDtoForSeller(
            op.getQuantity(),
            op.getPrice(),
            op.getProduct().getProduct().getProductName()
        )
    );
  }

  @Transactional
  public void changeOrderStatus(Long orderProductId,String fromStatus,String toStatus){
    Long userId = securityUtil.getCurrentUserId();
    OrderProduct orderProduct = orderProductRepo.findById(orderProductId)
        .orElseThrow(() -> new BadRequest("No Order found with this id"));

    if(!orderProduct.getProduct().getProduct().getSeller().getUser().getId().equals(userId)){
      throw new UnauthorizedException("Order does not belongs to you");
    }

    OrderStatusEntity orderStatusEntity1 = orderStatusRepo.findTopByOrderProduct_ItemIdOrderByTimestampDesc(
        orderProductId).orElseThrow(() -> new BadRequest("No Data found for this order"));

    OrderStatus fromStatus1 = OrderStatus.valueOf(fromStatus);

    if(orderStatusEntity1.getToStatus()!=fromStatus1){
      throw new BadRequest("Current status is not matching with your status");
    }

    OrderStatus toStatus1 = OrderStatus.valueOf(toStatus);

    if(fromStatus1.canTransitionTo(toStatus1)){
      OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
      orderStatusEntity.setToStatus(toStatus1);
      orderStatusEntity.setOrderProduct(orderProduct);
      orderStatusEntity.setFromStatus(fromStatus1);
      orderStatusEntity.setTransitionNotesComments("Transition Chnages from "+fromStatus +"to "+toStatus);
      orderStatusEntity.getCreateAndUpdatedBy().setCreatedBy(String.valueOf(userId));
      orderStatusRepo.save(orderStatusEntity);

    }

  }

  public Page<OrderEntity> getAllOrdersForAdmin(Pageable pageable){
    return orderRepo.findAll(pageable);
  }



}

