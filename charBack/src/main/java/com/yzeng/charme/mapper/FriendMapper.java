package com.yzeng.charme.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yzeng.charme.entity.UserRelation;

@Mapper
public interface FriendMapper {
	/**
	 * 建立好友关系
	 * @param userIdA
	 * @param userIdB
	 * @return
	 */
	int addFriend(UserRelation userRelation);
}
