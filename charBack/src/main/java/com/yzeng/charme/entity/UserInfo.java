package com.yzeng.charme.entity;

import java.util.Date;

/**
 * 用户信息资料
 * @author yzengchn@163.com
 * 2018-08-22 17:24:29
 * @version 1.0.0
 */
public class UserInfo {
	//用户ID
	private Integer userId;
	//用户名
	private String username;
	//昵称
	private String nickname;
	//性别（1男 2女 0未填）
	private Integer sex;
	//年龄
	private Integer age; 
	//出生年月日
	private Date birthTime;
	//签名
	private String signature; 
	//头像
	private Integer avatarId;
	//1为在线  0为离线
	private Integer isOnline;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getAvatarId() {
		return avatarId;
	}
	public void setAvatarId(Integer avatarId) {
		this.avatarId = avatarId;
	}
	public Integer getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}
	public Date getBirthTime() {
		return birthTime;
	}
	public void setBirthTime(Date birthTime) {
		this.birthTime = birthTime;
	}
	
	
	
}
