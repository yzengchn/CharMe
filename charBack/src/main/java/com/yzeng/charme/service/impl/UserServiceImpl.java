package com.yzeng.charme.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.yzeng.charme.dao.UserDao;
import com.yzeng.charme.dao.UserInfoDao;
import com.yzeng.charme.entity.User;
import com.yzeng.charme.entity.UserInfo;
import com.yzeng.charme.mapper.UserInfoMapper;
import com.yzeng.charme.mapper.UserMapper;
import com.yzeng.charme.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	

    @Override
    public List<UserInfo> findAll() {
        //return userDao.findAll();
        return userInfoMapper.findAll();
    }

    @Override
    public void insert(User user) {
    	String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
    	//插入用户账户信息
    	user.setPassword(md5Pwd);
    	user.setRegistertime(new Date());
    	user.setLastTime(new Date());
    	Integer num = userMapper.insertUser(user);
    	
    	//插入用户个人信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfoMapper.firstSaveUserInfo(userInfo);
    }

    
    
    @Override
	public Map<String, String> register(User user) {
		/**
		 *  1.用户输入[username]和[password] 
			2.系统为用户生成[Salt值]
			3.系统将[Salt值]和[password]拼接在一起 
			4.对拼接后的值进行散列，得到[Hash1] 
			5.将[Hash1]和[Salt值]放在数据库中。 
		 */
    	Map<String,String> map = new HashMap<String,String>();
    	try {
    		String millis = String.valueOf(System.currentTimeMillis());
    		String salt = DigestUtils.md5DigestAsHex(millis.getBytes());
    		String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
    		String hash = md5Pwd + salt;
    		String md5DigestAsHex = DigestUtils.md5DigestAsHex(hash.getBytes());
        	//插入用户账户信息
        	user.setPassword(md5DigestAsHex);
        	user.setSalt(salt);
        	user.setRegistertime(new Date());
        	user.setLastTime(new Date());
        	Integer num = userMapper.insertUser(user);
        	
        	//插入用户个人信息
            UserInfo userInfo = new UserInfo();	
            userInfo.setUserId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfoMapper.firstSaveUserInfo(userInfo);
            
            map.put("code", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
		}
		
		
		return map;
	}

	@Override
	public Map<String,Object> login(User user) {
		/**
		 *  1.用户输入[username]和[password] 
			2.系统通过用户名找到与之对应的[Hash1]和[Salt值] 
			3.系统将[Salt值]和[用户输入的密码]拼接在一起 
			4.对拼接后的值进行散列，得到[Hash2] 
			5.对比[Hash1]和[Hash2]是否相等，相等则授权登录
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
			User u = userMapper.getUserByName(user.getUsername());
			String hash = md5Pwd + u.getSalt();
			String md5DigestAsHex = DigestUtils.md5DigestAsHex(hash.getBytes());
			if(u.getPassword().equals(md5DigestAsHex)) {
				map.put("flag", true);
				map.put("userInfo", userInfoMapper.getUserInfoByUserId(u.getId()));

			}else {
				map.put("flag", false);
				map.put("msg", "用户名或密码错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", false);
			map.put("msg", "系统异常！");
		}
		
		return map;
	}

	@Override
	public User getNameById(User user) {
		User nameById = userMapper.getNameById(user);
		amqpTemplate.convertAndSend("exchange", "topic.message", JSONObject.toJSONString(nameById));
		return nameById;
	}
	
	
	/**************以下为个人信息相关操作*****************/ 	
	@Override
	public int firstSaveUserInfo(UserInfo userInfo) {
		
		return userInfoMapper.firstSaveUserInfo(userInfo);
	}

	@Override
	public UserInfo getUserInfoByUserId(Integer userId) {
		return userInfoMapper.getUserInfoByUserId(userId);
	}

	@Override
	public int updateUserInfo(UserInfo userInfo) {
		return userInfoMapper.updateUserInfo(userInfo);
	}

	@Override
	public List<UserInfo> findByName(UserInfo userinfo) {
		return userInfoMapper.findByName(userinfo);
	}

	@Override
	public User getUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAll(List<User> users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> findByPage(User user, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int inserTest(Map<String, String> map) {
		
		return userMapper.insertTest(map);
	}

	

}
