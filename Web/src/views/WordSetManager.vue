<template>
  <el-container style="height:100vh">

    <el-main class="main-content">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="openCreateDialog">新建词书</el-button>
        <el-button
            type="danger"
            :disabled="!selectedIds.length"
            @click="handleBatchDelete"
        >删除</el-button>
      </div>

      <!-- 词书列表 -->
      <el-table
          :data="pagedWordbooks"
          @selection-change="onSelectionChange"
          border
          stripe
          style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="词书名称"></el-table-column>
        <el-table-column prop="wordcount" label="单词数量" width="120"></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
                type="text"
                @click="goManageWords(row)"
            >管理单词</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页（去掉 page-sizes，下方只有总数/页码/跳转）-->
      <el-pagination
          class="pagination"
          background
          :total="total"
          :page-size="pageSize"
          :current-page="page"
          @current-change="onPageChange"
          layout="total, prev, pager, next, jumper"
      />
    </el-main>

    <!-- 新建词书对话框 -->
    <el-dialog
        title="新建词书"
        v-model="createDialogVisible"
        width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入词书名称" />
        </el-form-item>
        <el-form-item label="封面 URL">
          <el-input v-model="form.cover" placeholder="请输入封面图片 URL" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
              type="textarea"
              v-model="contentString"
              autosize
              placeholder="请用分号分隔单词，例如：apple; banana; orange"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCreate">创建</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

interface WordbookItem {
  id: number
  name: string
  cover: string
  wordcount: number
}

// 路由实例，用来跳转“管理单词”页面
const router = useRouter()

// 全量数据 & 选中 ID 列表
const wordbooks = ref<WordbookItem[]>([])
const selectedIds = ref<number[]>([])

// 分页状态
const page = ref(1)
const pageSize = 10
const total = computed(() => wordbooks.value.length)
const pagedWordbooks = computed(() => {
  const start = (page.value - 1) * pageSize
  return wordbooks.value.slice(start, start + pageSize)
})

// 新建对话框 & 表单
const createDialogVisible = ref(false)
const form = ref({ name: '', cover: '' })
const contentString = ref('')

/** 初始拉数据 */
async function fetchWordbooks() {
  try {
    const res = await axios.get<WordbookItem[]>('/wordbook')
    wordbooks.value = res.data
  } catch (err) {
    console.error(err)
    ElMessage.error('获取词书列表失败')
  }
}

/** 翻页 */
function onPageChange(p: number) {
  page.value = p
}

/** 选中项变化 */
function onSelectionChange(selection: WordbookItem[]) {
  selectedIds.value = selection.map(i => i.id)
}

/** 跳转到单词管理 */
function goManageWords(row: WordbookItem) {
  router.push({
    path: '/wordmanager',
    query: { wordbookId: String(row.id) }
  })
}

/** 批量删除 */
async function handleBatchDelete() {
  if (!selectedIds.value.length) return
  try {
    await Promise.all(
        selectedIds.value.map(id =>
            axios.delete('/wordbook', { params: { id } })
        )
    )
    ElMessage.success('删除成功')
    await fetchWordbooks()
  } catch (err) {
    console.error(err)
    ElMessage.error('删除失败')
  }
}

/** 打开新建对话框 */
function openCreateDialog() {
  form.value = { name: '', cover: '' }
  contentString.value = ''
  createDialogVisible.value = true
}

/** 确认新建 */
async function confirmCreate() {
  // 简单校验
  if (!form.value.name.trim() || !form.value.cover.trim()) {
    return ElMessage.warning('名称和封面 URL 都是必填项')
  }
  // 将分号分隔的字符串拆成数组
  const contentArr = contentString.value
      .split(';')
      .map(w => w.trim())
      .filter(w => w)
  const payload = {
    name: form.value.name.trim(),
    cover: form.value.cover.trim(),
    content: contentArr
  }
  try {
    await axios.post('/wordbook', payload)
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    await fetchWordbooks()
  } catch (err) {
    console.error(err)
    ElMessage.error('创建失败，请稍后重试')
  }
}

// 页面加载时先拉一次
onMounted(fetchWordbooks)
</script>

<style scoped>
.main-content {
  background-color: #faf6f2;
  padding: 20px;
  height: 100%;
}
.toolbar {
  margin-bottom: 16px;
}
.pagination {
  margin-top: 16px;
  text-align: right;
}
</style>
