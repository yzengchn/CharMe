
<template>
    <div class="layout">
    <input type="text" v-model="userId">
    				<Button @click="initWebpack">showAlert</Button>
        <Layout>
            <Header>
                <AddFriend 
                	ref="addFriend"
                	v-bind:loginUserId="userId"
                	v-bind:showAddFriend="showAddFriend"
                	@sendmsg="sendmsg" />
            </Header>
            <Layout>
                <Sider width="201" ref="side1" hide-trigger collapsible :style="{background: '#fff'}" v-model="isCollapsed">
                	<Tabs type="card" style="padding-left:7px">
				        <TabPane label="好友" icon="ios-contact">
				        	<FriendsList
		                    	v-bind:loginUserId="userId" 
		                    	v-bind:friendsList="contactList"
		                    />
		                </TabPane>
				        <TabPane label="群组" icon="md-contacts">
				       		 <GroupList 
				       		 	v-bind:loginUserId="userId"
				       		 />
				        </TabPane>
				    </Tabs>
	                	
                    
                    <Button @click="showAddFriend = true">添加好友</Button>
                    <Icon @click.native="collapsedSider" :class="rotateIcon" style="float:right;padding-right:8px" type="md-menu" size="24"></Icon>
                </Sider>
                <Layout :style="{padding: '0 0px 0px'}">
                
                    <!--<Breadcrumb :style="{margin: '24px 0'}">
                        <BreadcrumbItem>Home</BreadcrumbItem>
                        <BreadcrumbItem>Components</BreadcrumbItem>
                        <BreadcrumbItem>Layout</BreadcrumbItem>
                    </Breadcrumb>-->
                    <Content :style="{padding: '0px', minHeight: '1000', background: '#ebebeb'}">
                    
                        <router-view 
                        	@sendContent="change" 
                        	v-bind:contentDate="offlineMsgList"
                        	v-bind:messageConent="messageConent"
                        />
                    </Content>
                </Layout>
            </Layout>
        </Layout>
    </div>
</template>
<script>
import axios from 'axios'
import qs from 'qs'
export default {
     data () {
        return {
        	websock : [], 			//websocket连接
            userId : '',			//登录用户ID
            isCollapsed: false,
            contactList: {},		//联系人列表
            msgCont : [],
            messageConent : [],		//接受到的socket消息
            offlineMsgList : [],	
           	//--------------添加好友相关--------------
            showAddFriend:false,	//显示添加好友界面
        }
    },
    computed: {
        rotateIcon () {
            return [
                'menu-icon',
                this.isCollapsed ? 'rotate-icon' : ''
            ];
        },
        menuitemClasses () {
            return [
                'menu-item',
                this.isCollapsed ? 'collapsed-menu' : ''
            ]
        }
    },
    watch : {
    	
    },
    methods: {
    	getOfflineMessageList(userId){
    		var self = this;
    		$.ajax({
    			url:'/msg/getOfflineMessageList?userId='+userId,
    			type:'GET',
    			async:false, 
    			dataType : 'json',
    			success:function(data){
	    			self.offlineMsgList = data;
    			}
    		});
    	},
    	
    	sendmsg (msg) {
    		var msgParam = {
		   		from : msg.from,
				to : msg.to,
				content : msg.content,
				type : msg.type,
		    }
    		this.websock.send(JSON.stringify(msgParam));
    	},
	    change(msg) {
	    	this.sendmsg(msg);
	    },
    	initContact () {
    		var self = this;
    		axios.get('/user/find/all')
    		.then(function(response){
    			self.contactList = response.data;
    		})
    	},
        collapsedSider () {
            this.$refs.side1.toggleCollapse();
        },
		        
		initWebpack(){//初始化websocket
		    const wsuri = "ws://139.199.86.201:8086//socketserver/"+this.userId;
		    this.websock = new WebSocket(wsuri);//这里面的this都指向vue
		    console.log(this.websock);
		    this.websock.onopen = this.websocketopen;
		    this.websock.onmessage = this.websocketonmessage;
		    this.websock.onclose = this.websocketclose;
		    this.websock.onerror = this.websocketerror;
		  },
		  websocketopen(){//打开
		    console.log("WebSocket连接成功")
		  },
		  websocketonmessage(e){ //数据接收
		    var self = this;
		    //得到Message的JSon串
		    var msg = $.parseJSON(e.data);
		    console.log(msg);
		    //判断消息类型:5好友申请 6加群邀请 7加群申请
		    if(msg.type == 5 || msg.type == 6 || msg.type == 7){
		    	var title = '';
		    	var content = ''
		    	if(msg.type == 5){
		    		title = '好友申请';
		    		content = msg.fromName + ' 申请添加您为好友'
		    	}
		    	if(msg.type == 6){
		    		title = '加群邀请';
		    		content = msg.fromName + ' 邀请你加入群'
		    	}
		    	if(msg.type == 7){
		    		title = '加群申请';
		    		content = msg.fromName + ' 申请入群'
		    	}
		    	this.$Notice.info({
		    		name : msg.type+msg.from+msg.to[0],
                    title: title,
                    duration : 0,
                    render: h => {
                        return h('div', [ 
                        	h('h3', content),
                        	h('div', '附加信息：'+msg.content),
                            h('Button',{
                            	style : {
                            		margin: '10px 7px -5px'
                            	},
                            	props : {
                            		type : 'success'
                            	},
                            	on : {
                            		click : ()=>{
                            			self.actionManager(1,msg);
                            		}
                            	}
                            }, '同意'),
                            h('Button',{
                            	style : {
                            		margin: '10px 7px -5px'
                            	},
                            	props : {
                            		type : 'primary'
                            	},
                            	on : {
                            		click : ()=>{
                            			self.actionManager(0,msg);
                            		}
                            	}
                            }, '拒绝')
                        ])
                    }
                });
		    }else if(msg.type == 1){
			    //离线信息
			    self.getOfflineMessageList(self.userId);
			    
			    console.log("self.offlineMsgList");
			    console.log(self.offlineMsgList);
			    //循环联系人
			    $.each(self.contactList,function(indexs,item){
			    	if(msg.from == item.userId){
			    		self.messageConent = msg;
			    	}
			    	$.each(self.offlineMsgList,function(index,msgItem){
			    		if(msgItem.fromUserId == item.userId){
			    			if(item.notRead != null){
			    			
			    				if(self.offlineMsgList.length == 1){
				    				item.notRead = 1;
			    				}
			    				if(self.offlineMsgList.length > item.notRead){
				    				item.notRead ++;
			    				}
			    			}else{
			    				item.notRead = 1;
			    			}
			    		}
			    	});
			    	
			    });
			    console.log(self.messageConent);
			    console.log(self.contactList);
				self.$forceUpdate();
				
			//除自己消息	
		    }else if(msg.type == -1){
		    	console.log(msg);
		    	this.showQuanJuMessage(msg);
		    //群消息
		    }else if(msg.type == 2){
		    	self.messageConent = msg;
		    }
		    		    
		  },
		  websocketclose(){  //关闭
		    console.log("WebSocket关闭");
		  },
		  websocketerror(){  //失败
		    console.log("WebSocket连接失败");
		  },
		  
		  //添加好友（1通过，0拒绝）
		  actionManager (type,msg) {
		  	  if(msg.type == 5){
				  this.$refs.addFriend.friendManager(type,msg) ; 		  
		  	  }
	  		  this.$Notice.close(msg.type+msg.from+msg.to[0]);
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
		  },
		  testRouter (from, to) {
		  	alert(from);
		  		var url = '/message?toUserId=' + from + '&fromUserId=' + to;
		  		this.$router.push({path: '/message', query:{toUserId:from,fromUserId:to}});
		  }
    },
    mounted : function(){
    	this.initContact();
    }
}
</script>

<style>
.layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
}
.layout-logo{
    width: 100px;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
}
.layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
}
.ivu-tabs-bar{
	margin-bottom: 0px;
}
.chat-sender{
      clear:both;
      font-size: 80%;
    }
    .chat-sender div:nth-of-type(1){
      float: left;
    }
    .chat-sender div:nth-of-type(2){
      margin: 0 50px 2px 50px;
      padding: 0px;
      color: #848484;
      font-size: 70%;
      text-align: left;
    }
    .chat-sender div:nth-of-type(3){
      background-color: white;
      /*float: left;*/
      margin: 0 50px 10px 50px;
      padding: 10px 10px 10px 10px;
      border-radius:7px;
      text-indent: -12px;
    }

    .chat-receiver{
      clear:both;
      font-size: 80%;
    }
    .chat-receiver div:nth-of-type(1){
      float: right;
    }
    .chat-receiver div:nth-of-type(2){
      margin: 0px 50px 2px 50px;
      padding: 0px;
      color: #848484;
      font-size: 70%;
      text-align: right;
    }
    .chat-receiver div:nth-of-type(3){
      /*float:right;*/
      background-color: #b2e281;
      margin: 0px 50px 10px 50px;
      padding: 10px 10px 10px 10px;
      border-radius:7px;
    }

    .chat-receiver div:first-child img,
    .chat-sender div:first-child img{
      width: 40px;
      height: 40px;
      /*border-radius: 10%;*/
    }

    .chat-left_triangle{
      height: 0px;
      width: 0px;
      border-width: 6px;
      border-style: solid;
      border-color: transparent white transparent transparent;
      position: relative;
      left: -22px;
      top: 3px;
    }
    .chat-right_triangle{
      height: 0px;
      width: 0px;
      border-width: 6px;
      border-style: solid;
      border-color: transparent transparent transparent #b2e281;
      position: relative;
      right:-22px;
      top:3px;
    }

    .chat-notice{
      clear: both;
      font-size: 70%;
      color: white;
      text-align: center;
      margin-top: 15px;
      margin-bottom: 15px;
    }
    .chat-notice span{
      background-color: #cecece;
      line-height: 25px;
      border-radius: 5px;
      padding: 5px 10px;
    }
</style>
