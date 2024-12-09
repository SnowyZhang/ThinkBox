<template>
  <a-layout-footer style="text-align: center">
    ThinkBox © 2024 | 赋能知识，助力成长
    「学习 · 构建 · 分享」
  </a-layout-footer>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted} from 'vue';
import {useRef} from "preact/compat";
import store from "@/store";
import {Tool} from "@/util/tool";
import { notification } from 'ant-design-vue';

export default defineComponent({
  name: 'the-footer',
  setup () {
    let websocket :any
    let token : any
    const onOpen = () => {
      console.log('websocket open, state code:', websocket.readyState)
      // websocket.send(JSON.stringify({type: 'auth', token: token}))
    }
    const onClose = () => {
      console.log('websocket close, state code:', websocket.readyState)
    }
    const onMessage = (e: any) => {
      console.log('websocket message', e.data)
      notification['success']({
        message: '收到消息',
        description: e.data
      });
    }
    const onError = (e: any) => {
      console.log('websocket error, state code:', websocket.readyState)
    }
    const initWebSocket = () =>{

      websocket.onopen = onOpen
      websocket.onclose = onClose
      websocket.onmessage = onMessage
      websocket.onerror = onError
    }
    onMounted(()=>{
      if('WebSocket' in window) {
        token = Tool.uuid(10);
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token)
        initWebSocket()

        // websocket.close()
      }else{
        alert('浏览器不支持WebSocket')
      }
    })
    return {
      websocket
    }
  }
});
</script>