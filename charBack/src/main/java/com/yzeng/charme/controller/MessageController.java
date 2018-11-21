package com.yzeng.charme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yzeng.charme.entity.Message;
import com.yzeng.charme.service.MessageService;

@Controller
@RequestMapping("msg")
public class MessageController {
	
	@Autowired
	private MessageService messageService; 

	@RequestMapping("{pageNum}-{pageSize}")
	@ResponseBody
	public List<Message> getList(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize){
		Message message = new Message();
		message.setFromUserId(1);
		message.setToUserId(2);
		message.setMsgType(0);
		return messageService.getMessageloggingList(message,pageNum, pageSize);
	}
	
	@RequestMapping("getOfflineMessageList")
	@ResponseBody
	public List<Message> getOfflineMessageList(Integer userId){
		Message message = new Message();
		message.setToUserId(userId);
		message.setIsTransport(0);
		return messageService.getMessageListByStatus(message);
	}
	
	@RequestMapping("updateMsgStatus")
	@ResponseBody
	public String updateMsgStatus(Integer fromUserId, Integer toUserId) {
		Message message = new Message();
		message.setFromUserId(fromUserId);
		message.setToUserId(toUserId);
		message.setIsTransport(1);
		messageService.updateMessage(message);
		return "";
	}
	
	@RequestMapping("getMsgHistory")
	@ResponseBody
	public List<Message> getMsgHistory(Integer fromUserId, Integer toUserId,Integer pageNum, Integer pageSize){
		
		Message message = new Message();
		message.setFromUserId(fromUserId);
		message.setToUserId(toUserId);
		message.setMsgType(1);
		
		return messageService.getMessageloggingList(message, pageNum, pageSize);
	}
}
