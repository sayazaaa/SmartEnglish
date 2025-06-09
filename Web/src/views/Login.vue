<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <h2 class="title">管理员登录</h2>
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          label-position="top"
          class="login-form"
      >
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="el-icon-user"
              :disabled="loading"
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              placeholder="请输入密码"
              show-password
              prefix-icon="el-icon-lock"
              :disabled="loading"
          />
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
              type="primary"
              class="login-button"
              :loading="loading"
              @click="handleSubmit"
              style="width: 100%;"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { ElMessage } from "element-plus";

// 1. 用于绑定表单数据
const loginForm = reactive({
  username: "",
  password: "",
});

// 2. 表单校验规则
const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" }
  ],
};

// 3. 引用表单实例
const loginFormRef = ref(null);

// 4. loading 状态
const loading = ref(false);

// 5. 路由实例，用于登录成功后跳转
const router = useRouter();

// 6. 点击“登录”时，校验并调用后端登录接口
async function handleSubmit() {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return;

    loading.value = true;
    try {
      // 这里的 URL "/account/admin" 要和你的后端接口路径保持一致
      const response = await axios.post(
          "/account/admin",
          {
            username: loginForm.username,
            password: loginForm.password,
          },
          {
            // 让 4xx 不被 axios 自动抛错，好拿返回的错误信息
            validateStatus: (status) => status < 500,
          }
      );

      if (response.status === 200) {
        // 优先从响应头取 token
        let token = "";
        if (response.headers && response.headers.getAuthorization()) {
          token = response.headers.getAuthorization();
        } else if (response.data && response.data.token) {
          token = response.data.token;
        }

        if (!token) {
          ElMessage.error("登录失败：未收到 token");
          loading.value = false;
          return;
        }

        // 把 token 存 localStorage
        localStorage.setItem("admin_token", token);

        ElMessage.success("登录成功，跳转中…");
        router.push({ path: "/wordsetmanager" });
      } else {
        // 后端 4xx/5xx 错误时，显示 message
        const msg =
            (response.data && response.data.message) ||
            `登录失败 (状态码 ${response.status})`;
        ElMessage.error(msg);
      }
    } catch (err) {
      console.error("登录异常：", err);
      ElMessage.error("网络异常，请稍后重试");
    } finally {
      loading.value = false;
    }
  });
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background-color: #faf6f2;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-card {
  width: 360px;
  padding: 30px;
  border-radius: 8px;
}
.title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}
.login-form {
  width: 100%;
}
.login-button {
  margin-top: 10px;
}
</style>
