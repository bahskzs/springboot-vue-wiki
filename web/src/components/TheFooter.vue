<template>
  <a-layout-footer style="text-align: center">
    Personal wiki ©2022 Created by bahskzs <span v-show="user.id">, 欢迎 {{ user.name }}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import store from '@/store';
import {Tool} from '@/util/tool';
import {defineComponent, computed, onMounted} from 'vue';
import {message, notification} from "ant-design-vue";

export default defineComponent({
  name: 'TheFooter',
  setup() {
    const user = computed(() => store.state.user)
    let websocket: any;
    let token: any;

    const onOpen = () => {
      console.log('WebSocket连接成功，状态码：', websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log('WebSocket收到消息：', event.data);
      console.log("收到的提示  ", event.data)
      notification['success']({
        message: '收到消息',
        description: event.data,
      });

    };
    const onError = () => {
      console.log('WebSocket连接错误，状态码：', websocket.readyState)
    };
    const onClose = () => {
      console.log('WebSocket连接关闭，状态码：', websocket.readyState)
    };

    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(() => {
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket()
      } else {
        message.error("当前浏览器不支持");
      }
    })


    return {
      user,
    }
  }
});
</script>

<style>


.site-layout-background {
  background: #fff;
}
</style>



