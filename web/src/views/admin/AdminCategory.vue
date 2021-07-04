<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
  <p>
    <a-button type="primary" @click="add()" >
      新增
    </a-button>
  </p>
  <a-table
      :columns="columns"
      :data-source="level1"
      :pagination="false"
      :loading="loading">
    <template #action="{ record }">
      <span>
          <a-button type="primary" @click="editModal(record)">编辑</a-button>
        <a-modal
            title="电子书明细"
            v-model:visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            okText="确定"
            cancelText="取消"
        >

              <a-form
                  :model="category"

              >

                <a-form-item label="名称">
                  <a-input v-model:value="category.name" />
                </a-form-item>
                <a-form-item label="分类">
                  <a-select
                      v-model:value="category.parent"
                      ref="select"
                  >
                    <a-select-option :value="0">
                      无
                    </a-select-option>
                    <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
                      {{c.name}}
                    </a-select-option>
                </a-select>
                </a-form-item>
                <a-form-item label="序号">
                  <a-input v-model:value="category.sort" />
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
            <a-button type="danger" >删除</a-button>
          </a-popconfirm>
        </span>
    </template>
  </a-table>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";


export default defineComponent({
  setup() {

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
        key: 'parent',
      },
      {
        title: '排序',
        dataIndex: 'sort',
      },
      {
        title: '操作',
        key:'action',
        slots: { customRender: 'action' },

      },
    ];



    const loading = ref(false);
    // const categoryRes = ref();
    // categoryRes.value = [];

    const level1 = ref();
    level1.value = [];

    // const toTree = (data : any) => {
    //   const result =[];
    //   data.forEach((item) => {
    //     delete item.children;
    //   });
    //   const map = {};
    //   data.forEach((item) => {
    //     map[item.id] = item;
    //   });
    //   data.forEach((item) => {
    //     const parent = map[item.parent];
    //     if (parent) {
    //       (parent.children || (parent.children = [])).push(item);
    //     } else {
    //       result.push(item);
    //     }
    //   });
    //   return result;
    // }

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then(
          (response)=>{
            loading.value = false;
            const data = response.data;
            if(data.success) {

              const res = data.content;

              level1.value = [];
              level1.value = Tool.array2Tree(res, 0);

              //categoryRes.value = toTree(res);


            }else {
              message.error(data.message);
            }

          });
    };

    const category = ref({});
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    /*
    * 按钮的编辑事件
    * */
    const editModal = (record : any) => {
      //打开
      confirmLoading.value = false;
      visible.value = true;

      //vue使用输入框时，只赋值不双向绑定 Tool.copy 利用 JSON.parse 和 JSON.stringify
      //方法一:
      //ebook.value = Tool.copy(record);
      //方法二: ES6的写法
      category.value = {...record}
      //ebook.value = data;
    };

    /**
     * 删除
     */
    const handleDelete = (id : number) => {

      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery();
        }else{
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };

    const add = () => {
      visible.value = true;
      category.value = {};
    };


    /*
    * ok按钮
    * */
    const handleOk = () => {
      confirmLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
        confirmLoading.value = false;
        const data = response.data; // data = commonResp

        if (data.success) {
          visible.value = false;


          // 重新加载列表
          handleQuery();
        }else{
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };

    onMounted(()=>{
      handleQuery();
    });

    return {
      level1,
      columns,
      loading,

      category,
      visible,
      confirmLoading,

      add,
      editModal,
      handleDelete,
      handleOk,
    };
  },
});
</script>

