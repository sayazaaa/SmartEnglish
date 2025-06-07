<template>
  <el-container style="height: 100vh">
    <el-main class="main-content">
      <!-- 操作按钮 -->
      <div style="margin-bottom: 20px;">
        <el-button type="primary" @click="openNew">新建文章</el-button>
        <el-button
            type="danger"
            :disabled="multipleSelection.length === 0"
            @click="deleteArticles"
        >
          删除
        </el-button>
      </div>

      <!-- 数据表格 -->
      <el-table
          :data="pagedData"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          style="width: 100%;"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="阅读材料名称" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="editArticle(row)">编辑文章</el-button>
            <el-button type="text" @click="importWords(row)">导入</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页与总数 -->
      <div
          style="margin-top: 20px; display: flex; justify-content: space-between; align-items: center;"
      >
        <div>共 {{ total }} 项数据</div>
        <el-pagination
            background
            :page-size="pageSize"
            :current-page="page"
            :total="total"
            @current-change="handlePageChange"
            layout="prev, pager, next, jumper"
        />
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const router = useRouter();

// 数据状态
const articles = ref([]);
const loading = ref(false);
const page = ref(1);
const pageSize = ref(10);
const total = ref(0);
const multipleSelection = ref([]);

// 拉取文章列表 —— 注意：只传 query_string，不带 page/pageSize
async function fetchArticles() {
  loading.value = true;
  try {
    const res = await axios.get('/article/search', {
      params: { query_string: '' }
    });
    articles.value = res.data;
    total.value = res.data.length;
  } catch (err) {
    ElMessage.error('网络异常，无法获取文章列表');
  } finally {
    loading.value = false;
  }
}

// 计算当前页要展示的数据切片
const pagedData = computed(() => {
  const start = (page.value - 1) * pageSize.value;
  return articles.value.slice(start, start + pageSize.value);
});

// 处理表格多选
function handleSelectionChange(val) {
  multipleSelection.value = val;
}

// 分页切换事件
function handlePageChange(newPage) {
  page.value = newPage;
}

// 新建文章
function openNew() {
  router.push('/articlemanager/new');
}

// 编辑文章
function editArticle(row) {
  router.push(`/articlemanager/edit/${row.id}`);
}

// 导入单词
function importWords(row) {
  router.push(`/articlemanager/import/${row.id}`);
}

// 删除选中文章
async function deleteArticles() {
  const ids = multipleSelection.value.map((item) => item.id);
  try {
    await Promise.all(
        ids.map((id) =>
            axios.delete('/article', {
              params: { id }
            })
        )
    );
    ElMessage.success('删除成功');
    fetchArticles();
  } catch {
    ElMessage.error('删除失败');
  }
}

// 初始拉数据
onMounted(fetchArticles);
</script>

<style scoped>
.main-content {
  padding: 20px;
  background-color: #faf6f2;
}
</style>
