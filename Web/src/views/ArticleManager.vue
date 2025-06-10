<template>
  <el-container style="height:100vh">
    <!-- 主操作按钮 -->
    <el-header style="padding: 16px 0; background: #fff; display: flex; gap: 10px;">
      <el-button type="primary" @click="openCreateDialog">新建文章</el-button>
      <el-button type="danger" :disabled="!hasSelection" @click="handleBatchDelete">删除</el-button>
    </el-header>

    <el-main class="main-content">
      <el-table
          v-loading="loading"
          :data="articles"
          @selection-change="onSelectionChange"
          stripe
          style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="200"/>
        <el-table-column prop="title" label="阅读材料名称" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-link type="primary" @click="openViewDialog(row.id)">查看</el-link>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 16px 0;">
        共 {{ total }} 项数据
      </div>
    </el-main>

    <!-- 查看弹窗 -->
    <el-dialog
        title="文章详情"
        v-model="viewVisible"
        width="60%">
      <el-descriptions column="1" border>
        <el-descriptions-item label="ID">{{ viewForm.id }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item label="封面">
          <img :src="viewForm.cover" alt="封面" style="max-width:100%;"/>
        </el-descriptions-item>
        <el-descriptions-item label="内容">
          <div
              v-html="sanitizedContent"
              style="max-height:200px;overflow:auto;white-space:normal;"
          ></div>
        </el-descriptions-item>

        <el-descriptions-item label="日期">{{ viewForm.date }}</el-descriptions-item>
        <el-descriptions-item label="标签">
          <el-tag
              v-for="tag in viewForm.tags"
              :key="tag"
              style="margin-right:6px;"
          >{{ tag }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 新建弹窗 -->
    <el-dialog
        title="新建文章"
        v-model="createVisible"
        width="60%"
        @close="resetCreateForm">
      <el-form
          ref="createFormRef"
          :model="createForm"
          :rules="createRules"
          label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="createForm.title" />
        </el-form-item>
        <el-form-item label="封面链接" prop="cover">
          <el-input v-model="createForm.cover" placeholder="请填完整 URL" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
              type="textarea"
              v-model="createForm.content"
              autosize
          />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker
              v-model="createForm.date"
              type="date"
              placeholder="选择日期"
              style="width:100%;"
          />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-select
              v-model="createForm.tags"
              multiple
              placeholder="请添加标签"
              collapse-tags>
            <el-option
                v-for="tag in possibleTags"
                :key="tag"
                :label="tag"
                :value="tag"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCreate">确 定</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import {
  ElMessage,
  ElButton,
  ElContainer,
  ElHeader,
  ElMain,
  ElTable,
  ElTableColumn,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElDatePicker,
  ElSelect,
  ElOption,
  ElDescriptions,
  ElDescriptionsItem,
  ElTag,
  ElLink
} from 'element-plus';

import { computed } from 'vue'
import DOMPurify from 'dompurify'



// 列表数据
const articles = ref([]);
const loading = ref(false);
const total = ref(0);
// 选中项
const multipleSelection = ref([]);
const hasSelection = ref(false);

// 控制弹窗
const viewVisible = ref(false)

// 本地表单数据，先初始化所有字段
const viewForm = reactive({
  id: '',
  title: '',
  cover: '',
  content: '',
  date: '',
  tags: []
})

// 只过滤 h1~h6, p, img
const sanitizedContent = computed(() => {
  return DOMPurify.sanitize(viewForm.content || '', {
    ALLOWED_TAGS: ['h1','h2','h3','h4','h5','h6','p','img'],
    ALLOWED_ATTR: ['src','alt','title','width','height']
  })
})

// 假设你调用完接口后会填充 viewForm，然后打开弹窗
async function onView(id) {
  const res = await api.fetchArticle(id)
  Object.assign(viewForm, res.data)
  viewVisible.value = true
}
// 新建弹窗 & 表单
const createVisible = ref(false);
const createFormRef = ref(null);
const createForm = reactive({
  title: '',
  cover: '',
  content: '',
  date: '',
  tags: []
});
// 示例标签，你可以动态加载或自定义
const possibleTags = ['阅读','听力','语法','单词'];
const createRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  cover: [{ required: true, message: '请输入封面链接', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  tags: [{ type: 'array', required: true, message: '请至少选择一个标签', trigger: 'change' }]
};

function onSelectionChange(val) {
  multipleSelection.value = val;
  hasSelection.value = val.length > 0;
}

// 拉取文章列表
async function fetchArticles() {
  loading.value = true;
  try {
    const res = await axios.get('/article/search', {
      params: { query_string: '' }
    });
    // 文档：返回一个数组
    articles.value = res.data || [];
    total.value = articles.value.length;
  } catch {
    ElMessage.error('网络异常，无法获取文章列表');
  } finally {
    loading.value = false;
  }
}

// 打开查看
async function openViewDialog(id) {
  try {
    const res = await axios.get('/article', { params: { id } });
    Object.assign(viewForm, res.data);
    viewVisible.value = true;
  } catch {
    ElMessage.error('获取文章详情失败');
  }
}

// 打开新建
function openCreateDialog() {
  resetCreateForm();
  createVisible.value = true;
}

// 重置新建表单
function resetCreateForm() {
  createFormRef.value?.resetFields();
  createForm.title = '';
  createForm.cover = '';
  createForm.content = '';
  createForm.date = '';
  createForm.tags = [];
}

// 提交新建
async function submitCreate() {
  try {
    await createFormRef.value.validate();
    await axios.post('/article', {
      title: createForm.title,
      cover: createForm.cover,
      content: createForm.content,
      date: createForm.date,
      tags: createForm.tags
    });
    ElMessage.success('文章创建成功');
    createVisible.value = false;
    fetchArticles();
  } catch (err) {
    if (err === 'validate') return;
    ElMessage.error('新建文章失败');
  }
}

// 删除单篇
async function deleteArticle(id) {
  try {
    await axios.delete('/article', { params: { id } });
    ElMessage.success('删除成功');
    fetchArticles();
  } catch {
    ElMessage.error('删除失败');
  }
}

// 批量删除
async function handleBatchDelete() {
  const ids = multipleSelection.value.map(r => r.id);
  try {
    await Promise.all(
        ids.map(id => axios.delete('/article', { params: { id } }))
    );
    ElMessage.success('批量删除成功');
    fetchArticles();
  } catch {
    ElMessage.error('批量删除失败');
  }
}



onMounted(fetchArticles);
</script>

<style scoped>
.main-content {
  padding: 16px;
  background: #f8f8f8;
  word-break: break-all;
}

</style>
