<template>
  <el-container class="wordset-manager-container">
    <el-container>
       <!-- 右侧主内容 -->
      <el-main class="main-content">
        <!-- 顶部操作栏 -->
        <div class="top-bar">
          <el-button type="primary" @click="openCreateDialog">新建词书</el-button>
          <el-button type="danger" @click="openDeleteDialog">删除</el-button>
        </div>

        <!-- 词书列表表格 -->
        <el-table
            :data="wordBooks"
            style="width: 100%;"
            @selection-change="handleSelectionChange"
            border
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="100" align="center" />
          <el-table-column prop="name" label="词书名称" align="center" />
          <el-table-column label="操作" align="center" width="220">
            <template #default="scope">
              <el-button
                  type="text"
                  size="small"
                  @click="handleImport(scope.row)"
              >
                导入
              </el-button>
              <el-button
                  type="text"
                  size="small"
                  @click="handleManageWords(scope.row)"
              >
                管理单词
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="footer-bar">
          <div class="total-info">共 {{ total }} 项数据</div>
          <el-pagination
              background
              layout="prev, pager, next, jumper"
              :total="total"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />
        </div>

        <!-- 新建词书对话框 -->
        <el-dialog
            title="创建词书"
            :visible.sync="createDialogVisible"
            width="400px"
            :before-close="() => (createDialogVisible = false)"
        >
          <el-form
              ref="createFormRef"
              :model="newWordBook"
              :rules="rules"
              label-width="0px"
          >
            <el-form-item prop="name">
              <el-input v-model="newWordBook.name" placeholder="词书名称" />
            </el-form-item>

              <el-form-item prop="cover">
                <el-input v-model="newWordBook.cover" placeholder="封面URL" />
              </el-form-item>
              <el-form-item prop="content">
                <el-input
                  type="textarea"
                  v-model="newWordBook.content"
                  placeholder="初始单词列表，逗号分隔"
                />
              </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="createDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmCreate">确认创建</el-button>
          </span>
        </el-dialog>

        <!-- 删除确认对话框 -->
        <el-dialog
            title="提示"
            :visible.sync="deleteDialogVisible"
            width="350px"
            :before-close="() => (deleteDialogVisible = false)"
        >
          <span>确定要删除所选词书吗？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="deleteDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmDelete"
            >确认删除</el-button
            >
          </span>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import { ElMessage } from "element-plus";

/** 侧边栏高亮 */
const route = useRoute();
const activeMenu = ref(route.path);
watch(
    () => route.path,
    (newPath) => {
      activeMenu.value = newPath;
    }
);

/** 路由实例 */
const router = useRouter();

/** 分页参数 */
const currentPage = ref(1);
const total = ref(0);

/** 词书列表数组 [{id, name, cover, content}] */
const wordBooks = ref([]);

/** 选中行 */
const selectedWordBooks = ref([]);

/** “新建词书” 对话框相关 */
const createDialogVisible = ref(false);
const newWordBook = reactive({
  name: "",
  cover: "",
  content: []
});
const createFormRef = ref(null);
const rules = {
  name: [{ required: true, message: "请输入词书名称", trigger: "blur" }],
   cover: [{ required: true, message: "请输入封面地址", trigger: "blur" }],
   content: [{ type: "array", required: true, message: "请选择初始单词", trigger: "change" }],
};

/** “删除词书” 对话框 */
const deleteDialogVisible = ref(false);

/**
 * 退出登录
 */
function onLogout() {
  localStorage.removeItem("admin_token");
  ElMessage.success("已退出登录");
  router.push({ path: "/login" });
}

/**
 * 拉取词书列表（带分页）
 * 假定后端接口：GET /wordbook?page=<currentPage>
 * 返回 JSON 示例： { total: 101, list: [ {id, name, cover, content}, ... ] }
 */
async function fetchWordBooks() {
  try {
    const resp = await axios.get("/wordbook", {
      params: {
        page: currentPage.value,
      },
    });
    if (resp.status === 200) {
      // 根据后端实际返回字段改成 resp.data.xxx
      total.value = resp.data.total || 0;
      wordBooks.value = resp.data.list || [];
    } else {
      ElMessage.error(`获取词书列表失败 (状态码 ${resp.status})`);
    }
  } catch (err) {
    console.error("fetchWordBooks 异常：", err);
    ElMessage.error("网络异常，无法获取词书列表");
  }
}

/**
 * 点击分页页码
 */
function handlePageChange(page) {
  currentPage.value = page;
  fetchWordBooks();
}

/**
 * 表格选中行变化
 */
function handleSelectionChange(val) {
  selectedWordBooks.value = val;
}

/**
 * 点击 “新建词书”
 */
function openCreateDialog() {
  newWordBook.name = "";
   newWordBook.cover = "";
   newWordBook.content = [];
  createDialogVisible.value = true;
}

/**
 * 点击 “确认创建” 时，调用 POST /wordbook
 */
async function confirmCreate() {
  createFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      const bodyPayload = {
        name: newWordBook.name,
        cover: newWordBook.cover,
        content: newWordBook.content,
      };
      const resp = await axios.post("/wordbook", bodyPayload);

      if (resp.status === 200) {
        ElMessage.success("词书创建成功");
        createDialogVisible.value = false;
        // 刷新列表，从第一页开始看
        currentPage.value = 1;
        fetchWordBooks();
      } else {
        const msg =
            (resp.data && resp.data.message) ||
            `创建失败 (状态码 ${resp.status})`;
        ElMessage.error(msg);
      }
    } catch (err) {
      console.error("confirmCreate 异常：", err);
      ElMessage.error("网络异常，创建失败");
    }
  });
}

/**
 * 点击 “删除” 按钮
 */
function openDeleteDialog() {
  if (selectedWordBooks.value.length === 0) {
    ElMessage.warning("请先选择要删除的词书");
    return;
  }
  deleteDialogVisible.value = true;
}

/**
 * 确认删除所选词书
 * 根据接口文档：DELETE /wordbook?id=<id>
 * 后端只接收一个 id，所以前端这里需要循环调用或者并发调用。
 */
async function confirmDelete() {
  try {
    // 并发删除所有选中行
    const deletePromises = selectedWordBooks.value.map((row) =>
        axios.delete("/wordbook", { params: { id: row.id } })
    );
    const results = await Promise.all(deletePromises);

    // 检查是否全都删除成功（HTTP 200）
    const allSuccess = results.every((resp) => resp.status === 200);
    if (allSuccess) {
      ElMessage.success("删除成功");
      deleteDialogVisible.value = false;
      selectedWordBooks.value = [];
      // 如果当前页刚好被删空，向前翻一页
      if (
          wordBooks.value.length === results.length &&
          currentPage.value > 1
      ) {
        currentPage.value -= 1;
      }
      fetchWordBooks();
    } else {
      ElMessage.error("部分删除失败，请重试");
    }
  } catch (err) {
    console.error("confirmDelete 异常：", err);
    ElMessage.error("网络异常，删除失败");
  }
}

/**
 * 点击 “导入” 按钮：跳转到 单词管理 (WordManager.vue)
 * 路由示例：/wordsets/:wordsetId/words
 * 下面假定路由名称是 WordManager，path 配置为 /wordsets/:wordsetId/words
 */
function handleImport(row) {
  router.push({
    name: "wordmanager",
    params: { wordsetId: row.id },
  });
}

/**
 * 点击 “管理单词” 按钮：同样跳转到单词管理
 */
function handleManageWords(row) {
  router.push({
    name: "WordManager",
    params: { wordsetId: row.id },
  });
}

onMounted(() => {
  fetchWordBooks();
});
</script>

<style scoped>
.wordset-manager-container {
  height: 100vh;
  background-color: #f9f6f2;
}

.header {
  background-color: #2d3a4b;
  color: #fff;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left .logo {
  font-size: 18px;
  color: #fff;
}

.header-right {
  color: #fff;
  cursor: pointer;
}

.sidebar {
  background-color: #2d3a4b;
  color: #fff;
  padding-top: 20px;
}

.user-info {
  text-align: center;
  margin-bottom: 20px;
  color: #fff;
}

.avatar {
  margin-bottom: 8px;
}

.user-text .username {
  font-size: 14px;
  margin: 0;
}

.user-text .status {
  font-size: 12px;
  color: #9fa6b7;
  margin: 0;
}

.el-menu-vertical-demo {
  border-right: none;
}

.main-content {
  padding: 20px;
  background-color: #faf6f2;
}

.top-bar {
  margin-bottom: 16px;
}

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

.dialog-footer {
  text-align: right;
}

.el-dialog__body {
  padding: 20px 30px;
}
</style>
