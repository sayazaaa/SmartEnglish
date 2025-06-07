<template>
  <el-container style="height:100vh">
    <!-- 1. Header：非 /login 时显示 -->
    <el-header v-if="!isLoginRoute" class="header">
      <div class="header-left">
        <span class="logo">管理系统</span>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleLogout">
          <span class="el-dropdown-link">
            <i class="el-icon-user"></i> {{ username }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 2. Aside：非 /login 时显示 -->
      <el-aside v-if="!isLoginRoute" width="200px" class="sidebar">
        <div class="user-info">
          <el-avatar size="large" :src="avatarUrl" />
          <p class="username">{{ username }}</p>
          <p class="status"><i class="el-icon-circle-check"></i> 在线</p>
        </div>
        <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            background-color="#2d3a4b"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            router
        >
          <el-menu-item index="/wordsetmanager">
            <i class="el-icon-reading"></i><span>词书管理</span>
          </el-menu-item>
          <el-menu-item index="/wordmanager">
            <i class="el-icon-document"></i><span>单词管理</span>
          </el-menu-item>
          <el-menu-item index="/articlemanager">
            <i class="el-icon-document"></i><span>阅读材料管理</span>
          </el-menu-item>
          <el-menu-item index="/statistic">
            <i class="el-icon-data-analysis"></i><span>数据分析</span>
          </el-menu-item>
          <el-menu-item index="/feedback">
            <i class="el-icon-message"></i><span>用户反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 3. Main：路由组件都渲染在这里 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();

// 判断当前是不是登录页
const isLoginRoute = computed(() => route.path === '/login');

// 侧边栏高亮项
const activeMenu = ref(route.path);
watch(() => route.path, p => (activeMenu.value = p));

// 示例用户信息
const username = ref('测试用户');
const avatarUrl = ref('https://via.placeholder.com/80');

// 退出登录
function handleLogout(cmd) {
  if (cmd === 'logout') {
    localStorage.removeItem('admin_token');
    ElMessage.success('已退出登录');
    router.push('/login');
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #2d3a4b;
  color: #fff;
  padding: 0 20px;
}
.logo { font-size: 18px; }
.sidebar {
  background: #2d3a4b;
  color: #fff;
}
.user-info {
  text-align: center;
  padding: 20px 0;
  color: #fff;
}
.username { margin: 8px 0 4px; font-size: 14px; }
.status { font-size: 12px; color: #9fa6b7; }
.main-content {
  background: #faf6f2;
  padding: 20px;
  overflow: auto;
}
.el-menu-vertical-demo { border-right: none; }
</style>
