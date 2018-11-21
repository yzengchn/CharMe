package com.yzeng.charme.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.yzeng.charme.entity.Group;
import com.yzeng.charme.mapper.GroupMapper;
import com.yzeng.charme.service.GroupService;

@Service("groupService")
public class GroupServiceImpl implements GroupService{

	@Resource
	private GroupMapper groupMapper;
	
	@Override
	public int addGroup(Group group) {
		group.setCreateTime(new Date());
		return groupMapper.addGroup(group);
	}

	@Override
	public List<Group> getGroupByCreateUserId(Integer userId) {
		return groupMapper.getGroupByCreateUserId(userId);
	}
	
	@Override
	public List<Group> getGroupByUserId(Integer userId) {
		return groupMapper.getGroupByUserId(userId);
	}

	@Override
	public List<Map<String, Object>> getGroupUserByGroupId(Integer groupId) {
		return groupMapper.getGroupUserByGroupId(groupId);
	}

	@Override
	public int addUserToGroup(Integer groupId, Integer userId) {
		return groupMapper.addUserToGroup(groupId, userId);
	}

	@Override
	public List<Map<String, Object>> getGroupMsgHistory(Integer groupId,int pageNum, int pageSize) {
		//总页数
		Integer pageCount = 0;
		//总记录数
		Integer totalCount = groupMapper.getGroupMsgCount(groupId);
		if(totalCount%pageSize == 0) {
			pageCount = totalCount/pageSize;
		}else {
			pageCount = (totalCount/pageSize) + 1;
		}
		//当前传入的页数，计算
		pageNum = pageCount - pageNum;
		if(pageNum <= 0) {
			return new ArrayList<Map<String, Object>>();
		}
		PageHelper.startPage(pageNum, pageSize);
		return groupMapper.getGroupMsg(groupId);
	}

	@Override
	public Integer getGroupMsgCount(Integer groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
