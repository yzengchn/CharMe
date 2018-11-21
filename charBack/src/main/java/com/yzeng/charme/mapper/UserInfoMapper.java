package com.yzeng.charme.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yzeng.charme.entity.User;
import com.yzeng.charme.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
	/**
	 * 第一次保存用户个人信息
	 * @param userInfo userId username
	 * @return
	 */
	int firstSaveUserInfo(UserInfo userInfo);
	
	/**
	 * 根据用户ID查询个人信息
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfoByUserId(Integer userId);
	
	/**
	 * 更新用户个人信息
	 * @param userInfo
	 * @return
	 */
	int updateUserInfo(UserInfo userInfo);
	
	/**
	 * 获得所有用户
	 * @return
	 */
	List<UserInfo> findAll();
	
	/**
     * 根据用户名或者昵称查询用户信息列表 
     * @param user
     * @return
     */
    List<UserInfo> findByName(UserInfo userinfo);
}
