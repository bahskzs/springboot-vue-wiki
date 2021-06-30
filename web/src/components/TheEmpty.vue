<template  #renderEmpty>
  <div style="text-align: center">
    <smile-outlined style="font-size: 20px" />
    <p>Data Not Found</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {DownOutlined, SmileOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  name: 'TheEmpty',
});
</script>



