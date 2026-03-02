package com.ecommerce.ecommerce.rabbitmqConsumer;

import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

  private static final Logger LOGGER  = Logger.getLogger(RabbitMQConsumer.class.getName());



  @RabbitListener(queues = {"${rabbitmq.queue.name}"})
  public void consumeMessage(String message) {
    LOGGER.info(message);

  }

}
