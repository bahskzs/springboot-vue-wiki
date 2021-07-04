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
    <p>
      {{childrenStr}}
    </p>
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
    const categoryRes = ref();
    // categoryRes.value = [];

    const level1 = ref();
    level1.value = [];



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
              categoryRes.value = res;
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

    //子节点字符串
    const childrenStr = ref();

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
      let arr = [];
      arr = Tool.findChildrenNode(categoryRes.value, record.id);
      arr.push(record.id);

      childrenStr.value = arr;

    }

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
      childrenStr,
      add,
      editModal,
      handleDelete,
      handleOk,
    };
  },
});
</script>

