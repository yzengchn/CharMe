<template>
  <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
        <MenuGroup title="我创建的">
            <template v-for="(item,index) in groupList.slefGroup">
                	<MenuItem :name="'1-'+(index+1)" :to="{path:'/group/',query:{toGroupId:item.id,fromUserId:loginUserId}}">
                		<Icon type="md-chatbubbles" />
                		{{item.groupName}}
                	</MenuItem>
            </template>
            
        </MenuGroup>
        <MenuGroup title="我加入的">
            <template v-for="(item,index) in groupList.joinGroup">
                	<MenuItem :name="'2-'+(index+1)" :to="{path:'/group/',query:{toGroupId:item.id,fromUserId:loginUserId}}">
                		<Icon type="md-chatbubbles" />
                		{{item.groupName}}
                	</MenuItem>
            </template>
        </MenuGroup>
   
    </Menu>
</template>

<script>
import '../assets/js/jquery1.9.1/jquery.js'
import axios from 'axios'
export default {
  name: 'GroupList',
  data () {
    return {
    	groupList : {}
    }
  },
  props : ['loginUserId']
  ,
  watch : {
  	loginUserId (val) {
  		if(val != null && val != ''){
	  		this.initGroupList (val);
  		}
  	}
  },
  methods : {
  		initGroupList (userId) {
  			var self = this;
    		axios.get('/group/getAll?userId='+userId)
    		.then(function(response){
    			self.groupList = response.data.data;
    		})
  		}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
