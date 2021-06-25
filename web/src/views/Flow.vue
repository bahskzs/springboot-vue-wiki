<template>
  <div id="container"></div>
</template>
<script lang="ts">
import { Graph, Node, Platform } from '@antv/x6';
import {defineComponent, onMounted,ref } from 'vue';
import axios from "axios";

export default defineComponent({
  setup() {
    Graph.registerConnector(
        'algo-edge',
        (source, target) => {
          const offset = 2
          const control = 80
          const v1 = { x: source.x, y: source.y + offset + control }
          const v2 = { x: target.x, y: target.y - offset - control }

          return `M ${source.x} ${source.y}
       L ${source.x} ${source.y + offset}
       C ${v1.x} ${v1.y} ${v2.x} ${v2.y} ${target.x} ${target.y - offset}
       L ${target.x} ${target.y}
      `
        },
        true,
    )
    const data = {
      // 节点
      nodes: [
        {
          id: 'node1', // String，可选，节点的唯一标识
          x: 40,       // Number，必选，节点位置的 x 值
          y: 40,       // Number，必选，节点位置的 y 值
          width: 80,   // Number，可选，节点大小的 width 值
          height: 40,  // Number，可选，节点大小的 height 值
          label: 'hello', // String，节点标签
        },
        {
          id: 'node2', // String，节点的唯一标识
          x: 160,      // Number，必选，节点位置的 x 值
          y: 180,      // Number，必选，节点位置的 y 值
          width: 80,   // Number，可选，节点大小的 width 值
          height: 40,  // Number，可选，节点大小的 height 值
          label: 'world', // String，节点标签
        },
      ],
      // 边
      edges: [
        {
          source: 'node1', // String，必须，起始节点 id
          target: 'node2', // String，必须，目标节点 id
          attrs: {
            line: {
              strokeWidth: 2,
              stroke: '#1890ff',
              //strokeDasharray: 5,
              targetMarker: 'classic',
            }

          },
          vertices: [
            { x: 330, y: 100 },

          ],
          connector: 'algo-edge',
        },
      ],
    };

    const elem : HTMLElement = document.getElementById('container') as HTMLElement;
    let graph = new Graph({
      container: elem,
      width: 1200,
      height: 400,
      background: {
        //color: '#fffbe6', // 设置画布背景颜色
      },
      grid: {
        size: 20,      // 网格大小 10px
        visible: true, // 渲染网格背景
      },
    });

    onMounted(()=>{
      axios.get("/chart/list").then(
          (response)=>{
            const res = response.data.content;

          });

      graph.fromJSON(data);

    });

    // return {
    //   graph
    // }
  }
});


</script>

