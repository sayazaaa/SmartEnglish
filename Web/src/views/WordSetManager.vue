<template>
  <el-container style="height:100vh">

    <!-- 侧边栏省略… -->

    <el-main class="main-content">
      <el-button type="primary" @click="onOpenCreateDialog">新建词书</el-button>
      <el-button type="danger" :disabled="!multipleSelection.length" @click="onBatchDelete">删除</el-button>

      <el-table
          :data="wordbooks"
          @selection-change="multipleSelection = $event"
          style="width:100%; margin-top: 20px"
          v-loading="loading"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="name" label="词书名称"/>
        <el-table-column prop="wordcount" label="单词数" width="100"/>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="onOpenEditDialog(row)">编辑词书</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          background
          :current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="sizes, prev, pager, next, jumper"
          @size-change="onSizeChange"
          @current-change="onPageChange"
          style="margin-top: 20px; text-align: right"
      />
    </el-main>

    <!-- 新建词书弹窗 -->
    <el-dialog
        title="新建词书"
        v-model="createDialogVisible"
        width="500px"
        @close="resetCreateForm"
    >
      <el-form :model="createForm">
        <el-form-item label="名称">
          <el-input v-model="createForm.name" />
        </el-form-item>
        <el-form-item label="封面(链接)">
          <el-input v-model="createForm.cover" />
        </el-form-item>
        <el-form-item label="内容(分号隔开)">
          <el-input
              v-model="createForm.content"
              placeholder="word1;word2;word3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onCreateSubmit">提交</el-button>
      </template>
    </el-dialog>

    <!-- 编辑词书弹窗 -->
    <el-dialog
        title="编辑词书"
        v-model="editDialogVisible"
        width="500px"
        @close="resetEditForm"
    >
      <el-form :model="editForm">
        <el-form-item label="名称">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="封面(链接)">
          <el-input v-model="editForm.cover" />
        </el-form-item>
        <el-form-item label="内容(分号隔开)">
          <el-input
              v-model="editForm.content"
              placeholder="word1;word2;word3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onEditSubmit">保存</el-button>
      </template>
    </el-dialog>

  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 列表数据 & 分页
const wordbooks = ref([])
const loading = ref(false)
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const multipleSelection = ref([])

// 新建弹窗相关
const createDialogVisible = ref(false)
const createForm = ref({
  name: '',
  cover: '',
  content: ''
})
function resetCreateForm() {
  createForm.value = { name: '', cover: '', content: '' }
}
function onOpenCreateDialog() {
  resetCreateForm()
  createDialogVisible.value = true
}
async function onCreateSubmit() {
  try {
    const arr = createForm.value.content
        .split(';')
        .map(s => s.trim())
        .filter(Boolean)
    await axios.post('/wordbook', {
      name: createForm.value.name,
      cover: createForm.value.cover,
      content: arr
    })
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    fetchList()
  } catch {
    ElMessage.error('创建失败')
  }
}

// 编辑弹窗相关
const editDialogVisible = ref(false)
const editForm = ref({
  id: null,
  name: '',
  cover: '',
  content: ''
})
function resetEditForm() {
  editForm.value = { id: null, name: '', cover: '', content: '' }
}
function onOpenEditDialog(row) {
  editForm.value.id = row.id
  editForm.value.name = row.name
  editForm.value.cover = row.cover
  // 假设后端返回的是数组
  editForm.value.content = (row.content || []).join(';')
  editDialogVisible.value = true
}
async function onEditSubmit() {
  try {
    const arr = editForm.value.content
        .split(';')
        .map(s => s.trim())
        .filter(Boolean)
    await axios.put('/wordbook', {
      id: editForm.value.id,
      name: editForm.value.name,
      cover: editForm.value.cover,
      content: arr
    })
    ElMessage.success('保存成功')
    editDialogVisible.value = false
    fetchList()
  } catch {
    ElMessage.error('保存失败')
  }
}

// 删除
async function onBatchDelete() {
  try {
    const ids = multipleSelection.value.map(i => i.id)
    await Promise.all(ids.map(id => axios.delete('/wordbook', { params: { id } })))
    ElMessage.success('删除成功')
    fetchList()
  } catch {
    ElMessage.error('删除失败')
  }
}

// 拉列表
async function fetchList() {
  loading.value = true
  try {
    const res = await axios.get('/wordbook', {
      params: { page: page.value, pageSize: pageSize.value }
    })
    wordbooks.value = res.data
    total.value = parseInt(res.headers['x-total-count'] || res.data.length)
  } catch {
    ElMessage.error('获取失败')
  } finally {
    loading.value = false
  }
}

// 分页回调
function onSizeChange(size) {
  pageSize.value = size
  fetchList()
}
function onPageChange(p) {
  page.value = p
  fetchList()
}

onMounted(fetchList)
</script>

<style scoped>
.main-content {
  padding: 20px;
  background: #faf6f2;
}
</style>
