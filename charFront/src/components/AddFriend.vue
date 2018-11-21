<template>
  <Modal 
    	v-model="showAdd"
    	footer-hide
    	scrollable 
    	onVisibleChange="showAdd"
    	title="添加好友"
    	width="800"
    >
    	<Input v-model="findStr">
	        <Select v-model="findType" slot="prepend" style="width: 80px">
	            <Option value="day">账号查找</Option>
	            <Option value="month">用户名查找</Option>
	        </Select>
	        <Button type="primary" @click="findFriend" slot="append" icon="ios-search">查找</Button>
	    </Input>
    	
    	<template v-for="userInfo in findUserInfo">
		    <Card style="width:150px">
		        <p slot="title">
		            <Icon type="md-person" />
		            {{userInfo.username}}
		        </p>
		        <Poptip slot="extra" placement="right" width="400">
		            <a><Icon type="md-add-circle" :size="20"/></a>
		            <div slot="content" >
		            	<Input v-model="friendMsg" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入附加信息" /><br/>
		            	<Button @click="sendFriendMsg(userInfo.userId)" style="float:right;">发送</Button>
		            </div>
		        </Poptip>
		        <ul>
		            <li>
		                <a  target="_blank">{{ userInfo.username }}</a>
		            </li>
		        </ul>
		    </Card>
		</template>
    </Modal>
</template>

<script>
import '../assets/js/jquery1.9.1/jquery.js'
import axios from 'axios'
import qs from 'qs'
export default {
  name: 'AddFriend',
  data () {
    return {
        showAdd:false,			//显示添加好友界面
	    findStr:'',				//添加好友界面Input值
	    findType:'',			//查询类型
	    findUserInfo:[],		//查找出来的结果用户
	    friendMsg : ''			//附加信息
    }
  },
  props : ['loginUserId','showAddFriend']
  ,
  watch : {
  		showAddFriend (val){
  			this.showAdd = val;
  		}
  },
  methods : {
  		//发送添加附加信息
    	sendFriendMsg (userId) {
    		var msg = {};
    		msg.from = this.loginUserId;
    		msg.to = userId;
    		msg.content = this.friendMsg;
    		msg.type = 5;
    		
    		this.$emit("sendmsg",msg);
    	},
  	 	//查询用户
    	findFriend () {
    		var self = this;
	  		axios.get('/friend/find?findStr='+this.findStr)
	  		.then(function(response){
	  			self.findUserInfo = response.data;
	  			self.$forceUpdate();
	  		})
    	},
    	//添加好友（1通过，0拒绝）
		friendManager (type,msg) {
		  	  var self = this;
	  		  if(type == 1){ 
	  		    axios.post('/friend/add',qs.stringify({
		  			userIdA : msg.to[0],
		  			userIdB : msg.from,
		  		}))
		  		.then(function(response){
		  			//通过之后给用户发送消息提示已经成功添加
		  			var msg_send = {};
		    		msg_send.from = msg.to[0];
		    		msg_send.to = msg.from;
		    		msg_send.content = '通过了你的好友申请！！！';
		    		msg_send.type = -1;
		    		
		    		self.$emit("sendmsg",msg_send);
		    		//给自己提示
		    		self.showQuanJuMessage(msg);
		  			
		  		}) 
	  		  }
	  		  if(type == 0){
	  		  		var msg_send = {};
		    		msg_send.from = msg.to[0];
		    		msg_send.to = msg.from;
		    		msg_send.content = '对方拒绝了你的好友请求' ;
		    		msg_send.type = -1;
		    		
		    		self.$emit("sendmsg",msg_send);
	  		  }
		  },
		  
		  showQuanJuMessage (msg) {
			  	this.$Message.success({
		    		duration: 5,
	                closable: true,
	                render: h => {
	                    return h('span', [
	                        msg.content,
	                        h('a', 'render')
	                    ])
	                }
	            });
		  }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	
</style>
