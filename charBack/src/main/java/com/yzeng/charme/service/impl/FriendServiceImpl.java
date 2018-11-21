package com.yzeng.charme.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yzeng.charme.entity.UserRelation;
import com.yzeng.charme.mapper.FriendMapper;
import com.yzeng.charme.service.FriendService;

@Service("friendService")
public class FriendServiceImpl implements FriendService{

	@Resource
	private FriendMapper friendMapper; 
	
	@Override
	public int addFriend(UserRelation userRelation) {
		userRelation.setRelationTime(new Date());
		return friendMapper.addFriend(userRelation);
	}
	
}
