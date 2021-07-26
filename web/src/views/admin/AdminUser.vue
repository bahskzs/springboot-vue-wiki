<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <a-config-provider>
      <template #renderEmpty>
        <div style="text-align: center">
          <smile-outlined style="font-size: 20px"/>
          <p>暂无数据</p>
        </div>
      </template>
      <p>
        <a-input-search
            v-model:value="paramValue"
            placeholder="输入需要查询的用户"
            enter-button
            @search="onSearch"
            :style="{width:'300px'}"
        />
        <a-divider type="vertical"/>
        <a-button type="primary" @click="add()">
          新增
        </a-button>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :loading="loading"
          :pagination="pagination"
          @change="handleTableChange"
      >


        <template #action="{ record }">
        <span>

          <a-button type="primary" @click="editResetModal(record)" shape="round">重置密码</a-button>
          <a-modal
              title="重置密码"
              v-model:visible="resetVisible"
              :confirm-loading="resetConfirmLoading"
              @ok="handleResetOk"
              okText="确定"
              cancelText="取消"
          >

        <a-form
            :model="user"
            :label-col="labelCol"
            :wrapper-col="wrapperCol"
        >
          <!--user.id 不存在的时候就现实-->
          <a-form-item label="密码">
            <a-input v-model:value="user.password"/>
          </a-form-item>
        </a-form>


      </a-modal>
          <a-divider type="vertical"/>
          <a-button type="primary" @click="editModal(record)" shape="round">编辑</a-button>
          <a-modal
              title="用户明细"
              v-model:visible="visible"
              :confirm-loading="confirmLoading"
              @ok="handleOk"
              okText="确定"
              cancelText="取消"
          >

              <a-form
                  :model="user"
                  :label-col="labelCol"
                  :wrapper-col="wrapperCol"
              >
                <a-form-item label="登录名">
                  <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
                </a-form-item>
                <a-form-item label="名称">
                  <a-input v-model:value="user.name"/>
                </a-form-item>
                <!--user.id 不存在的时候就现实-->
                <a-form-item label="密码" v-show="!user.id">
                  <a-input v-model:value="user.password"/>
                </a-form-item>
              </a-form>


          </a-modal>


          <a-divider type="vertical"/>
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
import {SmileOutlined, DownOutlined} from '@ant-design/icons-vue';
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'AdminUser',
  setup() {

    const loading = ref(false);
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });

    const columns = [
      {
        title: '登录名',
        dataIndex: 'loginName',
      },
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '密码',
        dataIndex: 'password',
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},
      },
    ];
    const users = ref();

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;

      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: params.loginName,
        }
      }).then(
          (response) => {
            loading.value = false;
            const data = response.data;
            if (data.success) {
              users.value = data.content.list;

              //切换页码
              pagination.value.current = params.page;
              pagination.value.total = data.content.total;
            } else {
              message.error(data.message);
            }

          });

    };


    /*
    * 参数查询
    * */
    const paramValue = ref<string>('');
    const onSearch = (paramValue: string) => {
      console.log("参数查询", paramValue)
      handleQuery({
        loginName: paramValue
      })
    }


    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }

    //模态框相关
    const user = ref();
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);


    //按钮的编辑事件
    const editModal = (record: any) => {
      //打开
      confirmLoading.value = false;
      visible.value = true;

      //vue使用输入框时，只赋值不双向绑定 Tool.copy 利用 JSON.parse 和 JSON.stringify
      //方法一:
      //user.value = Tool.copy(record);
      //方法二: ES6的写法
      user.value = {...record}

    };


    const handleOk = () => {
      confirmLoading.value = true;
      console.log("user :", user.value);
      user.value.password = hexMd5(user.value.password + KEY)
      axios.post("/user/save", user.value).then((response) => {
        confirmLoading.value = false;
        const data = response.data; // data = commonResp

        if (data.success) {
          visible.value = false;


          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
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
      user.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {

      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
          message.success("删除成功 !");
        } else {
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };


    /*重置密码*/
    //模态框相关

    const resetVisible = ref<boolean>(false);
    const resetConfirmLoading = ref<boolean>(false);


    //按钮的编辑事件
    const editResetModal = (record: any) => {
      //打开
      resetConfirmLoading.value = false;
      resetVisible.value = true;

      //vue使用输入框时，只赋值不双向绑定 Tool.copy 利用 JSON.parse 和 JSON.stringify
      //方法一:
      //user.value = Tool.copy(record);
      //方法二: ES6的写法
      user.value = {...record}
      user.value.password = null;

    };


    const handleResetOk = () => {
      resetConfirmLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY)
      console.log(user.value);
      axios.post("/user/reset-password", user.value).then((response) => {
        resetConfirmLoading.value = false;
        const data = response.data; // data = commonResp

        if (data.success) {
          resetVisible.value = false;


          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          //如果返回异常则返回错误提示
          message.error(data.message);
        }
      });

    };


    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    })

    return {
      users,
      columns,
      pagination,
      loading,
      handleTableChange,

      user,
      visible,
      confirmLoading,
      editModal,
      handleOk,

      add,
      handleDelete,


      labelCol: {span: 4},
      wrapperCol: {span: 14},

      onSearch,
      paramValue,

      resetVisible,
      resetConfirmLoading,
      editResetModal,
      handleResetOk


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
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}

</style>


