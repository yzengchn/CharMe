// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import FriendsList from './components/FriendsList'
import GroupList from './components/GroupList'
import Message from './components/Message'
import GroupMessage from './components/GroupMessage'
import AddFriend from './components/AddFriend'
import UserInfo from './components/UserInfo'
import router from './router'

import iView from 'iview'
import 'iview/dist/styles/iview.css'    // 使用 CSS

Vue.use(iView)

//全局注册组件
Vue.component("FriendsList",FriendsList)
Vue.component("GroupList",GroupList)
Vue.component("Message",Message)
Vue.component("GroupMessage",GroupMessage)
Vue.component("AddFriend",AddFriend)
Vue.component("UserInfo",UserInfo)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
