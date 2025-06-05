<template>
  <el-container class="article-manager-container">
    <div class="page-content">
      <!-- 顶部操作栏 -->
      <div class="top-bar">
        <el-button type="primary" @click="openCreateDialog">新建文章</el-button>
        <el-button type="danger" @click="openDeleteDialog">删除</el-button>
      </div>

      <!-- 文章列表表格 -->
      <el-table
          :data="computedArticles"
          style="width: 100%;"
          @selection-change="handleSelectionChange"
          border
      >
        <el-table-column type="selection" width="55" />

        <el-table-column prop="id" label="ID" width="100" align="center" />
        <el-table-column prop="title" label="阅读材料名称" align="center" />

        <el-table-column label="操作" align="center" width="240">
          <template #default="scope">
            <el-button
                type="text"
                size="small"
                @click="openEditDialog(scope.row)"
            >
              编辑文章
            </el-button>
            <el-button
                type="text"
                size="small"
                @click="handleImport(scope.row)"
            >
              导入
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 底部分页与统计（去掉“10/page”下拉） -->
      <div class="footer-bar">
        <div class="total-info">共 {{ total }} 项数据</div>
        <el-pagination
            background
            layout="prev, pager, next, jumper"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 新建文章对话框 -->
    <el-dialog
        title="新建文章"
        :visible.sync="createDialogVisible"
        width="600px"
        :before-close="() => (createDialogVisible = false)"
        append-to-body
    >
      <el-form
          ref="createFormRef"
          :model="newArticle"
          :rules="articleRules"
          label-width="100px"
          label-position="left"
          class="create-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="newArticle.title" placeholder="请输入标题" />
        </el-form-item>

        <el-form-item label="封面 URL" prop="cover">
          <el-input v-model="newArticle.cover" placeholder="请输入封面地址" />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
              type="textarea"
              v-model="newArticle.content"
              placeholder="请输入文章内容"
              rows="4"
          />
        </el-form-item>

        <el-form-item label="发布日期" prop="date">
          <el-date-picker
              v-model="newArticle.date"
              type="date"
              placeholder="选择发布日期"
              style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="标签（逗号分隔）" prop="tags">
          <el-input
              v-model="newArticle.tagsInput"
              placeholder="例如：阅读,英语,考试"
          />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCreate">确认创建</el-button>
      </span>
    </el-dialog>

    <!-- 编辑文章对话框 -->
    <el-dialog
        title="编辑文章"
        :visible.sync="editDialogVisible"
        width="600px"
        :before-close="() => (editDialogVisible = false)"
        append-to-body
    >
      <el-form
          ref="editFormRef"
          :model="editArticle"
          :rules="articleRules"
          label-width="100px"
          label-position="left"
          class="edit-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="editArticle.title" />
        </el-form-item>

        <el-form-item label="封面 URL" prop="cover">
          <el-input v-model="editArticle.cover" />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
              type="textarea"
              v-model="editArticle.content"
              rows="4"
          />
        </el-form-item>

        <el-form-item label="发布日期" prop="date">
          <el-date-picker
              v-model="editArticle.date"
              type="date"
              placeholder="选择发布日期"
              style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="标签（逗号分隔）" prop="tags">
          <el-input v-model="editArticle.tagsInput" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">保存修改</el-button>
      </span>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
        title="提示"
        :visible.sync="deleteDialogVisible"
        width="350px"
        :before-close="() => (deleteDialogVisible = false)"
        append-to-body
    >
      <span>确定要删除所选文章吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDelete">确认删除</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

// **路由实例（用于“导入”或跳转到编辑页面）**
const router = useRouter();

// **分页参数：前端分页**
const currentPage = ref(1);
const pageSize = ref(10);
const articles = ref([]); // 从后端 GET /article/search 拉回来的全部文章
const total = computed(() => articles.value.length);

// **选中行**
const selectedArticles = ref([]);

// —— “新建文章” 对话框相关 —— //
const createDialogVisible = ref(false);
const createFormRef = ref(null);
const newArticle = reactive({
  title: "",
  cover: "",
  content: "",
  date: "",       // 存储为 YYYY-MM-DD 字符串
  tagsInput: "",  // 前端存储“逗号分隔”的标签字符串，提交时再 split
});

// **表单校验规则**
const articleRules = {
  title: [{ required: true, message: "请输入标题", trigger: "blur" }],
  cover: [{ required: true, message: "请输入封面 URL", trigger: "blur" }],
  content: [{ required: true, message: "请输入内容", trigger: "blur" }],
  date: [{ type: "date", required: true, message: "请选择发布日期", trigger: "change" }],
  tags: [{ required: true, message: "请输入至少一个标签", trigger: "blur" }],
};

// —— “编辑文章” 对话框相关 —— //
const editDialogVisible = ref(false);
const editFormRef = ref(null);
const editArticle = reactive({
  id: null,
  title: "",
  cover: "",
  content: "",
  date: "",
  tagsInput: "",
});

// —— “删除文章” 对话框 —— //
const deleteDialogVisible = ref(false);

/**
 * 前端分页计算：当前页要在 table 里渲染的数据
 */
const computedArticles = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return articles.value.slice(start, end);
});

/**
 * 拉取“所有文章”：GET /article/search?query_string=""
 * 假定后端返回：[{ id, title, cover, content, date, tags: [..] }, ...]
 */
async function fetchArticles() {
  try {
    const resp = await axios.get("/article/search", {
      params: { query_string: "" }, // 空串表示“拉取全部”
    });
    if (resp.status === 200) {
      // 如果后端直接返回一个数组
      articles.value = resp.data || [];
    } else {
      ElMessage.error(`获取文章列表失败 (状态 ${resp.status})`);
    }
  } catch (err) {
    console.error("fetchArticles 异常：", err);
    ElMessage.error("网络异常，无法获取文章列表");
  }
}

/** 分页页码变化 */
function handlePageChange(page) {
  currentPage.value = page;
}

/** 表格选中行变化 */
function handleSelectionChange(val) {
  selectedArticles.value = val;
}

/** 点击“新建文章” */
function openCreateDialog() {
  // 重置表单
  newArticle.title = "";
  newArticle.cover = "";
  newArticle.content = "";
  newArticle.date = "";
  newArticle.tagsInput = "";
  createDialogVisible.value = true;
}

/** 点击“确认创建” */
async function confirmCreate() {
  createFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      // 把 tagsInput（逗号分隔）转成数组
      const tagsArr = newArticle.tagsInput
          .split(",")
          .map((t) => t.trim())
          .filter((t) => t !== "");

      const payload = {
        title: newArticle.title,
        cover: newArticle.cover,
        content: newArticle.content,
        date: newArticle.date, // “YYYY-MM-DD”
        tags: tagsArr,
      };

      const resp = await axios.post("/article", payload);
      if (resp.status === 200) {
        ElMessage.success("文章创建成功");
        createDialogVisible.value = false;
        // 重新拉取全部文章，并跳回第 1 页
        currentPage.value = 1;
        await fetchArticles();
      } else {
        const msg =
            (resp.data && resp.data.message) ||
            `创建失败 (状态 ${resp.status})`;
        ElMessage.error(msg);
      }
    } catch (err) {
      console.error("confirmCreate 异常：", err);
      ElMessage.error("网络异常，创建失败");
    }
  });
}

/** 点击“编辑文章” */
async function openEditDialog(row) {
  try {
    // 先调用 GET /article?id=<row.id> 拿到完整文章数据
    const resp = await axios.get("/article", {
      params: { id: row.id },
    });
    if (resp.status === 200) {
      const data = resp.data;
      // 填充到 editArticle
      editArticle.id = data.id;
      editArticle.title = data.title;
      editArticle.cover = data.cover;
      editArticle.content = data.content;
      // 假定后端的 date 字段就是 “YYYY-MM-DD”
      editArticle.date = data.date;
      // 假定后端返回 tags 是数组，前端显示成 “逗号分隔” 形式
      editArticle.tagsInput = (data.tags || []).join(",");
      editDialogVisible.value = true;
    } else {
      ElMessage.error(`获取文章内容失败 (状态 ${resp.status})`);
    }
  } catch (err) {
    console.error("openEditDialog 异常：", err);
    ElMessage.error("网络异常，获取失败");
  }
}

/** 点击“保存修改” */
async function confirmEdit() {
  editFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      const tagsArr = editArticle.tagsInput
          .split(",")
          .map((t) => t.trim())
          .filter((t) => t !== "");

      const payload = {
        id: editArticle.id,
        title: editArticle.title,
        cover: editArticle.cover,
        content: editArticle.content,
        date: editArticle.date,
        tags: tagsArr,
      };

      const resp = await axios.put("/article", payload);
      if (resp.status === 200) {
        ElMessage.success("文章更新成功");
        editDialogVisible.value = false;
        await fetchArticles();
      } else {
        ElMessage.error(`更新失败 (状态 ${resp.status})`);
      }
    } catch (err) {
      console.error("confirmEdit 异常：", err);
      ElMessage.error("网络异常，更新失败");
    }
  });
}

/** 点击“删除”按钮 */
function openDeleteDialog() {
  if (selectedArticles.value.length === 0) {
    ElMessage.warning("请先选择要删除的文章");
    return;
  }
  deleteDialogVisible.value = true;
}

/** 确认删除选中文章 */
async function confirmDelete() {
  try {
    // 并发删除所有选中行：DELETE /article?id=<id>
    const promises = selectedArticles.value.map((row) =>
        axios.delete("/article", { params: { id: row.id } })
    );
    const results = await Promise.all(promises);
    const allOk = results.every((r) => r.status === 200);
    if (allOk) {
      ElMessage.success("删除成功");
      deleteDialogVisible.value = false;
      selectedArticles.value = [];
      // 如果当前页条目刚好被删光，则翻到上一页
      if (computedArticles.value.length === results.length && currentPage.value > 1) {
        currentPage.value -= 1;
      }
      await fetchArticles();
    } else {
      ElMessage.error("部分删除失败，请重试");
    }
  } catch (err) {
    console.error("confirmDelete 异常：", err);
    ElMessage.error("网络异常，删除失败");
  }
}

/** 点击“导入”按钮——示例跳转逻辑，可替换为你自己项目需要的路由 */
function handleImport(row) {
  // 假设你想把当前文章 ID 传给 WordManager 或别的页面
  router.push({
    name: "WordManager",
    params: { wordsetId: row.id },
  });
}

onMounted(() => {
  fetchArticles();
});
</script>

<style scoped>
.article-manager-container {
  height: 100vh;
  background-color: #f9f6f2;
}
.page-content {
  padding: 20px;
  background-color: #faf6f2;
}

/* 顶部操作栏 */
.top-bar {
  margin-bottom: 16px;
}

/* 底部分页栏 */
.footer-bar {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-info {
  color: #606266;
  font-size: 14px;
}

/* create/edit 对话框的表单宽度自适应 */
.create-form,
.edit-form {
  max-height: 400px;
  overflow-y: auto;
}

/* 对话框底部按钮对齐 */
.dialog-footer {
  text-align: right;
}

/* el-dialog body 内部 padding 调整 */
.el-dialog__body {
  padding: 20px 30px;
}
</style>
