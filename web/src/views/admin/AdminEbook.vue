<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >

  <a-table
      :columns="columns"
      :data-source="ebooks"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"

  >
    <template #cover="{text:cover}">
      <a-avatar shape="square" :size="32" v-if="cover" :src="cover" alt="" />
    </template>
    <template #action>
      <span>
        <a-button type="primary">编辑</a-button>
        <a-divider type="vertical" />
        <a-button type="danger">删除</a-button>
      </span>
    </template>
  </a-table>
  </a-layout-content>
</template>
<script lang="ts">
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { defineComponent, onMounted, ref } from 'vue';
import axios from "axios";
import { TableState } from 'ant-design-vue/lib/table/interface';




export default defineComponent({
  setup() {
    const loading = ref(false);
    const pagination = ref({
      current : 1,
      pageSize : 4,
      total : 0
    });

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        key : 'cover',
        slots: { customRender: 'cover' },

      },
      {
        // title和slots中的title对比,是title优先
        title: '名称',
        dataIndex: 'name',


      },
      {
        title: '分类一',
        dataIndex: 'category1Id',

      },
      {
        title: '分类二',
        dataIndex: 'category2Id',

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
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];
    const ebooks = ref();

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      axios.get("/ebook/list",{
          params: {
            page: params.page,
            size: params.size,
        }
    }).then(
          (response)=>{
            const data = response.data;
            ebooks.value = data.content.list;

            //切换页码
            pagination.value.current = params.page;
            pagination.value.total = data.content.total;
          });

    };

    const handleTableChange = (pagination: any) => {
      handleQuery({
        page : pagination.current,
        size : pagination.pageSize
      })
    }

    onMounted(()=>{
      handleQuery({
        page : 1,
        size : pagination.value.pageSize
      });
    });

    return {
      ebooks,
      columns,
      pagination,
      loading,
      handleTableChange
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


