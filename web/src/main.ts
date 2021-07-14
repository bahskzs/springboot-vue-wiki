import {createApp} from 'vue';
import Antd from 'ant-design-vue';
import App from './App.vue';
import router from './router';
import store from './store';
import 'ant-design-vue/dist/antd.css';
import axios from "axios";
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';


axios.defaults.baseURL = process.env.VUE_APP_SERVER;


createApp(App).use(Antd).use(store).use(router).use(mavonEditor).mount('#app')


new App({
    'el': '#main',
    data() {
        return {value: ''}
    }
})

console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER)


/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    const response = error.response;
    const status = response.status;
    return Promise.reject(error);
});
