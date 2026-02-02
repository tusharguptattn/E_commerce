package com.ecommerce.ecommerce.scheduler;

import com.ecommerce.ecommerce.dto.OrderSummaryDto;
import com.ecommerce.ecommerce.service.EmailService;
import com.ecommerce.ecommerce.service.OrderService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerScheduler {

  OrderService orderService;
  EmailService emailService;

  @Scheduled(cron = "0 30 14 * * *")
  public void sendOrderStatusToSeller(){
    List<OrderSummaryDto> listOfOrdersForSeller = orderService.getListOfOrdersForSeller();
    Map<String , List<OrderSummaryDto>> ordersBySeller = listOfOrdersForSeller.stream().collect(
        Collectors.groupingBy(OrderSummaryDto::sellerEmail));

    ordersBySeller.forEach(emailService::sendPendingOrderMail);


  }

}
