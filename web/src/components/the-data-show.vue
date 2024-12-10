<!--<template>-->
<!--  <h1>欢迎</h1>-->
<!--  <p>欢迎使用本系统11</p>-->
<!--</template>-->
<!--<script lang="ts">-->
<!--import {computed, defineComponent, onMounted} from 'vue';-->
<!--import {useRef} from "preact/compat";-->
<!--import store from "@/store";-->
<!--import {Tool} from "@/util/tool";-->
<!--import { notification } from 'ant-design-vue';-->

<!--export default defineComponent({-->
<!--  name: 'the-data-show',-->
<!--  setup () {-->

<!--  }-->
<!--});-->
<!--</script>-->


<template>
  <div>
    <!-- 统计数据卡片 -->
    <a-row gutter="16">
      <a-col :span="24">
        <a-card>
          <a-row gutter="16">
            <a-col :span="8">
              <a-statistic title="总阅读量" :value="statistic.viewCount">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="总点赞量" :value="statistic.voteCount">
                <template #suffix>
                  <LikeOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="点赞率" :value="statistic.voteCount / statistic.viewCount * 100"
                           :precision="2"
                           suffix="%"
                           :value-style="{ color: '#cf1322' }">
                <template #suffix>
                  <LikeOutlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <!-- 今日统计数据 -->
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row gutter="16">
            <a-col :span="12">
              <a-statistic title="今日阅读" :value="statistic.todayViewCount">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                <template #suffix>
                  <LikeOutlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row gutter="16">
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读"
                  :value="statistic.todayViewIncrease"
                  :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读增长"
                  :value="statistic.todayViewIncreaseRateAbs"
                  :precision="2"
                  suffix="%"
                  :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <ArrowDownOutlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <ArrowUpOutlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <!-- ECharts图表展示 -->
    <a-row>
      <a-col :span="24">
        <div ref="echartsContainer" style="width: 100%;height:300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import axios from 'axios';
import { UserOutlined, LikeOutlined, ArrowUpOutlined, ArrowDownOutlined } from '@ant-design/icons-vue';

declare let echarts: any;

export default defineComponent({
  name: 'TheDataShow',
  components: {
    UserOutlined,
    LikeOutlined,
    ArrowUpOutlined,
    ArrowDownOutlined,
  },
  setup () {
    const statistic = ref<any>({});
    const echartsContainer = ref<HTMLElement | null>(null);

    const getStatistic = async () => {
      try {
        const response = await axios.get('/snapshot/get-data');
        const data = response.data;
        if (data.success) {
          const statisticResp = data.content;
          statistic.value.viewCount = statisticResp[1].viewCount;
          statistic.value.voteCount = statisticResp[1].voteCount;
          statistic.value.todayViewCount = statisticResp[1].viewCountIncrease;
          console.log('todayViewCount:', statisticResp[1].viewCountIncrease);
          statistic.value.todayVoteCount = statisticResp[1].voteCountIncrease;
          console.log('todayVoteCount:', statisticResp[1].voteCountIncrease);

          const now = new Date();
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewCountIncrease / nowRate));
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewCountIncrease) / statisticResp[0].viewCountIncrease * 100;
          statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
        }
      } catch (error) {
        console.error('获取统计数据失败', error);
      }
    };

    const init30DayEcharts = (list: any[]) => {
      if (!echartsContainer.value) return;

      const myChart = echarts.init(echartsContainer.value);
      // const xAxis = [];
      // const seriesView = [];
      // const seriesVote = [];
      const xAxis: string[] = [];
      const seriesView: number[] = [];
      const seriesVote: number[] = [];

      list.forEach((record) => {
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      });

      const option = {
        title: { text: '30天趋势图' },
        tooltip: { trigger: 'axis' },
        legend: { data: ['总阅读量', '总点赞量'] },
        xAxis: { type: 'category', boundaryGap: false, data: xAxis },
        yAxis: { type: 'value' },
        series: [
          { name: '总阅读量', type: 'line', data: seriesView, smooth: true },
          { name: '总点赞量', type: 'line', data: seriesVote, smooth: true },
        ],
      };

      myChart.setOption(option);
    };

    const getMonthData = async () => {
      try {
        const response = await axios.get('/snapshot/get-month-data');
        const data = response.data;
        if (data.success) {
          const statisticList = data.content;
          init30DayEcharts(statisticList);
        }
      } catch (error) {
        console.error('获取一个月统计数据失败', error);
      }
    };

    onMounted(() => {
      getStatistic();
      getMonthData();
    });

    return {
      statistic,
      echartsContainer,
    };
  },
});
</script>

<style scoped>
.tip {
  padding: 10px 5px;
  margin-bottom: 20px;
  color: red;
  border: 1px solid transparent;
  background: linear-gradient(white, white) padding-box,
  repeating-linear-gradient(-45deg, black 0, black 25%, white 0, white 50%) 0/.6em .6em;
  animation: ants 12s linear infinite;
}
</style>
