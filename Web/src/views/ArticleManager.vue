<template>
  <el-container class="article-manager-container">
    <!-- 顶部 Header -->
    <el-header class="header">
      <div class="header-left">
        <span class="logo">管理系统</span>
      </div>
      <div class="header-right">
        <el-dropdown>
          <span class="el-dropdown-link">
            <i class="el-icon-user"></i>
            admin
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 左侧侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="user-info">
          <el-avatar
              size="large"
              src="https://via.placeholder.com/80x80.png?text=头像"
              class="avatar"
          />
          <div class="user-text">
            <p class="username">测试用户</p>
            <p class="status"><i class="el-icon-circle-online"></i> 在线</p>
          </div>
        </div>
        <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            background-color="#2d3a4b"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            router
        >
          <el-menu-item index="/wordsetmanager">
            <i class="el-icon-reading"></i>
            <span slot="title">词书管理</span>
          </el-menu-item>
          <el-menu-item index="/articlemanager">
            <i class="el-icon-folder-opened"></i>
            <span slot="title">阅读材料管理</span>
          </el-menu-item>
          <el-menu-item index="/wordmanager">
            <i class="el-icon-document"></i>
            <span slot="title">单词管理</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-data-analysis"></i>
            <span slot="title">数据分析</span>
          </el-menu-item>
          <el-menu-item index="/feedback">
            <i class="el-icon-message"></i>
            <span slot="title">用户反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 右侧主内容 -->
      <el-main class="main-content">
        <!-- 顶部操作栏 -->
        <div class="top-bar">
          <el-button type="primary" @click="openCreateDialog">
            新建文章
          </el-button>
          <el-button type="danger" @click="openDeleteDialog">
            删除
          </el-button>
        </div>

        <!-- 文章列表表格 -->
        <el-table
            :data="articles"
            style="width: 100%;"
            border
            @selection-change="onSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="100" align="center" />
          <el-table-column
              prop="title"
              label="阅读材料名称"
              align="center"
          />
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
                  @click="importArticle(scope.row)"
              >
                导入
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 底部分页与统计 -->
        <div class="footer-bar">
          <div class="total-info">共 {{ total }} 项数据</div>
          <el-pagination
              background
              layout="sizes, prev, pager, next, jumper"
              :total="total"
              :page-size="pageSize"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20, 50]"
              @current-change="onPageChange"
              @size-change="onSizeChange"
          />
        </div>

        <!-- 创建文章对话框 -->
        <el-dialog
            title="创建文章"
            :visible.sync="createDialogVisible"
            width="400px"
            :before-close="() => (createDialogVisible = false)"
        >
          <el-form
              ref="createFormRef"
              :model="newArticle"
              :rules="rules"
              label-width="0px"
          >
            <el-form-item prop="title">
              <el-input
                  v-model="newArticle.title"
                  placeholder="请输入阅读材料名称"
              />
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="createDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmCreate">
              确认创建
            </el-button>
          </span>
        </el-dialog>

        <!-- 编辑文章对话框 -->
        <el-dialog
            title="编辑文章"
            :visible.sync="editDialogVisible"
            width="400px"
            :before-close="() => (editDialogVisible = false)"
        >
          <el-form
              ref="editFormRef"
              :model="editingArticle"
              :rules="rules"
              label-width="0px"
          >
            <el-form-item prop="title">
              <el-input
                  v-model="editingArticle.title"
                  placeholder="修改阅读材料名称"
              />
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmEdit">
              保存修改
            </el-button>
          </span>
        </el-dialog>

        <!-- 删除确认对话框 -->
        <el-dialog
            title="提示"
            :visible.sync="deleteDialogVisible"
            width="350px"
            :before-close="() => (deleteDialogVisible = false)"
        >
          <span>确定要删除所选文章吗？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="deleteDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmDelete">
              确认删除
            </el-button>
          </span>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";

/**
 * 文章列表示例数据
 * 实际项目中，请从后端 API 获取并赋值给 articles
 */
const articles = ref([]); // 每一项形如 { id: 44, title: "xxxxxxx" }
const total = ref(0); // 总数据条数
const pageSize = ref(10); // 每页条数
const currentPage = ref(1); // 当前页码

// 选中行
const selectedArticles = ref([]);

// “创建文章”对话框相关
const createDialogVisible = ref(false);
const newArticle = reactive({ title: "" });
const createFormRef = ref(null);

// “编辑文章”对话框相关
const editDialogVisible = ref(false);
const editingArticle = reactive({ id: null, title: "" });
const editFormRef = ref(null);

// 表单校验规则
const rules = {
  title: [{ required: true, message: "请输入文章名称", trigger: "blur" }],
};

// “删除”对话框
const deleteDialogVisible = ref(false);

/**
 * 模拟拉取文章列表
 * 实际项目中，从后端 API 获取数据并更新 articles、total
 */
function fetchArticles() {
  // 示例：假设总共 101 条数据
  total.value = 101;

  // 根据 currentPage 和 pageSize，从后端拉取对应页数据
  // 这里只做简单示例，只给出一条记录
  articles.value = [
    {
      id: 44,
      title: "xxxxxxx",
    },
    // … 如需更多示例可继续添加
  ];
}

/**
 * 点击 “新建文章” 按钮
 */
function openCreateDialog() {
  newArticle.title = "";
  createDialogVisible.value = true;
}

/**
 * 确认创建文章
 */
function confirmCreate() {
  createFormRef.value.validate((valid) => {
    if (valid) {
      // 实际项目中，此处调用后端接口创建文章
      console.log("创建文章：", newArticle.title);
      ElMessage.success("文章创建成功");
      createDialogVisible.value = false;
      fetchArticles();
    }
  });
}

/**
 * 点击 “编辑文章” 按钮
 */
function openEditDialog(row) {
  editingArticle.id = row.id;
  editingArticle.title = row.title;
  editDialogVisible.value = true;
}

/**
 * 确认保存编辑
 */
function confirmEdit() {
  editFormRef.value.validate((valid) => {
    if (valid) {
      // 实际项目中，此处调用后端接口保存编辑
      console.log("编辑文章：", editingArticle.id, editingArticle.title);
      ElMessage.success("文章保存成功");
      editDialogVisible.value = false;
      fetchArticles();
    }
  });
}

/**
 * 点击 “导入” 按钮
 */
function importArticle(row) {
  console.log("导入文章：", row.id);
  // TODO: 实际项目中可触发文件上传或其他导入逻辑
}

/**
 * 选中行变化
 */
function onSelectionChange(val) {
  selectedArticles.value = val;
}

/**
 * 点击 “删除” 按钮
 */
function openDeleteDialog() {
  if (selectedArticles.value.length === 0) {
    ElMessage.warning("请先选择要删除的文章");
    return;
  }
  deleteDialogVisible.value = true;
}

/**
 * 确认删除文章
 */
function confirmDelete() {
  // 实际项目中，此处调用后端接口批量删除文章
  console.log(
      "删除文章：",
      selectedArticles.value.map((row) => row.id)
  );
  ElMessage.success("删除成功");
  deleteDialogVisible.value = false;
  selectedArticles.value = [];
  fetchArticles();
}

/**
 * 分页页码变化
 */
function onPageChange(page) {
  currentPage.value = page;
  fetchArticles();
}

/**
 * 分页每页条数变化
 */
function onSizeChange(size) {
  pageSize.value = size;
  fetchArticles();
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
  display: flex;
  gap: 10px;
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
