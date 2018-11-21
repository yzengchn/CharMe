package com.yzeng.charme.entity;

import java.util.Date;

/**
 * 	 群实体
 * @author yzengchn@163.com
 * 2018-09-06 10:43:25
 * @version 1.0.0
 */
public class Group {
	private Integer id;
	private Integer createUserId;//创建用户
	private String groupName;//群名
	private String groupDescribes;//群介绍
	private Date createTime;//创建时间
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescribes() {
		return groupDescribes;
	}
	public void setGroupDescribes(String groupDescribes) {
		this.groupDescribes = groupDescribes;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
