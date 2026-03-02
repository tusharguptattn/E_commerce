package com.ecommerce.ecommerce.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${rabbitmq.queue.name}")
  private String queueName;
  @Value("${rabbitmq.exchange.name}")
  private String  exchangeName;
  @Value("${rabbitmq.binding.key}")
  private String  bindingKey;

  @Bean
  public Queue  queue(){
    return new Queue(queueName);
  }
  @Bean
  public TopicExchange exchange(){
    return new TopicExchange(exchangeName);
  }
  @Bean
  public Binding bind(){
    return BindingBuilder.bind(queue()).to(exchange()).with(bindingKey);
  }
}
