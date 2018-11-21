<style scoped>
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
</style>
<template>
    <div class="layout">
        <Layout>
            <Header>
                <Menu mode="horizontal" theme="dark" active-name="1">
                    <div class="layout-logo"></div>
                    <div class="layout-nav">
                        <MenuItem name="1">
					            <Icon type="ios-paper" />
					            内容管理
					        </MenuItem>
					        <MenuItem name="2">
					            <Icon type="ios-people" />
					            用户管理
					        </MenuItem>
					        <Submenu name="3">
					            <template slot="title">
					                <Icon type="ios-stats" />
					                统计分析
					            </template>
					            <MenuGroup title="使用">
					                <MenuItem name="3-1">新增和启动</MenuItem>
					                <MenuItem name="3-2">活跃分析</MenuItem>
					                <MenuItem name="3-3">时段分析</MenuItem>
					            </MenuGroup>
					            <MenuGroup title="留存">
					                <MenuItem name="3-4">用户留存</MenuItem>
					                <MenuItem name="3-5">流失用户</MenuItem>
					            </MenuGroup>
					        </Submenu>
                    </div>
                </Menu>
            </Header>
            <Layout>
                <Sider ref="side1" hide-trigger collapsible :style="{background: '#fff'}" v-model="isCollapsed">
                    <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']" :class="menuitemClasses">
                        <Submenu name="1">
                            <template slot="title">
                                <Icon type="ios-navigate"></Icon>
                                 	我的好友 ({{contactList.length}})
                            </template>
                            <template v-for="(item,index) in contactList">
	                            <router-link :to="{path:'/message/',query:{toUserId:item.userId,fromUserId:userId}}">
	                            	<MenuItem :name="'1-'+(index+1)">
	                            		{{item.username}}{{item.notRead}}
	                            	</MenuItem>
	                            </router-link>
                            </template>
                            <MenuItem name="1-5">Option 2</MenuItem>
                            <MenuItem name="1-6">Option 3</MenuItem>
                        </Submenu>
                        <Submenu name="2">
                            <template slot="title">
                                <Icon type="ios-keypad"></Icon>
                                Item 2
                            </template>
                            <MenuItem name="2-1">Option 1</MenuItem>
                            <MenuItem name="2-2">Option 2</MenuItem>
                        </Submenu>
                        <Submenu name="3">
                            <template slot="title">
                                <Icon type="ios-analytics"></Icon>
                                Item 3
                            </template>
                            <MenuItem name="3-1">Option 1</MenuItem>
                            <MenuItem name="3-2">Option 2</MenuItem>
                        </Submenu>
                    </Menu>
                    <Icon @click.native="collapsedSider" :class="rotateIcon" style="float:right;padding-right:8px" type="md-menu" size="24"></Icon>
                </Sider>
                <Layout :style="{padding: '0 0px 0px'}">
                    <!--<Breadcrumb :style="{margin: '24px 0'}">
                        <BreadcrumbItem>Home</BreadcrumbItem>
                        <BreadcrumbItem>Components</BreadcrumbItem>
                        <BreadcrumbItem>Layout</BreadcrumbItem>
                    </Breadcrumb>-->
                    <Content :style="{padding: '0px', minHeight: '1000', background: '#fff'}">
                    <input type="text" v-model="userId">
    <Button @click="initWebpack">showAlert</Button>
                        <router-view @add-comment="appendComment" @sendContent="change" v-bind:contentDate="offlineMsgList"/>
                        <Message></Message>
                    </Content>
                </Layout>
            </Layout>
        </Layout>
    </div>
</template>
<script>
import axios from 'axios'
export default {
     data () {
        return {
        	websock : [],
            userId : null,
            isCollapsed: false,
            contactList: [],
            msgCont : null,
            messageConents : [],
            offlineMsgList : []
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
    methods: {
    	getOfflineMessageList(userId){
    		var self = this;
    		$.ajax({
    			url:'/msg/getOfflineMessageList?userId='+userId,
    			type:'GET',
    			async:false, 
    			dataType : 'json',
    			success:function(data){
    				console.log(data);
	    			self.offlineMsgList = data;
    			}
    		});
    	},
    	appendComment (item) {
    		console.log("item");
    		console.log(item)
      		this.messageConents = item;
      		self.$forceUpdate();	
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
		    const wsuri = "ws://localhost:8086/socketserver/"+this.userId;
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
		    console.log(self.userId);
		    //得到Message的JSon串
		    var msg = $.parseJSON(e.data);
		    self.getOfflineMessageList(self.userId);
		    
		    console.log(self.contactList);
		    console.log("self.offlineMsgList");
		    console.log(self.offlineMsgList);
		    console.log(msg);
		    //循环联系人
		    $.each(self.contactList,function(indexs,item){
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
		    console.log(self.contactList);
			self.$forceUpdate();		    
		  },
		  websocketclose(){  //关闭
		    console.log("WebSocket关闭");
		  },
		  websocketerror(){  //失败
		    console.log("WebSocket连接失败");
		  }
    },
    mounted : function(){
    	this.initContact();
    	//this.connect();
    }
}
</script>
