package com.ecommerce.ecommerce.Publisher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class RabbitMQProducer {

  static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
  @Value("${rabbitmq.exchange.name}")
  String exchangeName;
  @Value("${rabbitmq.binding.key}")
  String routingKey;

  private RabbitTemplate rabbitTemplate;


  public void sendMessage(String message){
    LOGGER.info("Sending message: {}", message);
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
  }


}
