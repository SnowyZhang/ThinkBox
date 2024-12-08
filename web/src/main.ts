import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import {Tool} from "@/util/tool";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/*
* axios 拦截器
*/
axios.interceptors.request.use(config => {
  // 在发送请求之前做些什么
  console.log('请求参数', config);
  const token = store.state.user.token;
    if(Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log('在headers增加token', config.headers.token);
    }
  return config;
},error => {
  return Promise.reject(error);
});
axios.interceptors.response.use(response => {
  console.log('响应参数', response);
    return response;
},error => {
    return Promise.reject(error);
});
const  app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const  icons: any = Icons;
for(const i in icons) {
  app.component(i, icons[i]);
}

console.log('环境',process.env.NODE_ENV);
console.log('环境服务端',process.env.VUE_APP_SERVER);