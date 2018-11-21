package com.yzeng.charme.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yzeng.charme.entity.User;
import com.yzeng.charme.entity.UserInfo;
import com.yzeng.charme.service.UserService;
import com.yzeng.charme.utils.WebResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
    private UserService userService;
	@ApiOperation(value="用户注册", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	@RequestMapping(value="register",method=RequestMethod.POST)
	public WebResult register(String username,String password) {
		WebResult wr = new WebResult();
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		Map<String, String> map = userService.register(user);
		wr.setData(map);
		return wr;
	}
	
	
	@RequestMapping("login")
	public String login(String username,String password,HttpSession session) {
		User user = new User();
    	user.setUsername(username);
    	user.setPassword(password);
    	Map<String, Object> map = userService.login(user);
    	if(map != null && map.size() > 0) {
    		boolean flag = (boolean) map.get("flag");
    		if(flag) {
    			session.setAttribute("user", map.get("userInfo"));
    			return "index";
    		}else {
    			return "login";
    		}
    	}else {
    		return "login";
    	}
	}
	
	@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "INTEGER")
    @RequestMapping("/get/{id}")
    @ResponseBody
    public User getUser(@PathVariable int id) {
    	//User user = userService.getUser(id);
    	User user = new User();
    	user.setId(id);
    	User user1 = userService.getNameById(user);
        return user1;
    }
    
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id) {
        userService.remove(id);
        return "delete sucess";
    }

    @RequestMapping("/add")
    @ResponseBody
    public String insert(String username,String password) {
    	User user = new User();
    	user.setUsername(username);
    	user.setPassword(password);
        userService.insert(user);
        return "sucess";
    }

    @RequestMapping("/insert")
    @ResponseBody
    public String insertAll() {
        List<User> list = new ArrayList<>();
        for (int i = 10; i < 15; i++) {
            //list.add(new User(i, "" + i, i));
        }
        userService.insertAll(list);
        return "sucess";
    }

    @RequestMapping("/find/all")
    @ResponseBody
    public List<UserInfo> find(){
        return userService.findAll();
    }

    @RequestMapping("/find/{start}")
    @ResponseBody
    public List<User> findByPage(@PathVariable int start,User user){
        Pageable pageable=new PageRequest(start, 2);
        return userService.findByPage(user, pageable);
    } 

    @RequestMapping("/update/{id}")
    @ResponseBody
    public String update(@PathVariable int id){
        User user =new User();
        userService.update(user);
        return "sucess";
    }
    
    @RequestMapping("/test")
    @ResponseBody
    public String testId(){
    	Map<String,String> map=new HashMap<String,String>();
        map.put("id", null);
        map.put("description", "123456");
        userService.inserTest(map);
    	return "sucess";
    }
    
    
}
