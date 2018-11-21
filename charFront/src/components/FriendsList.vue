<template>
    <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
        <Submenu name="1">
            <template slot="title">
                <Icon type="ios-navigate"></Icon>
                 	我的好友 ({{friendsList.length}})
            </template>
            <template v-for="(item,index) in friendsList">
                	<MenuItem :name="'1-'+(index+1)" :to="{path:'/message/',query:{toUserId:item.userId,fromUserId:loginUserId}}">
                		<Avatar src="https://i.loli.net/2017/08/21/599a521472424.jpg" />&nbsp;{{item.username}}{{item.notRead}}
                	</MenuItem>
            </template>
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
</template>

<script>
import '../assets/js/jquery1.9.1/jquery.js'
import axios from 'axios'
export default {
  name: 'FriendsList',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  props : ['loginUserId','friendsList']
  ,
  methods : {
  	  
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	
</style>
