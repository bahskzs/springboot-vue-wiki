<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">

            <MailOutlined />
            <span>欢迎</span>

        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">

          <template #title>
            <span><user-outlined/>
              {{ item.name }}
            </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>

          </a-menu-item>


        </a-sub-menu>


      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <TheWelcome></TheWelcome>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">

<!--      <a-list item-layout="vertical" size="large" :grid="{  column: 2 }" :data-source="ebooks">-->
      <template #renderItem="{ item }">
        <a-list-item key="item.title">

          <template #actions>
              <span v-bind:="item">
                <component v-bind:is="'EyeOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
            <span v-bind:="item">
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            <span v-bind:="item">
                <component v-bind:is="'FolderOpenOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
          </template>
          <a-list-item-meta :description="item.description">
            <template #title>
              <router-link :to="'/doc?ebookId=' + item.id">{{ item.name }}</router-link>

            </template>
            <template #avatar>
              <a-avatar :src="item.cover"/>
            </template>
          </a-list-item-meta>

        </a-list-item>


      </template>
    </a-list>

    </a-layout-content>

  </a-layout>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {
  LikeOutlined,
  MessageOutlined,
  StarOutlined,
  FolderOpenOutlined,
  UserOutlined,
  EyeOutlined
} from "@ant-design/icons-vue";
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
import TheWelcome from "@/components/TheWelcome.vue";


export default defineComponent({

  components: {
    StarOutlined,
    LikeOutlined,
    FolderOpenOutlined,
    UserOutlined,
    EyeOutlined,
    TheWelcome

  },
  setup() {
    const isShowWelcome = ref(true);
    let level1 = ref();
    level1.value = [];
    const ebooks = ref();
    let categoryId2 = 0;


    /*
    * 列表点击
    * */
    const handleClick = (value: any) => {
      // console.log("menu click", value)
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQuery();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };

    /*
    * 查询全部电子书
    * */
    const handleQuery = () => {
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 1000,
          category2Id: categoryId2
        }
      }).then(
          (response)=>{

            const data = response.data;
            if(data.success) {
              ebooks.value = data.content.list;

            }else{
              message.error(data.message);
            }
          });
    }

    /*
    * 查询分类
    * */

    const handleCategoryQuery = () => {
      axios.get("/category/all").then(
          (response)=>{
            const data = response.data;
            if(data.success) {
              const res = data.content;
              level1.value = [];
              level1.value = Tool.array2Tree(res, 0);
              level1.value = Tool.array2Tree(res, 0);
            }else {
              message.error(data.message);
            }

          });
    };




    onMounted(() => {

      handleCategoryQuery();

    });


    return {
      ebooks,
      level1,
      isShowWelcome,
      handleClick,
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      collapsed: ref<boolean>(false),
      openKeys: ref<string[]>(['sub1']),

    };
  },
});
</script>

<style scoped>
.ant-avatar {
  width : 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}


.site-layout-background {
  background: #fff;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}
</style>
