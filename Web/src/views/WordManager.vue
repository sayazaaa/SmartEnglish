<template>
  <el-container style="height: 100vh;">
    <!-- 主体内容：页头已在 App.vue 中渲染，这里只负责表格 -->
    <el-main class="main-content">
      <el-table
          v-loading="loading"
          :data="visibleWords"
          stripe
          border
          style="width: 100%"
      >
        <!-- ID 列 -->
        <el-table-column prop="id" label="ID" width="80" />

        <!-- 单词 列 -->
        <el-table-column prop="word" label="单词" />

        <!-- 操作：更新 -->
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="mini"
                @click="openEdit(row)"
            >更新</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 本地分页控件 -->
      <div class="pagination-wrap">
        <el-pagination
            background
            layout="total, sizes, prev, pager, next"
            :total="total"
            :page-size="size"
            :current-page="page"
            :page-sizes="[10, 20, 50]"
            @current-change="onPageChange"
            @size-change="onSizeChange"
        />
      </div>
    </el-main>

    <!-- 更新对话框 -->
    <el-dialog
        title="更新单词"
        v-model="createDialogVisible"
        width="50%"
        @close="resetForm"
    >
      <el-form
          ref="formRef"
          :model="form"
          label-width="100px"
      >
        <el-form-item label="单词" prop="word">
          <el-input v-model="form.word" autocomplete="off" />
        </el-form-item>
        <el-form-item label="音标" prop="phonetic">
          <el-input v-model="form.phonetic" autocomplete="off" />
        </el-form-item>
        <el-form-item label="读音 URL" prop="pronunciation">
          <el-input v-model="form.pronunciation" autocomplete="off" />
        </el-form-item>
        <el-form-item label="释义" prop="explanations">
          <el-input
              type="textarea"
              :rows="3"
              v-model="explainText"
              placeholder="每行一条释义"
          />
        </el-form-item>
        <el-form-item label="同义词 (a/v/n)" prop="synonyms">
          <el-input
              type="textarea"
              :rows="2"
              v-model="synText"
              placeholder="格式：a:apple,apply; v:run,go; n:fruit,piece"
          />
        </el-form-item>
        <el-form-item label="反义词 (a/v/n)" prop="antonyms">
          <el-input
              type="textarea"
              :rows="2"
              v-model="antText"
              placeholder="格式：a:bad,evil; v:stop,halt; n:loss,defeat"
          />
        </el-form-item>
        <el-form-item label="示例句" prop="examples">
          <el-input
              type="textarea"
              :rows="3"
              v-model="exampText"
              placeholder="格式：English text||中文翻译（每行一个）"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

/** 单词项接口 */
interface WordItem {
  id: number
  word: string
  phonetic: string | null
  pronunciation: string | null
  explanations: string[]       // 释义列表
  synonyms: Record<string,string[]>  // 同义词：{ a:[], v:[], n:[] }
  antonyms: Record<string,string[]>  // 反义词
  examples: { english:string; chinese:string }[]
}

// 数据列表 & 分页
const words = ref<WordItem[]>([])
const loading = ref(false)
const total = ref(0)
const page = ref(1)
const size = ref(10)

// 本地分页计算
const visibleWords = computed(() => {
  const start = (page.value - 1) * size.value
  return words.value.slice(start, start + size.value)
})

// 1. 拉取所有单词
async function fetchWords() {
  loading.value = true
  try {
    const res = await axios.get<WordItem[]>('/word/all')
    words.value = res.data
    total.value = res.data.length
  } catch (e) {
    ElMessage.error('获取单词列表失败')
  } finally {
    loading.value = false
  }
}

// 分页回调
function onPageChange(p: number) {
  page.value = p
}
function onSizeChange(s: number) {
  size.value = s
  page.value = 1
}

// --- 编辑对话框 ---
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive<WordItem>({
  id: 0,
  word: '',
  phonetic: null,
  pronunciation: null,
  explanations: [],
  synonyms: { a: [], v: [], n: [] },
  antonyms: { a: [], v: [], n: [] },
  examples: []
})

// 辅助中间变量，用于 textarea ↔ 数组/对象 的转换
const explainText = ref('')
const synText = ref('')   // 格式：a:apple,apply; v:run,go
const antText = ref('')
const exampText = ref('') // 格式：English||中文，每行一条

// 打开对话框并填充
function openEdit(row: WordItem) {
  Object.assign(form, JSON.parse(JSON.stringify(row)))
  // 释义
  explainText.value = form.explanations.join('\n')
  // 同义词格式化
  synText.value = Object.entries(form.synonyms)
      .map(([k,arr]) => `${k}:${arr?.join(',')}`)
      .join('; ')
  // 反义词
  antText.value = Object.entries(form.antonyms)
      .map(([k,arr]) => `${k}:${arr?.join(',')}`)
      .join('; ')
  // 例句
  exampText.value = form.examples
      .map(e => `${e.english}||${e.chinese}`)
      .join('\n')

  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

// 提交更新
async function submitEdit() {
  // 同步回 form 对象
  form.explanations = explainText.value
      .split('\n').map(s=>s.trim()).filter(s=>s)
  ;[form.synonyms, form.antonyms] = [synText.value, antText.value].map(txt => {
    const obj: Record<string,string[]> = { a: [], v: [], n: [] }
    txt.split(';').forEach(seg => {
      const [key, vals] = seg.split(':')
      if (obj[key] && vals) {
        obj[key] = vals.split(',').map(s=>s.trim()).filter(s=>s)
      }
    })
    return obj
  })
  form.examples = exampText.value
      .split('\n')
      .map(line => line.split('||'))
      .filter(arr => arr.length===2)
      .map(([eng,chn]) => ({ english: eng.trim(), chinese: chn.trim() }))

  try {
    await axios.put('/word', form)
    ElMessage.success('更新成功')
    dialogVisible.value = false
    fetchWords()
  } catch {
    ElMessage.error('更新失败')
  }
}

// 重置表单
function resetForm() {
  formRef.value?.resetFields?.()
}

// 首次加载
onMounted(fetchWords)
</script>

<style scoped>
.main-content {
  padding: 20px;
  background: #faf6f2;
}
/* 分页居中 */
.pagination-wrap {
  padding: 16px 0;
  text-align: center;
}
</style>
