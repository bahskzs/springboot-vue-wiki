<template>
  <a-tree-select
      v-model:value="value"
      style="width: 100%"
      :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
      :tree-data="treeData"
      placeholder="Please select"
      tree-default-expand-all

  >
    <template #title="{ key, value }">
      <span style="color: #08c" v-if="key === '0-0-1'">Child Node1 {{ value }}</span>
    </template>
  </a-tree-select>
</template>
<script lang="ts">
import {defineComponent, ref, watch} from 'vue';

interface TreeDataItem {
  value: string;
  key: string;
  title?: string;
  slots?: Record<string, string>;
  children?: TreeDataItem[];
  disabled?: boolean;
}

const treeData: TreeDataItem[] = [
  {
    title: 'Node1',
    value: '0-0',
    key: '0-0',
    children: [
      {
        value: '0-0-1',
        key: '0-0-1',
        slots: {
          title: 'title',
        },

      },
      {
        title: 'Child Node2',
        value: '0-0-2',
        key: '0-0-2',

      },
    ],

  },
  {
    title: 'Node2',
    value: '0-1',
    key: '0-1',
  },
];
export default defineComponent({
  setup() {
    const value = ref<string>();
    value.value = '0-0';


    return {
      value,
      treeData,
    };
  },
});
</script>

