<template>
  <el-container class="word-manager-container">
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
            default-active="3"
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
          <el-badge
              :value="selectedWords.length"
              class="badge-wrapper"
              v-if="selectedWords.length > 0"
          >
            <el-button type="danger" @click="handleDelete">删除</el-button>
          </el-badge>
          <el-button type="danger" @click="handleDelete" v-else>删除</el-button>
        </div>

        <!-- 单词列表表格 -->
        <el-table
            :data="words"
            style="width: 100%;"
            border
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="100" align="center" />
          <el-table-column prop="text" label="单词" align="center" />
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
              @current-change="handlePageChange"
              @size-change="handleSizeChange"
          />
        </div>

        <!-- 删除确认对话框 -->
        <el-dialog
            title="提示"
            :visible.sync="deleteDialogVisible"
            width="350px"
            :before-close="() => (deleteDialogVisible = false)"
        >
          <span>确定要删除所选单词吗？</span>
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
 * 单词列表示例数据
 * 实际项目中，请从后端 API 获取并赋值给 words
 */
const words = ref([]); // 每项形如 { id: 44, text: "xxxxxxx" }
const total = ref(0); // 总数据量
const pageSize = ref(10); // 每页数量
const currentPage = ref(1); // 当前页码

// 选中行
const selectedWords = ref([]);

// “删除”对话框
const deleteDialogVisible = ref(false);

/**
 * 模拟拉取单词列表
 * 实际项目中，这里应调用后端接口，然后更新 words & total
 */
function fetchWords() {
  // 示例：假设总共 101 项
  total.value = 101;

  // 根据 currentPage 和 pageSize，从后端拉取对应页数据
  // 这里只做简单模拟，只给出一条记录
  words.value = [
    {
      id: 44,
      text: "xxxxxxx",
    },
    // … 如需更多示例可在此添加
  ];
}

/**
 * 选中行变化时触发
 */
function handleSelectionChange(val) {
  selectedWords.value = val;
}

/**
 * 点击 “删除” 按钮
 */
function handleDelete() {
  if (selectedWords.value.length === 0) {
    ElMessage.warning("请先选择要删除的单词");
    return;
  }
  deleteDialogVisible.value = true;
}

/**
 * 在 “删除确认” 对话框中点击 “确认删除”
 */
function confirmDelete() {
  // 实际项目中，此处调用后端接口批量删除单词
  console.log("删除单词：", selectedWords.value.map((row) => row.id));
  ElMessage.success("删除成功");
  deleteDialogVisible.value = false;
  // 清空选中
  selectedWords.value = [];
  // 重新拉取列表
  fetchWords();
}

/**
 * 分页页码变化回调
 */
function handlePageChange(page) {
  currentPage.value = page;
  fetchWords();
}

/**
 * 分页每页数量变化回调
 */
function handleSizeChange(size) {
  pageSize.value = size;
  fetchWords();
}

onMounted(() => {
  fetchWords();
});
</script>

<style scoped>
.word-manager-container {
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

.top-bar .badge-wrapper {
  margin-left: 10px;
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
