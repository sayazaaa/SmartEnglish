<template>
  <el-container style="height:100vh">
    <!-- 顶部总用户数 -->
    <el-header class="header">
      <h2>总用户数：<span class="total">{{ totalUser }}</span></h2>
    </el-header>

    <el-main class="main">
      <el-row :gutter="20">
        <!-- 左侧：模块使用时长柱状图 -->
        <el-col :span="12">
          <div ref="usageChart" class="chart"></div>
        </el-col>

        <!-- 右侧：上用户数–日期图， 下留存热力图 -->
        <el-col :span="12">
          <el-row :gutter="20">
            <el-col :span="24">
              <div ref="userChart" class="chart"></div>
            </el-col>
            <el-col :span="24">
              <div ref="heatmapChart" class="heatmap"></div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import * as echarts from 'echarts'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

// --- DOM refs ---
const usageChart    = ref(null)
const userChart     = ref(null)
const heatmapChart  = ref(null)

// --- ECharts 实例 ---
let chartUsage, chartUser, chartHeatmap

// --- 绑定数据 ---
const totalUser = ref(0)

// --- 公用常量 ---
const DAYS    = 7
const START   = dayjs().subtract(DAYS - 1, 'day').format('YYYY-MM-DD')
const TYPE    = 'd'  // 按天；后端同样支持 'w' / 'm'

// --- 生成日期标签 ---
const makeDates = (start, count) =>
    Array.from({ length: count })
        .map((_, i) => dayjs(start).add(i, 'day').format('MM-DD'))

const makeAfter = count =>
    Array.from({ length: count })
        .map((_, i) => `${i+1}天后`)

// --- 1. 平均模块使用时长 ---
async function loadUsage() {
  try {
    const res = await axios.get('/data')
    totalUser.value = res.data.total_user

    const arr = res.data.average_usetime
    const names  = arr.map(i => i.function)
    const values = arr.map(i => i.value)

    chartUsage = echarts.init(usageChart.value)
    chartUsage.setOption({
      title: { text: '平均模块使用时长 ' },
      tooltip: {},
      xAxis: { type:'category', data:names },
      yAxis: { type:'value' },
      series: [{ type:'bar', data:values }]
    })
  } catch (e) {
    console.error(e)
    ElMessage.error('加载模块使用时长失败')
  }
}

// --- 2. 用户数–日期折线图 ---
async function loadUserCount() {
  const days = 7
  // 从今天往前推 6 天，一共 7 天
  const startDate = dayjs().subtract(days - 1, 'day').format('YYYY-MM-DD')
  const res = await axios.post('/data', {
    data: 'user_count',
    type: 'd',
    start: startDate
  })
  const raw = Array.isArray(res.data) ? res.data : []

  // 构造固定长度为 7 的 labels 和 values
  const labels = []
  const values = []
  for (let i = 0; i < days; i++) {
    // 生成 “MM-DD” 格式标签
    labels.push(
        dayjs(startDate).add(i, 'day').format('MM-DD')
    )
    // 如果后台没返回这一天的数据，就补 0
    const v = raw[i]
    values.push(Number.isFinite(v) ? v : 0)
  }

  // 渲染折线图
  chartUser = echarts.init(userChart.value)
  chartUser.setOption({
    title: { text: '用户增长量 ' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: labels },
    yAxis: { type: 'value' },
    series: [{
      type: 'line',
      data: values,
      smooth: true,
      connectNulls: true
    }]
  })
}

// --- 3. 留存率热力图 ---
async function loadHeatmap() {
  try {
    const xLabels = makeAfter(DAYS)
    const yLabels = makeDates(START, DAYS)
    const heatData = []

    // 逐天请求留存接口，拼成 heatmap 数据
    for (let i = 0; i < DAYS; i++) {
      const cohort = dayjs(START).add(i, 'day').format('YYYY-MM-DD')
      const res = await axios.post('/data', {
        data:  'remain',
        type:  "d",
        start: cohort
      })
      res.data.forEach((v, j) => {
        if(i+j <=6)heatData.push([ j, i,  parseInt(100*v) ])  // [xIndex, yIndex, value]
      })
    }

    chartHeatmap = echarts.init(heatmapChart.value)
    chartHeatmap.setOption({
      title: {
        text: '用户留存率热力图 (%)',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',         // ← 一定要是 item
        position: 'top',
        formatter: '{c}%',       // ← 只显示当前格子的值，并加个百分号
      },
      grid: { top: '15%', height: '65%' },
      xAxis: {
        type: 'category',
        data: xLabels,      // ['1天后','2天后',…]
        splitArea: { show: true }
      },
      yAxis: {
        type: 'category',
        data: yLabels,      // ['06-02','06-03',…]
        splitArea: { show: true }
      },
      visualMap: {
        min: 0,
        max: 100,
        calculable: true,
        orient: 'horizontal',
        left: 'center',
        bottom: 0,
        formatter: val => val + '%'  // 右侧那条也显示百分号
      },
      series: [{
        name: '留存率',
        type: 'heatmap',
        data: heatData,
        encode: {
          // 指明 heatData[i][0] 用来映射到 y 轴，
          // heatData[i][1] 用来映射到 x 轴，
          // heatData[i][2] 才是格子的“值”
          x: 1,
          y: 0,
          value: 2
        },
        label: {
          show: true,
          //formatter: '{c}%'   // {c} 会自动读取 encode.value 那一维
        },
        emphasis: {
          itemStyle: {
            borderColor: '#333',
            borderWidth: 1
          }
        }
      }]
    })



  } catch (e) {
    console.error(e)
    ElMessage.error('加载留存热力图失败')
  }
}

onMounted(() => {
  loadUsage()
  loadUserCount()
  loadHeatmap()
})
</script>

<style scoped>
.header {
  background: #2d3a4b; color: #fff;
  padding: 0 20px; line-height: 40px;
}
.header .total { color:#ffd400; }
.main {
  background: #faf6f2;
  padding:20px;
  overflow-y:auto;
}
.chart { width:100%; height:250px; }
.heatmap { width:100%; height:350px; margin-top:20px; }
</style>
