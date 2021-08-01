<template>
  <a-layout-header class="header">
    <div class="logo">橘猫知识库</div>
    <a-row>
      <a-col :span="20">

        <a-menu
            theme="dark"
            mode="horizontal"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="/home">
            <router-link to="/">首页</router-link>
          </a-menu-item>
          <a-menu-item key="/about" :style="!user.id? '': {display:'none'}">
            <router-link to="/about">关于我们</router-link>
          </a-menu-item>


          <template v-for="item in list" :key="item.key">
            <template v-if="!item.children">
              <a-menu-item :key="item.key">
                <router-link :to="item.key">{{ item.title }}111</router-link>
              </a-menu-item>
            </template>
          </template>
          <!--          <a-menu-item key="/admin/user" :style="!!user.id? '' : {display:'none'}">-->
          <!--            <router-link to="/admin/user">用户管理</router-link>-->
          <!--          </a-menu-item>-->
          <!--          <a-menu-item key="/admin/ebook" :style="!!user.id? {} : {display:'none'}">-->
          <!--            <router-link to="/admin/ebook">电子书管理</router-link>-->
          <!--          </a-menu-item>-->
          <!--          <a-menu-item key="/admin/category" :style="!!user.id? {} : {display:'none'}">-->
          <!--            <router-link to="/admin/category">分类管理</router-link>-->
          <!--          </a-menu-item>-->
          <a-menu-item key="/about" :style="!!user.id? {} : {display:'none'}">
            <router-link to="/about">关于我们</router-link>
          </a-menu-item>


        </a-menu>
      </a-col>
      <a-col :span="4">
        <a-popconfirm
            title="确认退出登录?"
            ok-text="是"
            cancel-text="否"
            @confirm="logout()"
        >
          <a class="login-menu" v-show="user.id">
            <span>退出登录</span>
          </a>
        </a-popconfirm>
        <a class="login-menu" @click="showLoginModal" v-show="!user.id">
          <span>登录</span>
        </a>
        <a class="login-menu" v-show="user.id">
          <span>您好 : {{ user.name }}  </span>
        </a>


      </a-col>
    </a-row>


    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import store from '@/store';
import router from '@/router';

declare let hexMd5: any;
declare let KEY: any;
//TODO 考虑将现在由于display none导致不显示的菜单改造为动态菜单

export default defineComponent({
  name: 'TheHeader',
  setup() {
    const loginUser = ref({
      loginName: "test",
      password: "test123456"
    });


    const user = computed(() => store.state.user);

    let list = ref();
    list.value = [];


    const onLoadMenu = (user: string) => {
      if (user) {
        list.value = [
          {
            key: '/admin/user',
            title: '用户管理'
          }, {
            key: '/admin/ebook',
            title: '电子书管理'
          }, {
            key: '/admin/category',
            title: '分类管理'
          }
        ];
        console.log("userid", user);
        console.log("list", list.value);
      } else {
        list.value = [];
      }
    }


    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    const showLoginModal = () => {
      loginModalVisible.value = true;
    }

    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser", data.content);
          onLoadMenu(loginUser.value.loginName);
        } else {
          message.error(data.message);
        }
      });

    }

    const logout = () => {
      console.log("退出登录开始");

      axios.get('/user/logout/' + user.value.token, {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "*",
          "Access-Control-Allow-Headers": "*"
        }, params: {
          token: user.value.token
        }
      }).then((response) => {

        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser", {});
          onLoadMenu(null);

        } else {
          message.error(data.message);
        }

        setTimeout(() => {
          router.replace("/");
        }, 500);


      });
    };


    return {
      loginUser,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      login,
      list,
      user,
      logout
    }
  }
});
</script>

<style scoped>
.login-menu {
  float: right !important;
  color: white;
  padding-left: 10px;
}

.logo {
  float: left;
  width: 120px;
  height: 31px;
  /*margin: 16px 24px 16px 0;*/
  color: white;
  font-size: 18px;
  /*background: rgba(255, 255, 255, 0.3);*/
}


</style>

