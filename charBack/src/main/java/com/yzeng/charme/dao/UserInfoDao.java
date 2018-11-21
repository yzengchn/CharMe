package com.yzeng.charme.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yzeng.charme.entity.UserInfo;

public interface UserInfoDao {
	
	List<UserInfo> findAll();

	UserInfo getUserInfoByUserId(Integer id);
	
	UserInfo getUserInfoByName(String username);

    void update(UserInfo userInfo);

    void insert(UserInfo userInfo);

    void insertAll(List<UserInfo> userInfoList);

    void remove(Integer id);

    List<UserInfo> findByPage(UserInfo userInfo, Pageable pageable);
}
