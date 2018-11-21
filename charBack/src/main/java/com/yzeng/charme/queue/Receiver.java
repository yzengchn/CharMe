package com.yzeng.charme.queue;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "topic.messages")
public class Receiver {

	@RabbitHandler
	public void receiver1(String msg) {
		System.out.println("receiver:"+ msg);
	}
}
