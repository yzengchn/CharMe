package com.yzeng.charme.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输, 
Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。 
Queue:消息的载体,每个消息都会被投到一个或多个队列。 
Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来. 
Routing Key:路由关键字,exchange根据这个关键字进行消息投递。 
vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。 
Producer:消息生产者,就是投递消息的程序. 
Consumer:消息消费者,就是接受消息的程序. 
Channel:消息通道,在客户端的每个连接里,可建立多个channel.
*/
@Configuration
public class RabbitMQConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String message = "topic.message";
	
	private final static String messages = "topic.messages";

    @Bean
    public Queue queueMessage() {
    	return new Queue(RabbitMQConfig.message);
    }
    
    @Bean
    public Queue queueMessages() {
    	return new Queue(RabbitMQConfig.messages);
    }
    
    @Bean
    TopicExchange exchange() {
    	return new TopicExchange("exchange");
    }
    
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage,TopicExchange exchange) {
    	return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }
    
    @Bean
    Binding bindingExchangeMessages(Queue queueMessage,TopicExchange exchange) {
    	//这里的#表示零个或多个词。
    	return BindingBuilder.bind(queueMessage).to(exchange).with("topic.#");
    }
    
    

}
