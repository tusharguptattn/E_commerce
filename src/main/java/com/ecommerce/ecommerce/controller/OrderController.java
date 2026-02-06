package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.dto.OrderProductDtoForSeller;
import com.ecommerce.ecommerce.dto.OrderResponseDto;
import com.ecommerce.ecommerce.entity.OrderEntity;
import com.ecommerce.ecommerce.service.OrderService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {

  OrderService service;
  MessageSource messageSource;

  // Customer

  @PostMapping("/addAllToOrder/{addressId}")
  public ResponseEntity<String> addAllProductFromCartToOrder(
      @PathVariable @NotNull(message = "address id can not be null") Long addressId, Locale locale) {
    service.addAllCartToOrder(addressId);
    return ResponseEntity.ok(messageSource.getMessage("order.added", null, locale));
  }

  // Customer
  @PostMapping("/addPartialProductToOrder/{addressId}")
  public ResponseEntity<String> addPartialProductFromCartToOrder(
      @RequestParam @NotNull(message = "Atleast one id is required")
      List<Long> cartItemIds,
      @PathVariable @NotNull(message = "address id can not be null") Long addressId,Locale locale) {

    service.addPartialCartToOrder(cartItemIds, addressId);
    return ResponseEntity.ok(messageSource.getMessage("order.added", null, locale));
  }


  //Customer
  @PostMapping("/addOrderFromProductVariation/{addressId}/{productVariationId}/{quantity}")
  public ResponseEntity<String> addOrderDirectlyFromProductVariation(
      @PathVariable @NotNull(message = "product variation id can not be null") Long productVariationId,
      @PathVariable @Min(value = 1, message = "Minimum quatity should be 1") int quantity,
      @PathVariable @NotNull(message = "address id can not be null") Long addressId,Locale locale) {
    service.directlyOrderFromProductVariation(productVariationId, quantity, addressId);
    return ResponseEntity.ok(messageSource.getMessage("order.placed", null, locale));
  }

  // Customer
  @PutMapping("/cancelOrder/{orderProductId}")
  public ResponseEntity<String> cancelOrder(
      @PathVariable @NotNull(message = "order product id can not be null") Long orderProductId,Locale locale) {
    service.cancelOrder(orderProductId);
    return ResponseEntity.ok(messageSource.getMessage("order.cancelled", null, locale));
  }

  // Customer

  @PutMapping("/returnOrder/{orderProductId}")
  public ResponseEntity<String> returnOrder(
      @PathVariable @NotNull(message = "order product id can not be null") Long orderProductId,Locale locale) {
    service.returnOrder(orderProductId);
    return ResponseEntity.ok(messageSource.getMessage("order.returned", null, locale));
  }

  // Customer
  @GetMapping("/viewOrder/{orderId}")
  public ResponseEntity<OrderResponseDto> viewOrder(
      @PathVariable @NotNull(message = "order id can not be null") Long orderId) {
    return ResponseEntity.ok(service.getOrderDetails(orderId));
  }

  // Customer
  @GetMapping("/viewAllOrder")
  public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
      @RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "orderId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query) {

    if ("id".equalsIgnoreCase(sort)) {
      sort = "orderId";
    }
    Sort.Direction direction = order.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;

    Sort sorting = Sort.by(direction, sort);

    Pageable pageable = PageRequest.of(offset, max, sorting);
    return ResponseEntity.ok(service.getAllOrders(pageable));
  }


    //Seller
    @GetMapping("/seller/getAllProducts")
    public ResponseEntity<Page<OrderProductDtoForSeller>> getAllSellerProductOrders(  @RequestParam(defaultValue = "10") int max,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "itemId") String sort,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(required = false) String query){

      if ("id".equalsIgnoreCase(sort)) {
        sort = "itemId";
      }
      Sort.Direction direction = order.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;

      Sort sorting = Sort.by(direction, sort);

      Pageable pageable = PageRequest.of(offset, max, sorting);
      return ResponseEntity.ok(service.getAllOrderOfSellerProduct(pageable));

    }

    //Seller
    // Admin
    @PutMapping("/changeOrderStatus/{orderProductId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable @NotNull(message = "order product id can not be null") Long orderProductId,@RequestParam @NotBlank(message = "from status can not be null") String fromStatus,@RequestParam @NotBlank(message = "to status can not be blank") String toStatus,Locale locale) {
      service.changeOrderStatus(orderProductId,fromStatus,toStatus);
      return ResponseEntity.ok(messageSource.getMessage("order.status-change", null, locale));

    }

    //Admin
  @GetMapping("/admin/getAllOrderForAdmin")
  public ResponseEntity<Page<OrderEntity>> getAllOrdersForAdmin(@RequestParam(defaultValue = "10") int max,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "orderId") String sort,
      @RequestParam(defaultValue = "asc") String order,
      @RequestParam(required = false) String query){

    if ("id".equalsIgnoreCase(sort)) {
      sort = "orderId";
    }
    Sort.Direction direction = order.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;

    Sort sorting = Sort.by(direction, sort);

    Pageable pageable = PageRequest.of(offset, max, sorting);
    return ResponseEntity.ok(service.getAllOrdersForAdmin(pageable));

  }














}
