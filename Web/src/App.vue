<!-- src/App.vue -->
<template>
  <el-container style="height: 100vh">
    <!-- 只要当前不是 /login，就渲染 Header -->
    <el-header v-if="!isLoginRoute" class="header">
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
            <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 只要当前不是 /login，就渲染 Sidebar -->
      <el-aside
          v-if="!isLoginRoute"
          width="200px"
          class="sidebar"
      >
        <div class="user-info">
          <el-avatar
              size="large"
              src="https://via.placeholder.com/80x80.png?text=头像"
              class="avatar"
          />
          <div class="user-text">
            <p class="username">测试用户</p>
            <p class="status">
              <i class="el-icon-circle-online"></i> 在线
            </p>
          </div>
        </div>
        <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            background-color="#2d3a4b"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            router
        >
          <el-menu-item index="/wordsets">
            <i class="el-icon-reading"></i>
            <span>词书管理</span>
          </el-menu-item>
          <el-menu-item index="/articles">
            <i class="el-icon-folder-opened"></i>
            <span>阅读材料管理</span>
          </el-menu-item>
          <el-menu-item index="/wordsets/1/words">
            <i class="el-icon-document"></i>
            <span>单词管理</span>
          </el-menu-item>
          <el-menu-item index="/statistic">
            <i class="el-icon-data-analysis"></i>
            <span>数据分析</span>
          </el-menu-item>
          <el-menu-item index="/feedback">
            <i class="el-icon-message"></i>
            <span>用户反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区，无论是否登录，都渲染子路由 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const route = useRoute();
const router = useRouter();


const isLoginRoute = computed(() => route.path === "/login");


const activeMenu = ref(route.path);
watch(
    () => route.path,
    (newPath) => {
      activeMenu.value = newPath;
    }
);

/** 退出登录 */
function handleLogout() {
  localStorage.removeItem("admin_token");
  ElMessage.success("已退出登录");
  router.push("/login");
}
</script>

<style scoped>
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
</style>
