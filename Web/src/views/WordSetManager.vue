<template>
  <el-container class="wordset-manager-container">
    <!-- 头部 -->
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
      <!-- 侧边栏 -->
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
            default-active="1"
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

      <!-- 主内容区 -->
      <el-main class="main-content">
        <div class="top-bar">
          <el-button type="primary" @click="handleCreate">新建词书</el-button>
          <el-badge
              :value="selectedWordSets.length"
              class="badge-wrapper"
              v-if="selectedWordSets.length > 0"
          >
            <el-button type="danger" @click="handleDelete">删除</el-button>
          </el-badge>
          <el-button type="danger" @click="handleDelete" v-else>删除</el-button>
        </div>

        <!-- 词书列表表格 -->
        <el-table
            :data="wordSets"
            style="width: 100%;"
            @selection-change="handleSelectionChange"
            border
        >
          <el-table-column type="selection" width="55" />

          <el-table-column
              prop="id"
              label="ID"
              width="100"
              align="center"
          />
          <el-table-column
              prop="name"
              label="词书名称"
              align="center"
          />
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

        <!-- 分页及底部信息 -->
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

        <!-- 新建词书对话框 -->
        <el-dialog
            title="创建词书"
            :visible.sync="createDialogVisible"
            width="400px"
            :before-close="() => (createDialogVisible = false)"
        >
          <el-form
              ref="formRef"
              :model="newWordSet"
              :rules="rules"
              label-width="0px"
          >
            <el-form-item prop="name">
              <el-input v-model="newWordSet.name" placeholder="请输入词书名称" />
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
 * 词书列表数据 (示例数据)
 * 在真实项目中，你会从接口拉取数据并赋值给 wordSets。
 */
const wordSets = ref([]); // 每一项形如 { id: 44, name: "xxxxxxx" }
const total = ref(0); // 总数据量
const pageSize = ref(10); // 每页数量
const currentPage = ref(1); // 当前页码

// 表格选中项
const selectedWordSets = ref([]);

// “新建词书”对话框开关 & 表单数据
const createDialogVisible = ref(false);
const newWordSet = reactive({
  name: "",
});
const rules = {
  name: [{ required: true, message: "请输入词书名称", trigger: "blur" }],
};
const formRef = ref(null);

// “删除”对话框开关
const deleteDialogVisible = ref(false);

/**
 * 模拟拉取词书列表
 * 在真实项目中，这里应调用后端 API，然后更新 wordSets、total。
 */
function fetchWordSets() {
  // 示例：假设总共有 101 条
  total.value = 101;

  // 根据 currentPage 和 pageSize，从后端拉取对应页数据
  // 这里只做简单模拟，只给出一条记录
  wordSets.value = [
    {
      id: 44,
      name: "xxxxxxx",
    },
    // ... 如果需要，可在此添加更多示例行
  ];
}

/**
 * 点击 “新建词书” 按钮
 */
function handleCreate() {
  newWordSet.name = "";
  createDialogVisible.value = true;
}

/**
 * 在 “创建词书” 对话框中点击 “确认创建”
 */
function confirmCreate() {
  formRef.value.validate((valid) => {
    if (valid) {
      // 真实项目中，此处调用后端接口创建词书
      console.log("创建词书：", newWordSet.name);
      ElMessage.success("词书创建成功");
      createDialogVisible.value = false;
      // 重新拉取列表
      fetchWordSets();
    }
  });
}

/**
 * 选中行变化时触发
 */
function handleSelectionChange(val) {
  selectedWordSets.value = val;
}

/**
 * 点击 “删除” 按钮
 */
function handleDelete() {
  if (selectedWordSets.value.length === 0) {
    ElMessage.warning("请先选择要删除的词书");
    return;
  }
  deleteDialogVisible.value = true;
}

/**
 * 在 “删除确认” 对话框中点击 “确认删除”
 */
function confirmDelete() {
  // 真实项目中，此处调用后端接口批量删除词书
  console.log("删除词书：", selectedWordSets.value.map((row) => row.id));
  ElMessage.success("删除成功");
  deleteDialogVisible.value = false;
  // 清空已选
  selectedWordSets.value = [];
  // 重新拉取列表
  fetchWordSets();
}

/**
 * 点击 “导入” 按钮
 */
function handleImport(row) {
  console.log("导入词书：", row.id);
  // TODO: 触发文件上传等逻辑
}

/**
 * 点击 “管理单词” 按钮
 */
function handleManageWords(row) {
  console.log("管理单词：", row.id);
  // TODO: 跳转到该词书的单词管理页面，比如：
  // router.push({ name: "WordManager", query: { wordsetId: row.id } });
}

/**
 * 分页页码变化回调
 */
function handlePageChange(page) {
  currentPage.value = page;
  fetchWordSets();
}

/**
 * 分页每页数量变化回调
 */
function handleSizeChange(size) {
  pageSize.value = size;
  fetchWordSets();
}

onMounted(() => {
  fetchWordSets();
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
