<template>
  <a-layout-footer style="text-align: center">
    Personal wiki ©2022 Created by bahskzs <span v-show="user.id">, 欢迎 {{ user.name }}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import store from '@/store';
import {defineComponent, computed} from 'vue';

export default defineComponent({
  name: 'TheFooter',
  setup() {
    const user = computed(() => store.state.user)

    return {
      user
    }
  }
});
</script>

<style>
.logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>



