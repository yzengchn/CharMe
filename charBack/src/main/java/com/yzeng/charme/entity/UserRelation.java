package com.yzeng.charme.entity;

import java.util.Date;

/**
 * 用户关系关联
 * @author Yao.Zeng
 * @version  [版本号, 2018年8月21日]
 */
public class UserRelation {
	private Integer id;
	//用户A
	private Integer userIdA;
	//用户B
	private Integer userIdB;
	//用户之间的关系状态 1：好友  0:黑名单
	private int relationStatus;
	//添加时间
	private Date relationTime;
	//用户A给用户B的备注名
	private String memoName;
	
	public String getMemoName() {
		return memoName;
	}
	public void setMemoName(String memoName) {
		this.memoName = memoName;
	}
	public Integer getUserIdA() {
		return userIdA;
	}
	public void setUserIdA(Integer userIdA) {
		this.userIdA = userIdA;
	}
	public Integer getUserIdB() {
		return userIdB;
	}
	public void setUserIdB(Integer userIdB) {
		this.userIdB = userIdB;
	}
	public int getRelationStatus() {
		return relationStatus;
	}
	public void setRelationStatus(int relationStatus) {
		this.relationStatus = relationStatus;
	}
	public Date getRelationTime() {
		return relationTime;
	}
	public void setRelationTime(Date relationTime) {
		this.relationTime = relationTime;
	}
	public UserRelation() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
