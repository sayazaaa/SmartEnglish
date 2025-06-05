<template>
  <el-container class="feedback-manager-container">
    <el-container>
      <!-- 右侧主内容 -->
      <el-main class="main-content">
        <!-- 顶部操作栏 -->
        <div class="top-bar">
          <el-button type="danger" @click="openDeleteDialog">
            删除
          </el-button>
        </div>

        <!-- 反馈列表表格 -->
        <el-table
            :data="feedbackList"
            style="width: 100%;"
            border
            @selection-change="onSelectionChange"
        >
          <el-table-column type="selection" width="55" />

          <el-table-column prop="id" label="ID" width="100" align="center" />
          <el-table-column prop="sourceUser" label="来源用户" align="center" />

          <el-table-column label="操作" align="center" width="200">
            <template #default="scope">
              <el-button
                  type="text"
                  size="small"
                  @click="openViewDialog(scope.row)"
              >
                查看具体内容
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

        <!-- 查看内容对话框 -->
        <el-dialog
            title="反馈内容"
            :visible.sync="viewDialogVisible"
            width="500px"
            :before-close="() => (viewDialogVisible = false)"
        >
          <div class="dialog-body">
            <p><strong>ID：</strong>{{ currentFeedback.id }}</p>
            <p><strong>来源用户：</strong>{{ currentFeedback.sourceUser }}</p>
            <p><strong>内容：</strong></p>
            <div class="feedback-content">
              {{ currentFeedback.content }}
            </div>
          </div>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="viewDialogVisible = false">
              关闭
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
          <span>确定要删除所选反馈吗？</span>
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
import { ref, reactive, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { ElMessage } from "element-plus";

/**
 * 通过 useRoute 获取当前路由，用于侧边栏高亮
 */
const route = useRoute();
const activeMenu = ref(route.path);
watch(
    () => route.path,
    (newPath) => {
      activeMenu.value = newPath;
    }
);

/**
 * 反馈列表示例数据
 * todo:调用后端接口获取并赋值
 */
const feedbackList = ref([]); // 每项形如 { id: 44, sourceUser: "xxxxxxx", content: "用户反馈具体内容……" }
const total = ref(0); // 总数据量
const pageSize = ref(10); // 每页数量
const currentPage = ref(1); // 当前页码

// 选中行
const selectedFeedbacks = ref([]);

// “查看内容”对话框
const viewDialogVisible = ref(false);
const currentFeedback = reactive({
  id: null,
  sourceUser: "",
  content: "",
});

// “删除”对话框
const deleteDialogVisible = ref(false);

/**
 * 模拟拉取反馈列表
 * todo:后端 API，然后更新 feedbackList、total
 */
function fetchFeedback() {
  // 示例：假设总共 101 条反馈
  total.value = 101;

  // 根据 currentPage 和 pageSize，从后端拉取对应页数据
  // 这里只做简单模拟，只给出几条记录
  feedbackList.value = [
    {
      id: 44,
      sourceUser: "用户A",
      content:
          "这是用户 A 提交的反馈内容示例，用于演示在弹窗中查看具体文本。",
    },
    {
      id: 45,
      sourceUser: "用户B",
      content: "这是用户 B 的另一条反馈，包含较长的文字内容。",
    },
    // …如需更多示例，可继续添加
  ];
}

/**
 * 选中行变化时触发
 */
function onSelectionChange(val) {
  selectedFeedbacks.value = val;
}

/**
 * 点击“查看具体内容”
 */
function openViewDialog(row) {
  currentFeedback.id = row.id;
  currentFeedback.sourceUser = row.sourceUser;
  currentFeedback.content = row.content;
  viewDialogVisible.value = true;
}

/**
 * 点击“删除”按钮
 */
function openDeleteDialog() {
  if (selectedFeedbacks.value.length === 0) {
    ElMessage.warning("请先选择要删除的反馈");
    return;
  }
  deleteDialogVisible.value = true;
}

/**
 * 确认删除反馈
 */
function confirmDelete() {
  // 实际项目中，此处调用后端接口批量删除反馈
  console.log(
      "删除反馈：",
      selectedFeedbacks.value.map((row) => row.id)
  );
  ElMessage.success("删除成功");
  deleteDialogVisible.value = false;
  selectedFeedbacks.value = [];
  fetchFeedback();
}

/**
 * 分页页码变化
 */
function onPageChange(page) {
  currentPage.value = page;
  fetchFeedback();
}

/**
 * 分页每页数量变化
 */
function onSizeChange(size) {
  pageSize.value = size;
  fetchFeedback();
}

onMounted(() => {
  fetchFeedback();
});
</script>

<style scoped>
.feedback-manager-container {
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

.dialog-body p {
  margin: 8px 0;
}

.feedback-content {
  white-space: pre-wrap;
  background-color: #fafafa;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-top: 6px;
}

.dialog-footer {
  text-align: right;
}

.el-dialog__body {
  padding: 20px 30px;
}
</style>
