package com.yzeng.charme.queue;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yzeng.charme.entity.User;

@Component
@RabbitListener(queues = "topic.message")
public class UserReceiver {

	@RabbitHandler
	public void receiver(String userStr) {
		User user = JSON.parseObject(userStr, User.class);
		System.out.println(user.getPassword());
		System.out.println(user.getUsername());
	}
}
