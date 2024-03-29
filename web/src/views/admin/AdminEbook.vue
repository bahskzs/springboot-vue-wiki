<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <a-config-provider>
      <template  #renderEmpty>
        <div style="text-align: center">
          <smile-outlined style="font-size: 20px" />
          <p>暂无数据</p>
        </div>
      </template>
      <p>
        <a-input-search
            v-model:value="paramValue"
            placeholder="输入需要查询的电子书"
            enter-button
            @search="onSearch"
            :style="{width:'300px'}"
        />
        <a-divider type="vertical" />
        <a-button type="primary" @click="add()" >
          新增
        </a-button>
      </p>
      <a-table
        :columns="columns"
        :row-key="record => record.id"
        :data-source="ebooks"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
    >
      <template #cover="{text:cover}">
        <a-avatar shape="square" :size="32" v-if="cover" :src="cover" alt="" />
      </template>
      <template #category="{record}">
        <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        {{ getCategoryName(record.category1Id) }}
     </template>

      <template #action="{ record }">
        <span>
          <router-link :to="'/admin/doc?ebookId='+record.id">
            <a-button type="primary" shape="round">文档管理</a-button>
          </router-link>
          <a-divider type="vertical"/>
          <a-button type="primary" @click="editModal(record)" shape="round">编辑</a-button>
          <a-modal
              title="电子书明细"
              v-model:visible="visible"
              :confirm-loading="confirmLoading"
              @ok="handleOk"
              okText="确定"
              cancelText="取消"
          >

              <a-form
                  :model="ebook"
                  :label-col="labelCol"
                  :wrapper-col="wrapperCol"
              >
                <a-form-item label="封面">
                  <a-input v-model:value="ebook.cover" />
                </a-form-item>
                <a-form-item label="名称">
                  <a-input v-model:value="ebook.name" />
                </a-form-item>
                <a-form-item label="分类">
                  <a-cascader
                      v-model:value="categoryIds"
                      :field-names="{ label: 'name', value: 'id', children: 'children' }"
                      :options="level1"
                  />
                </a-form-item>
                <a-form-item label="描述">
                  <a-input v-model:value="ebook.description" type="textarea" />
                </a-form-item>
              </a-form>


          </a-modal>
          <a-divider type="vertical" />
          <a-popconfirm
              title="删除后不可恢复,确认删除"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)"
          >
            <a-button type="danger" shape="round">删除</a-button>
          </a-popconfirm>
        </span>
      </template>
    </a-table>
    </a-config-provider>
  </a-layout-content>
</template>
<script lang="ts">
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { defineComponent, onMounted, ref } from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";


export default defineComponent({
  name: 'AdminEbook',
  setup() {

    const loading = ref(false);
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        key : 'cover',
        slots: { customRender: 'cover' },

      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name'


      },
      {
        title: '分类',
        dataIndex: 'category',
        key: 'category',
        slots: { customRender: 'category' },

      },
      {
        title: '描述',
        dataIndex: 'description',

      },
      {
        title: '文档数',
        dataIndex: 'docCount',

      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',

      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];
    const ebooks = ref();

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list",{
          params: {
            page: params.page,
            size: params.size,
            name: params.name,
        }
    }).then(
          (response)=>{
            loading.value = false;
            const data = response.data;
            if(data.success) {

              ebooks.value = data.content.list;

              //切换页码
              pagination.value.current = params.page;
              pagination.value.total = data.content.total;
            }else {
              message.error(data.message);
            }

          });

    };

    /*
    * 查询分类
    * */
    const level1 = ref();
    level1.value = [];
    let categoryData = [];
    const handleCategoryQuery = () => {
      loading.value = true;
      axios.get("/category/all").then(
          (response)=>{
            loading.value = false;
            const data = response.data;
            if(data.success) {

              const res = data.content;
              categoryData = data.content;
              level1.value = [];
              level1.value = Tool.array2Tree(res, 0);

              //categoryRes.value = toTree(res);
              handleQuery({
                page : 1,
                size : pagination.value.pageSize
              });

            }else {
              message.error(data.message);
            }

          });
    };

    /*
    * 参数查询
    * */
    const paramValue = ref<string>('');
    const onSearch = (paramValue: string) => {
      console.log("参数查询",paramValue)
      handleQuery({
        name : paramValue
      })
    }


    const handleTableChange = (pagination: any) => {
      handleQuery({
        page : pagination.current,
        size : pagination.pageSize
      })
    }

    //模态框相关
    const ebook = ref();
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const categoryIds = ref();

    //按钮的编辑事件
    const editModal = (record : any) => {
      //打开
      confirmLoading.value = false;
      visible.value = true;

      //vue使用输入框时，只赋值不双向绑定 Tool.copy 利用 JSON.parse 和 JSON.stringify
      //方法一:
      //ebook.value = Tool.copy(record);
      //方法二: ES6的写法
      ebook.value = {...record}
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
      console.log("categoryIds.value",categoryIds.value);
      //ebook.value = data;
    };

    /*
    * 获取分类名称
    * */
    const getCategoryName = (categoryId : any) => {
      let result = "";
      categoryData.forEach((item)=>{
        if(Number(item.id) === Number(categoryId)){
          result =  item.name;
        }
      });
      return result;
    }


    const handleOk = () => {
      confirmLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      console.log("ebook :",ebook.value);
      axios.post("/ebook/save", ebook.value).then((response) => {
        confirmLoading.value = false;
        const data = response.data; // data = commonResp

        if (data.success) {
          visible.value = false;


          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }else{
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };


    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      ebook.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id : number) => {

      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }else{
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };


    onMounted(()=>{
      handleCategoryQuery();

    });

    return {
      ebooks,
      columns,
      pagination,
      loading,
      handleTableChange,
      level1,
      categoryIds,

      ebook,
      visible,
      confirmLoading,
      editModal,
      getCategoryName,
      handleOk,

      add,
      handleDelete,


      labelCol: { span: 4 },
      wrapperCol: { span: 14 },

      onSearch,
      paramValue

  };
  },
  components: {
    SmileOutlined,
    DownOutlined,
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

</style>


