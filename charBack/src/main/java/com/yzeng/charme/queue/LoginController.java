package com.yzeng.charme.queue;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@RequestMapping(value="send",method=RequestMethod.GET)
	@ResponseBody
	public String send() {
		String content = "Date : "+new Date();
		amqpTemplate.convertAndSend("exchange", "topic.messages", content);
		return content;
	}
	
}
