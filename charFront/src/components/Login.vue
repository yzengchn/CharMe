<template>
  <div class="hello">
  <input type="text" id="str">
    <Button @click="showAlert()">showAlert</Button>
  </div>
</template>

<script>
import '../assets/js/jquery1.9.1/jquery.js'
import axios from 'axios'
export default {
  name: 'Login',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods : {
  	showAlert : function(){
  		alert($("#str").val());
  		axios.get('/user/get/1')
  		.then(function(response){
  			console.log(response);	
  		})
  		.catch(function (error) {
		    console.log(error);
		});
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
