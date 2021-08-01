<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic title="总阅读数" :value="statistic.viewCount" style="margin-right: 50px">
                <template #suffix>
                  <EyeOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="总点赞量" :value="statistic.voteCount" style="margin-right: 50px">
                <template #suffix>
                  <like-outlined/>
                </template>
              </a-statistic>

            </a-col>
            <a-col :span="8">
              <a-statistic
                  title="点赞率"
                  :value="statistic.voteCount / statistic.viewCount*100"
                  suffix="%"
                  :precision="2"
                  :value-style="{ color: '#cf1322' }"
                  style="margin-right: 50px"
              >

              </a-statistic>

            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="今日阅读" :value="statistic.todayViewCount" style="margin-right: 50px">
                <template #suffix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                <template #suffix>
                  <like-outlined/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读"
                  :value="statistic.todayViewIncrease"
                  :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读增长"
                  :value="statistic.todayViewIncreaseRateAbs"
                  :precision="2"
                  suffix="%"
                  class="demo-class"
                  :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row>


      <a-col :span="24">
        <a-card>
          <a-row>
            <v-chart class="chart" :option="option"/>
          </a-row>
        </a-card>
      </a-col>

    </a-row>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, onMounted} from 'vue';
import axios from "axios";
import {LikeOutlined, EyeOutlined, ArrowUpOutlined, ArrowDownOutlined, UserOutlined} from '@ant-design/icons-vue';
import {LineChart} from "echarts/charts";
import {use} from "echarts/core";
import {CanvasRenderer} from "echarts/renderers";
import {LegendComponent, TitleComponent, TooltipComponent, GridComponent, ToolboxComponent} from "echarts/components";
import VChart from "vue-echarts";

use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  ToolboxComponent
]);

export default defineComponent({
  components: {
    LikeOutlined,
    EyeOutlined,
    ArrowUpOutlined, ArrowDownOutlined, UserOutlined,
    VChart
  },
  name: 'TheWelcome',
  setup() {

    const statistic = ref();
    statistic.value = {};

    //获取统计值相关的数据
    const getStatistic = () => {
      axios.get('/ebook-snapshot/get-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          //虽然返回的是个List但是实际上只有一行数值，所以默认取
          //注意一下到底是应该从0开始还是从1开始
          const statisticResp = data.content;
          statistic.value.viewCount = statisticResp[1].viewCount;
          statistic.value.voteCount = statisticResp[1].voteCount;
          statistic.value.todayViewCount = statisticResp[1].viewIncrease;
          statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

          // 按分钟计算当前时间点，占一天的百分比
          const now = new Date();
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          // console.log(nowRate)
          statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
          // todayViewIncreaseRate：今日预计增长率
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewIncrease) / statisticResp[0].viewIncrease * 100;
          statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
        } else {
          console.log("暂未取到数据");
        }
      });
    }

    let option = ref();
    const init30DayEcharts = (list: any) => {

      // const myChart = echarts.init(document.getElementById('main'));
      const xAxis = [];
      const seriesView = [];
      const seriesVote = [];
      for (let i = 0; i < list.length; i++) {
        const record = list[i];
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      }
      option.value = {
        title: {
          text: '30天趋势图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总阅读量', '总点赞量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '总阅读量',
            type: 'line',
            data: seriesView,
            smooth: true
          },
          {
            name: '总点赞量',
            type: 'line',
            data: seriesVote,
            smooth: true
          }

        ]
      };
    }


    //获取30天图表的
    const get30DayStatistic = () => {
      axios.get('/ebook-snapshot/get-30-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticList = data.content;
          init30DayEcharts(statisticList);

          console.log("statisticList", statisticList);
          // const xAxis = [];
          // const seriesView = [];
          // const seriesVote = [];
          // for (let i = 0; i < statisticList.length; i++) {
          //   const record = statisticList[i];
          //   console.log("record",record);
          //   xAxis.push(record.date);
          //   seriesView.push(record.viewIncrease);
          //   seriesVote.push(record.voteIncrease);
          // }
          // option.value = {
          //   title: {
          //     text: '30天趋势图'
          //   },
          //   tooltip: {
          //     trigger: 'axis'
          //   },
          //   legend: {
          //     data: ['总阅读量', '总点赞量']
          //   },
          //   grid: {
          //     left: '3%',
          //     right: '4%',
          //     bottom: '3%',
          //     containLabel: true
          //   },
          //   toolbox: {
          //     feature: {
          //       saveAsImage: {}
          //     }
          //   },
          //   xAxis: {
          //     type: 'category',
          //     boundaryGap: false,
          //     data: xAxis
          //   },
          //   yAxis: {
          //     type: 'value'
          //   },
          //   series: [
          //     {
          //       name: '总阅读量',
          //       type: 'line',
          //       data: seriesView,
          //       smooth: true
          //     },
          //     {
          //       name: '总点赞量',
          //       type: 'line',
          //       data: seriesVote,
          //       smooth: true
          //     }
          //
          //   ]
          // };
        } else {
          console.log("暂时获取不到数据");
        }
      });
    }

    onMounted(() => {
      getStatistic();
      get30DayStatistic();

    });

    return {
      statistic,
      option
    }
  }
});
</script>

<style scoped>
.chart {
  height: 400px;
}
</style>

