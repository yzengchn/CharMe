package com.yzeng.charme.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzeng.charme.entity.Message;
import com.yzeng.charme.entity.User;
import com.yzeng.charme.entity.UserInfo;
import com.yzeng.charme.mapper.MessageMapper;
import com.yzeng.charme.service.GroupService;
import com.yzeng.charme.service.MessageService;
import com.yzeng.charme.service.UserService;
import com.yzeng.charme.utils.SpringUtils;

@ServerEndpoint(value = "/socketserver/{userId}")
@Component
public class SocketServer {
	//表示与某个用户的连接会话，通过它给客户端发送数据
	private Session session;
	//在线人数
	private static Integer onlineCount = 0;
	
	private static Integer id = 0; 
	//客户端对象列表
	private static Map<UserInfo, SocketServer> clients = new ConcurrentHashMap<UserInfo,SocketServer>();
	//用户与websocket绑定session
	private static Map<Integer,Session> userPool = new HashMap<Integer,Session>();
	//当前用户
	private UserInfo user;
	
	//你要注入的service或者dao
	private UserService userService;
	private MessageService messageService;
	private GroupService groupService;
	
	/**
    * 用户连接时触发
    * @param session
    * @param userid
    */
	@OnOpen
	public void open(Session session,@PathParam(value="userId")Integer userId) {
		this.session = session;
		
		userService = SpringUtils.getBean(UserService.class);
		messageService = SpringUtils.getBean(MessageService.class);
		groupService = SpringUtils.getBean(GroupService.class);
		
		//获得登录用户个人信息
		user = userService.getUserInfoByUserId(userId);
		//更新在线状态
		user.setIsOnline(1);
		userService.updateUserInfo(user);
		//增加在线人数
		addOnlineCount();
		//放入客户端Map
		clients.put(user, this);
		//放入用户池
		userPool.put(userId, session);
		//检查自己是否有未接收的消息
		List<Message> messageList = new ArrayList<Message>();
		messageList = messageService.getOfflineMessageList(userId);
		if(messageList != null && messageList.size() > 0) {
			for (Message message : messageList) {
				String jsonMessage = getMessage(message);
				//发送特定消息
				singleSend(jsonMessage, userPool.get(userId));
				message.setIsOffline("N");
				messageService.updateMessage(message);
			}
		}
				
		System.out.println(userId+"已连接");
	}
	
	/**
    * 收到信息时触发
    * @param message 客户端发送过来的消息
    * @param session 可选的参数
	 * @throws IOException 
    */		
	@OnMessage
	public void onMessage(String message,Session session) throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Message msg = new Message();
		JSONObject jsonMsg = JSON.parseObject(message); 
		Integer type = jsonMsg.getIntValue("type");
		Integer from = jsonMsg.getIntValue("from");
		Integer to = jsonMsg.getIntValue("to");
		String content = jsonMsg.getString("content");
		msg.setMsgType(type);
		msg.setContent(content);
		msg.setFromUserId(from);
		msg.setToUserId(to);
		msg.setSendMsgTime(new Date());
		
		sendMessage(msg);
		System.out.println("当前发送人sessionid为"+session.getId()+"发送内容为"+message);
	}
	
	/**
    * 连接关闭触发
    */
	@OnClose
	public void onClose() {
		//客户端移除
		clients.remove(user);  
		//用户池移除
		userPool.remove(user.getUserId());
		//在线人数减一
        subOnlineCount();  
        //将用户的在线状态设为0
        user.setIsOnline(0);
        //更新用户个人信息状态
		userService.updateUserInfo(user);
	}
	
	/**
    * 发生错误时触发
    * @param session
    * @param error
    */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
	/**
	 * 发送消息
	 * @param message
	 * @throws IOException
	 */
    public void sendMessage(Message message) throws IOException {
    	Session toSession = userPool.get(message.getToUserId());
    	switch (message.getMsgType()) {
    	case -1://不给发送方（自己）发消息
    		if(toSession != null && toSession.isOpen()) {
	    		toSession.getBasicRemote().sendText(getMessage(message));
	    	}
			break;
		case 1://普通消息
			//判断是否在线
	    	if(toSession != null && toSession.isOpen()) {
	    		message.setIsOffline("N");
	    		messageService.insertMsg(message);
	    		
	    		toSession.getBasicRemote().sendText(getMessage(message));
	    	}else {
	    		//将消息写到DB,标记为离线消息
	    		message.setIsOffline("Y");
	    		messageService.insertMsg(message);
	    	}
			break;
		case 2://群信息
			message.setIsOffline("N");
			messageService.insertMsg(message);
			//查询该群所有成员-遍历-给登录的用户发送消息
			List<Map<String,Object>> list = groupService.getGroupUserByGroupId(message.getToUserId());
			for (Map<String, Object> map : list) {
				int userId = Integer.parseInt(map.get("userId").toString());
				//不包括自己
				if(userId != message.getFromUserId()) {
					Session sessionUser = userPool.get(userId);
					if(sessionUser != null && sessionUser.isOpen()) {
						sessionUser.getBasicRemote().sendText(getMessage(message));
					}
				}
			}
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		case 5://好友申请信息
			//判断是否在线
	    	if(toSession != null && toSession.isOpen()) {
	    		toSession.getBasicRemote().sendText(getMessage(message));
	    	}
			break;
		case 6:
			
			break;
		
		}
    	
    }
    
    public void sendMessageTo(String message, String To) throws IOException {  
//        for (SocketServer item : clients.values()) { 
//            if (item.user.getUsername().equals(To) ) {
//            	System.out.println(JSON.toJSONString(user.getMsg().get(0)));
//				String jsonString = JSON.toJSONString(user.getMsg().get(0));
//				item.session.getAsyncRemote().sendText(jsonString);  
//			}
//        }  
    	SocketServer server = clients.get(user);
    	server.session.getAsyncRemote().sendText(message);
    	System.out.println(server);
    }  
      
    public void sendMessageAll(String message) throws IOException {  
        for (SocketServer item : clients.values()) {  
            item.session.getAsyncRemote().sendText(message);  
        }  
    }  
    
    /**
     * 对特定用户发送消息
     * @param message
     * @param session
     */
    public void singleSend(String message, Session session){
        try {
            session.getBasicRemote().sendText(message);
            System.out.println("特定消息:"+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
    /**
     * 根据Message实体组装Json格式的数据返回给前台
     */
	public String getMessage(Message message){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fromName = userService.getUserInfoByUserId(message.getFromUserId()).getUsername();
		//使用JSONObject方法构建Json数据
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("from", String.valueOf(message.getFromUserId()));
        jsonObjectMessage.put("fromName", fromName);
        jsonObjectMessage.put("to", new String[] {String.valueOf(message.getToUserId())});
        jsonObjectMessage.put("content", String.valueOf(message.getContent()));
        jsonObjectMessage.put("type", String.valueOf(message.getMsgType()));
        jsonObjectMessage.put("time", formatter.format(message.getSendMsgTime()));
        return jsonObjectMessage.toString();
    }
    
    public static synchronized int getOnlineCount() {  
        return onlineCount;  
    }  
  
    public static synchronized void addOnlineCount() {  
    	SocketServer.onlineCount++;  
    }  
  
    public static synchronized void subOnlineCount() {  
    	SocketServer.onlineCount--;  
    }  
	
	
	
}
