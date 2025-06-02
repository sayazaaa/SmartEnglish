<template>
  <div class="login-container">
    <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        label-position="top"
        class="login-form"
    >
      <h2 class="title">登录</h2>

      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            autocomplete="off"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            autocomplete="off"
        />
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            @click="onSubmit"
            class="login-button"
            :loading="loading"
        >
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const loginFormRef = ref(null)

const loginForm = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const onSubmit = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true

      setTimeout(() => {
        loading.value = false
        ElMessage.success(`欢迎，${loginForm.username}！`)
        // TODO: 在此处添加实际的登录跳转逻辑
      }, 1500)
    } else {

      return false
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 400px;
  margin: 100px auto;
  padding: 30px;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
}

.login-button {
  width: 100%;
}
</style>
