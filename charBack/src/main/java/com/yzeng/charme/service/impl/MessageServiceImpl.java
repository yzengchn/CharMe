package com.yzeng.charme.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.yzeng.charme.entity.Message;
import com.yzeng.charme.mapper.MessageMapper;
import com.yzeng.charme.service.MessageService;

@Service("messageService")
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageMapper messageMapper;
	
	@Override
	public Integer insertMsg(Message message) {
		return messageMapper.insertMsg(message);
	}

	@Override
	public Message getMsgById(Integer id) {
		return messageMapper.getMsgById(id);
	}

	@Override
	public List<Message> getMessageloggingList(Message message,int pageNum, int pageSize) {
		//总页数
		Integer pageCount = 0;
		Integer totalCount = messageMapper.getMessageTotalCount(message);
		if(totalCount%pageSize == 0) {
			pageCount = totalCount/pageSize;
		}else {
			pageCount = (totalCount/pageSize) + 1;
		}
		//当前传入的页数，计算
		pageNum = pageCount - pageNum;
		PageHelper.startPage(pageNum, pageSize);
		return messageMapper.getMessageloggingList(message);
	}
	
	@Override
	public List<Message> getOfflineMessageList(Integer userId) {
		return messageMapper.getOfflineMessageList(userId);
	}

	@Override
	public int updateMessage(Message message) {
		return messageMapper.updateMessage(message);
	}

	@Override
	public List<Message> getMessageListByStatus(Message message) {
		return messageMapper.getMessageListByStatus(message);
	}

	@Override
	public Integer getMessageTotalCount(Message message) {
		return messageMapper.getMessageTotalCount(message);
	}

}
