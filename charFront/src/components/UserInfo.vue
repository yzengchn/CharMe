<template>

</template>
<script>
export default {
	name: 'UserInfo',
	data () {
	    return {
	        showUserInfo: false,
	        
	    }
	},
	props : ['loginUserId','showUserRelation']
    ,
    watch : {
  		showUserRelation (val){
  			this.showUserInfo = val;
  		}
    },
}
</script>
<style scoped>
    
</style>
