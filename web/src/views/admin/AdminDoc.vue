<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <a-row :gutter="24">
      <a-col :span="6">
        <p>
          <a-button type="primary" @click="add()" size="small">
            新增
          </a-button>
        </p>
        <a-table
            v-if="level1 && level1.length"
            :row-key="record => record.id"
            :columns="columns"
            :data-source="level1"
            :pagination="false"
            :defaultExpandAllRows="true"
            :loading="loading"
            size="small"
        >
          <template #name="{ text, record }">
            {{ record.sort }} - {{ text }}
          </template>
          <!--defaultExpandAllRows 通常要搭配v-if,数据存在才展开,不然不生效-->
          <template #action="{ record }">

      <span>
        <a-button type="primary" @click="editModal(record)" size="small" shape="round">编辑</a-button>

        <a-divider type="vertical"/>
        <a-popconfirm
            title="删除后不可恢复,确认删除"
            ok-text="是"
            cancel-text="否"
            @confirm="handleDelete(record.id)"
        >
            <a-button type="danger" size="small" shape="round">删除</a-button>
          </a-popconfirm>
        </span>
          </template>

        </a-table>

      </a-col>
      <a-col :span="18">
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-button type="primary" @click="handleSave()" size="small">
                保存
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-form :model="doc" layout="vertical">

          <a-form-item>
            <a-input v-model:value="doc.name" placeholder="名称"/>
          </a-form-item>
          <a-form-item>
            <a-tree-select
                v-model:value="doc.parent"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto'}"
                :tree-data="treeSelectData"
                placeholder="选择父文档"
                tree-default-expand-all
                show-search
                :replaceFields="{title:'name', key:'id', value: 'id'}"

            >
            </a-tree-select>
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="doc.sort" placeholder="顺序"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handlePreviewContent()" size="small">
              内容预览
            </a-button>
          </a-form-item>
          <a-form-item>
            <div id="content"></div>
          </a-form-item>

        </a-form>
      </a-col>
    </a-row>


    <!--如果需要table之外的按钮能够触发模态框的操作,那么模态框就不能放置于table里边-->
    <!--    <a-modal-->
    <!--        title="文档明细"-->
    <!--        v-model:visible="visible"-->
    <!--        :confirm-loading="confirmLoading"-->
    <!--        @ok="handleOk"-->
    <!--        okText="确定"-->
    <!--        cancelText="取消"-->
    <!--    >-->


    <!--    </a-modal>-->

    <!--  </a-config-provider>-->
    <a-drawer width="900" placement="right"
              :closable="false"
              :visible="drawerVisible"
              @close="onDrawerClose">
      <div class="wangeditor" :innerHTML="previewHtml"></div>
    </a-drawer>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor';


export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};


    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        slots: {customRender: 'name'},
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},

      },
    ];

    const treeSelectData = ref();
    treeSelectData.value = [];

    const loading = ref(false);
    // const docRes = ref();
    // docRes.value = [];

    const level1 = ref();
    level1.value = [];


    /**
     * 数据查询
     **/

    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all/" + route.query.ebookId).then(
          (response) => {
            loading.value = false;
            const data = response.data;
            if (data.success) {

              const res = data.content;

              level1.value = [];
              level1.value = Tool.array2Tree(res, 0);

              treeSelectData.value = Tool.copy(level1.value) || [];
              treeSelectData.value.unshift({id: 0, name: '无'});
              console.log("treeSelectData", treeSelectData.value);


            } else {
              message.error(data.message);
            }

          });
    };

    const doc = ref();
    doc.value = {};
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    /*
    * 按钮的编辑事件
    * */
    const editModal = (record: any) => {
      //打开
      //confirmLoading.value = false;
      // visible.value = true;
      editor.txt.html("");
      treeSelectData.value = [];
      //vue使用输入框时，只赋值不双向绑定 Tool.copy 利用 JSON.parse 和 JSON.stringify
      //方法一:
      //ebook.value = Tool.copy(record);
      //方法二: ES6的写法
      doc.value = {...record}
      treeSelectData.value = Tool.copy(level1.value) || [];
      if (Tool.isNotEmpty(record)) {
        Tool.findChildrenTree(treeSelectData.value, record.id);
      }
      treeSelectData.value.unshift({id: 0, name: '无'});
      handleQueryContent();


    };


    let deleteIds: Array<string> = [];

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      deleteIds = [];
      Tool.findChildrenNode(deleteIds, treeSelectData.value, id);
      console.log("deleteIds", deleteIds);
      axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery();
        } else {
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };

    /**
     * @author: bahsk
     * @date: 2021/7/9 15:38
     * @description: 增加文档
     * @params:
     * @return:
     */
    const add = () => {
      editor.txt.html("");
      doc.value = {
        ebookId: route.query.ebookId
      };
      //当前文档没有节点的新增的时候,文档节点里边应该只会出现无一个选项而已
      treeSelectData.value = Tool.copy(level1.value) || [];
      console.log("treeSelectData", treeSelectData.value);
      treeSelectData.value.unshift({id: 0, name: '无'});


    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content)
        } else {
          message.error(data.message);
        }
      });
    };

    const editor = new E("#content");
    editor.config.zIndex = 0;
    /*
    * 保存按钮
    * */
    const handleSave = () => {
      confirmLoading.value = true;
      const content = editor.txt.html();
      console.log("content", content);
      doc.value.ebookId = route.query.ebookId;
      doc.value.content = content;
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {

          message.success("保存成功!");
          // 重新加载列表
          handleQuery();
        } else {
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };

    /**
     * @author: bahsk
     * @date: 2021/7/11 0:34
     * @description:  富文本预览相关
     */
    const drawerVisible = ref(false);
    const previewHtml = ref();

    const handlePreviewContent = () => {
      drawerVisible.value = true;
      previewHtml.value = editor.txt.html();
    }

    /*
    *  drawer关闭
    * */
    const onDrawerClose = () => {
      drawerVisible.value = false;
    }


    onMounted(() => {
      handleQuery();
      //初始化富文本编辑器

      editor.create();

    });


    return {
      param,
      level1,
      columns,
      loading,
      doc,
      visible,
      confirmLoading,
      treeSelectData,
      add,
      editModal,
      handleDelete,
      handleSave,

      onDrawerClose,
      drawerVisible,
      handlePreviewContent,
      previewHtml
    };
  },
});
</script>

<style>

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

.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}
</style>
