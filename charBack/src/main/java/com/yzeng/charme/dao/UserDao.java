package com.yzeng.charme.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yzeng.charme.entity.User;

public interface UserDao {
	
	List<User> findAll();

	User getUserById(Integer id);
	
	User getUserByName(String username);
	
	User login(String username,String password);

    void update(User user);

    void insert(User user);

    void insertAll(List<User> users);

    void remove(Integer id);

    List<User> findByPage(User user, Pageable pageable);
}
