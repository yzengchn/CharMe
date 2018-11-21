package com.yzeng.charme.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yzeng.charme.entity.Group;

@Mapper
public interface GroupMapper {
	/**
	 *  新增群
	 * @param group
	 * @return
	 */
	int addGroup(Group group);
	
	/**
	 *  	加群
	 * @param groupId
	 * @param userId
	 * @return
	 */
	int addUserToGroup(@Param("groupId")Integer groupId,@Param("userId")Integer userId);
	
	/**
	 * 	查询用户创建的群
	 * @param userId
	 * @return
	 */
	List<Group> getGroupByCreateUserId(Integer userId);
	
	/**
	 * 	查询用户加入的群
	 * @param userId
	 * @return
	 */
	List<Group> getGroupByUserId(Integer userId);
	
	/**
	 *  获得群成员信息
	 * @param groupId
	 * @return
	 */
	List<Map<String, Object>> getGroupUserByGroupId(Integer groupId);
	
	/**
	 * 查询群信息总数
	 * @param groupId
	 * @return
	 */
	Integer getGroupMsgCount(Integer groupId);
	
	/**
	 * 查询群消息
	 * @param groupId
	 * @return
	 */
	List<Map<String, Object>> getGroupMsg(Integer groupId);
	
	
}
