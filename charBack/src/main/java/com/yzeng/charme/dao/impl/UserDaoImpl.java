package com.yzeng.charme.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.yzeng.charme.dao.UserDao;
import com.yzeng.charme.entity.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

	/**
     * 由springboot自动注入，默认配置会产生mongoTemplate这个bean
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查找全部
     */
    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    /**
     * 根据id得到对象
     */
    @Override
    public User getUserById(Integer id) {

        return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), User.class);
    }
    
    /**
     * 根据UserName得到对象
     */
    @Override
    public User getUserByName(String username) {
    	
    	return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), User.class);
    }
    
    /**
     	* 登录
     */
    @Override
    public User login(String username,String password) {
    	Criteria criteria = new Criteria();
    	criteria.and("username").is(username);
    	criteria.and("password").is(password);
    	return mongoTemplate.findOne(new Query(criteria), User.class);
    }

    /**
     * 插入一个用户
     */
    @Override
    public void insert(User user) {
        mongoTemplate.insert(user);
    }

    /**
     * 根据id删除一个用户
     */
    @Override
    public void remove(Integer id) {
        Criteria criteria = Criteria.where("id").is(id);  
        Query query = new Query(criteria);
        mongoTemplate.remove(query,User.class);
    }

    /**
     * 分页查找
     * 
     * user代表过滤条件
     * 
     * pageable代表分页bean
     */
    @Override
    public List<User> findByPage(User user, Pageable pageable) {
        Query query = new Query();
        if (user != null && user.getUsername() != null) {
            //模糊查询
            query = new Query(Criteria.where("name").regex("^" + user.getUsername()));
        }
        List<User> list = mongoTemplate.find(query.with(pageable), User.class);
        return list;
    }

    /**
     * 根据id更新
     */
    @Override
    public void update(User user) {
        Criteria criteria = Criteria.where("id").is(user.getId());
        Query query = Query.query(criteria);
        Update update = Update.update("name", user.getUsername()).set("password", user.getPassword());
        mongoTemplate.updateMulti(query, update, User.class);
    }

    /**
     * 插入一个集合
     */
    @Override
    public void insertAll(List<User> users) {
        mongoTemplate.insertAll(users);
    }

}
