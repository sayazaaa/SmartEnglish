<template>
  <el-container style="height:100vh">
    <el-header style="background:#f5f7fa; padding: 0 20px; display:flex; align-items:center;">
      <el-button
          type="danger"
          :disabled="!multipleSelection.length"
          @click="onBatchDelete"
      >
        批量删除
      </el-button>
    </el-header>

    <el-main class="main-content">
      <el-table
          v-loading="loading"
          :data="feedbackList"
          @selection-change="onSelectChange"
          style="width:100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="content" label="反馈内容" />
        <el-table-column prop="date" label="提交日期" width="160" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
                type="text"
                size="small"
                @click="onDelete(row.id)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; text-align: right;">
        共 {{ feedbackList.length }} 条反馈
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 数据
interface FeedbackItem {
  id: number
  content: string
  date: string
}
const feedbackList = ref<FeedbackItem[]>([])
const loading = ref(false)
const multipleSelection = ref<FeedbackItem[]>([])

// 拉反馈列表
async function fetchFeedback() {
  loading.value = true
  try {
    const res = await axios.get<FeedbackItem[]>('/feedback')
    feedbackList.value = res.data
  } catch (err) {
    ElMessage.error('获取反馈列表失败')
  } finally {
    loading.value = false
  }
}

// 单条删除
async function onDelete(id: number) {
  await ElMessageBox.confirm('确认删除这一条反馈？', '请确认', {
    type: 'warning',
  })
  try {
    await axios.delete('/feedback', { params: { id } })
    ElMessage.success('删除成功')
    fetchFeedback()
  } catch {
    ElMessage.error('删除失败')
  }
}

// 多选改变
function onSelectChange(rows: FeedbackItem[]) {
  multipleSelection.value = rows
}

// 批量删除
async function onBatchDelete() {
  await ElMessageBox.confirm(`确认删除选中的 ${multipleSelection.value.length} 条？`, '请确认', {
    type: 'warning',
  })
  try {
    await Promise.all(
        multipleSelection.value.map(item =>
            axios.delete('/feedback', { params: { id: item.id } })
        )
    )
    ElMessage.success('批量删除成功')
    fetchFeedback()
  } catch {
    ElMessage.error('批量删除失败')
  }
}

onMounted(fetchFeedback)
</script>

<style scoped>
.main-content {
  padding: 20px;
  background: #faf6f2;
}
</style>
