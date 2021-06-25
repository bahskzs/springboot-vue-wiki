<template>
  <v-chart class="chart" :option="option" />
</template>

<script>
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { PieChart } from "echarts/charts";
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from "echarts/components";
import VChart, { THEME_KEY } from "vue-echarts";
import { ref, defineComponent } from "vue";
import {onMounted} from "@vue/runtime-core";
import axios from "axios";

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
]);

export default defineComponent({

  components: {
    VChart
  },
  provide: {
    [THEME_KEY]: "dark"
  },
  setup () {
    let option = ref();


    onMounted( ()=> {
      axios.get("/chart/list").then(
          (response)=>{

            const data = response.data;

            option.value =  {
              title: {
                text: "Traffic Sources",
                left: "center"
              },
              tooltip: {
                trigger: "item",
                formatter: "{a} <br/>{b} : {c} ({d}%)"
              },
              legend: {
                orient: "vertical",
                left: "left",
                data: data.content.name
              },
              series: [
                {
                  name: "Traffic Sources",
                  type: "pie",
                  radius: "55%",
                  center: ["50%", "60%"],
                  data: data.content,
                  emphasis: {
                    itemStyle: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: "rgba(0, 0, 0, 0.5)"
                    }
                  }
                }
              ]
            }

          });



    });

    return { option };
  }
});
</script>

<style scoped>
.chart {
  height: 400px;
}
</style>
