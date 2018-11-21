package com.yzeng.charme.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yzeng.charme.entity.Message;

@Mapper
public interface MessageMapper {
	/**
	 * 插入一条消息
	 * @param message
	 * @return
	 */
	Integer insertMsg (Message message);
	
	/**
	 * 获得聊天记录总数
	 * @return
	 */
	Integer getMessageTotalCount(Message message);
	
	/**
	 * 根据ID查询消息
	 * @param id
	 * @return
	 */
	Message getMsgById(Integer id);
	
	/**
	 * 聊天记录查询
	 * @param message
	 * @return
	 */
	List<Message> getMessageloggingList(Message message);
	
	/**
	 * 查询该用户的离线消息 
	 * @param userId
	 * @return
	 */
	List<Message> getOfflineMessageList(Integer userId);
	
	/**
	 * 查询该用户的消息传输状态1：已读 0：未读 2：失败
	 * @param message
	 * @return
	 */
	List<Message> getMessageListByStatus(Message message);
	
	
	
	/**
	 * 更新消息状态
	 * @param message
	 * @return
	 */
	int updateMessage (Message message);
	
	
}
