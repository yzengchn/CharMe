package com.yzeng.charme.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yzeng.charme.entity.User;

@Mapper
public interface UserMapper {
	
	User getNameById(User user);
	
	Integer insertUser(User user);
	
	User getUserByName(@Param(value="username")String username);

	int insertTest(Map<String, String> map);
}
