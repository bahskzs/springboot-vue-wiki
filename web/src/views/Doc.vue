<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree v-if="level1 && level1.length"
                  :tree-data="level1"
                  :replaceFields="{title:'name', key:'id', value: 'id'}"
                  :defaultExpandAll="true"
                  @select="onSelect"
                  :defaultSelectedeKey="defaultSelectedKey"
          >

          </a-tree>
        </a-col>
        <a-col :span="18">
          <div v-if="level1.length != 0">
            <h2>{{ doc.name }}</h2>
            <div>
              <span>阅读数：{{ doc.viewCount }}</span> &nbsp; &nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
            <a-divider style="height: 1px; background-color: #f0f2f5"/>
          </div>
          <div class="wangeditor" :innerHTML="content"></div>
          <div class="vote-div" v-if="level1.length != 0">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon>
                <LikeOutlined/> &nbsp;点赞：{{ doc.voteCount }}
              </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, createVNode} from 'vue';
import axios from 'axios';
import {message, notification} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {

    //路由
    const route = useRoute();

    //左侧的目录
    const level1 = ref();
    level1.value = [];

    //初始化定义选择的节点
    const defaultSelectedKey = ref();
    defaultSelectedKey.value = [];

    const doc = ref();
    doc.value = {};


    /**
     * @author: bahsk
     * @date: 2021/7/11 16:05
     * @description: 左侧点击右侧事件
     * @params:
     * @return:
     */

    const onSelect = (selectedKeys: any, info: any) => {
      //点击调用右侧的查询接口
      if (Tool.isNotEmpty(selectedKeys)) {
        handleQueryContent(selectedKeys[0]);
        // 选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        console.log("tree-id:", selectedKeys[0]);
      }


    };


    /**
     * @author: bahsk
     * @date: 2021/7/11 15:59
     * @description: 右侧文档内容
     * @params: 会接受左侧点击的doc的id
     * @return:
     */

    const content = ref();

    const handleQueryContent = (doc: number) => {
      axios.get("/doc/find-content/" + doc).then(
          (response) => {
            const data = response.data;
            if (data.success) {
              content.value = data.content;

            } else {
              message.error(data.message);
            }
          });
    };


    /**
     * 左侧目录树
     **/
    const handleQuery = () => {
      axios.get("/doc/all/" + route.query.ebookId).then(
          (response) => {
            const data = response.data;
            if (data.success) {

              const res = data.content;

              level1.value = [];
              level1.value = Tool.array2Tree(res, 0);

              if (Tool.isNotEmpty(level1)) {
                defaultSelectedKey.value = [level1.value[0].id];
                handleQueryContent(level1.value[0].id);
                doc.value = level1.value[0];
              }

            } else {
              message.error(data.message);
            }

          });
    };

    /*
    * 点赞
    * */
    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
        } else {
          // message.error(data.message);

          notification['error']({
            message: '收到消息',
            description: '您已点赞过',
          });

        }
      });
    }


    onMounted(() => {
      handleQuery();
    });

    return {
      content,
      level1,
      onSelect,
      defaultSelectedKey,
      doc,
      vote


    }
  }
});
</script>

<style>
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>
