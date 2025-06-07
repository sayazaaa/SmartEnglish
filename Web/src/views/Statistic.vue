<template>
  <div class="statistic-page">
    <!-- 总用户数 -->
    <el-card class="total-user-card">
      <div class="total-label">总用户数</div>
      <div class="total-value">{{ totalUser }}</div>
    </el-card>

    <!-- 图表布局：左右等比 -->
    <div class="charts-container">
      <!-- 左侧：模块使用时长 -->
      <el-card class="chart-card">
        <div class="chart-header">模块使用时长（每日）</div>
        <div ref="moduleUsageChart" class="chart"></div>
      </el-card>

      <!-- 右侧：上用户数量，下留存率 -->
      <div class="right-charts">
        <el-card class="chart-card">
          <div class="chart-header">用户数量（每日新增）</div>
          <div ref="userCountChart" class="chart"></div>
        </el-card>
        <el-card class="chart-card">
          <div class="chart-header">用户留存率 (%)</div>
          <div ref="retentionChart" class="chart"></div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import axios from 'axios';
import * as echarts from 'echarts';

const totalUser          = ref(0);
const moduleUsageChart  = ref(null);
const userCountChart    = ref(null);
const retentionChart    = ref(null);

// 工具：生成最近 N 天的日期标签
function getLastNDates(n) {
  return Array.from({ length: n }).map((_, i) => {
    const d = new Date();
    d.setDate(d.getDate() - (n - 1 - i));
    return `${d.getMonth() + 1}.${d.getDate()}`;
  });
}
// 工具：计算 N 天前日期字符串（YYYY-MM-DD）
function dateNDaysAgo(n) {
  const d = new Date();
  d.setDate(d.getDate() - n + 1);
  return d.toISOString().slice(0, 10);
}

onMounted(() => {
  nextTick(async () => {
    const dates = getLastNDates(7);

    // 1. 请求总览数据：total_user + average_usetime
    try {
      const { data: overview } = await axios.get('/data');
      totalUser.value = overview.total_user;
      const avgData = overview.average_usetime || [];
      const mu = echarts.init(moduleUsageChart.value);
      mu.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: avgData.map(i => i.function) },
        yAxis: { type: 'value', name: '分钟' },
        series: [{ name: '平均时长', type: 'bar', data: avgData.map(i => i.value) }]
      });
    } catch (e) {
      console.error('GET /data failed', e);
    }

    // 2. 请求用户数量
    try {
      const start = dateNDaysAgo(7);
      const resp1 = await axios.post('/data', { data: 'user_count', type: 'd', start });
      const uc = echarts.init(userCountChart.value);
      uc.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: '人数' },
        series: [{ name: '新增用户', type: 'bar', data: resp1.data }]
      });
    } catch (e) {
      console.error('POST /data user_count failed', e);
    }

    // 3. 请求留存率：用户增长率 / 次日留存 / 七日留存
    try {
      const start = dateNDaysAgo(7);
      const [rInc, rDay, rWeek] = await Promise.all([
        axios.post('/data', { data: 'increase', type: 'd', start }),
        axios.post('/data', { data: 'remain',   type: 'd', start }),
        axios.post('/data', { data: 'remain',   type: 'w', start })
      ]);
      const rc = echarts.init(retentionChart.value);
      rc.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: params => params.map(p => `${p.marker} ${p.seriesName}: ${p.value}%`).join('<br/>')
        },
        legend: { data: ['用户增长率', '次日留存率', '七日留存率'] },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: '%', axisLabel: { formatter: '{value}%' } },
        series: [
          { name: '用户增长率', type: 'bar', data: rInc.data },
          { name: '次日留存率', type: 'bar', data: rDay.data },
          { name: '七日留存率', type: 'bar', data: rWeek.data }
        ]
      });
    } catch (e) {
      console.error('POST /data retention failed', e);
    }
  });
});
</script>

<style scoped>
.statistic-page {
  padding: 20px;
  background: #faf6f2;
}

.total-user-card {
  width: 200px;
  margin-bottom: 20px;
  text-align: center;
}

.total-label {
  font-size: 14px;
  color: #606266;
}

.total-value {
  font-size: 28px;
  color: #f56c6c;
  margin-top: 4px;
}

.charts-container {
  display: flex;
  gap: 20px;
}

.chart-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 360px;
}

.right-charts {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chart-header {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
}

.chart {
  width: 100%;
  height: 260px;
}
</style>
