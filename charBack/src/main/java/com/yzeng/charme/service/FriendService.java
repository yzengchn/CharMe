package com.yzeng.charme.service;

import com.yzeng.charme.entity.UserRelation;

public interface FriendService {
	
	/**
	 * 建立好友关系
	 * @param userIdA
	 * @param userIdB
	 * @return
	 */
	int addFriend(UserRelation userRelation);
}
