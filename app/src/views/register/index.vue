<template>
  <div class="register-container">
    <el-form
      ref="registerForm"
      :model="registerForm"
      :rules="registerRules"
      class="register-form"
      auto-complete="on"
      label-position="left"
    >
      <h3 class="title">Register</h3>
      <el-form-item prop="login">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          v-model="registerForm.login"
          name="login"
          type="text"
          auto-complete="on"
          placeholder="username"
        />
      </el-form-item>
      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="icon_cmail"/>
        </span>
        <el-input
          v-model="registerForm.email"
          name="email"
          type="text"
          auto-complete="on"
          placeholder="example@xxx.com"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :type="pwdType"
          v-model="registerForm.password"
          name="password"
          auto-complete="on"
          placeholder="password"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="pwdType === 'password' ? 'eye' : 'eye-open'"/>
        </span>
      </el-form-item>
      <el-form-item>
        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >Sign up</el-button>
      </el-form-item>
      <div class="tips">
        <span>
          已有账号？
          <a href="/login">登录</a>
        </span>
      </div>
    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import { register, checkUsernameOrEmailExistence } from '@/api/register'

export default {
  name: 'Register',
  data() {
    const validateUsername = (rule, login, callback) => {
      if (!validUsername(login)) {
        callback(new Error('请输入正确的用户名,不小于5位的数字或字母'))
      } else {
        checkUsernameOrEmailExistence(login, undefined)
          .then(response => {
            if (response.data.existed) {
              callback(new Error('用户名已被使用过'))
            } else {
              callback()
            }
          })
          .catch(error => {
            // console.log(error.response)
            console.log(error)
          })
      }
    }
    const validateEmail = (rule, email, callback) => {
      const pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
      if (email.length === 0) {
        callback(new Error('邮箱不为空'))
      } else if (!pattern.test(email)) {
        callback(new Error('邮箱格式不正确'))
      } else {
        checkUsernameOrEmailExistence(undefined, email)
          .then(response => {
            if (response.data.existed) {
              callback(new Error('邮箱已被使用过'))
            } else {
              callback()
            }
          })
          .catch(function(error) {
            // handle error
            console.log(error)
            console.log(error.message)
            callback(new Error('服务器出错'))
          })
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码不能小于5位'))
      } else {
        callback()
      }
    }
    return {
      // 表单项数据
      registerForm: {
        login: '',
        email: '',
        password: ''
      },
      // 表单校验规则
      registerRules: {
        login: [
          {
            required: true,
            trigger: 'blur',
            validator: validateUsername
          }
        ],
        password: [
          {
            required: true,
            trigger: 'blur',
            validator: validatePass
          }
        ],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }]
      },
      loading: false,
      pwdType: 'password',
      redirect: undefined
    }
  },
  watch: {
    // 监听 route 对象
    $route: {
      handler: function(route) {
        // 将查询参数绑定到 this.redirect
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          var vm = this
          register(this.registerForm)
            .then(function(response) {
              vm.loading = false
              vm.$message({
                message:
                  '激活邮件已发送到您的邮箱中，请激活后登陆。正在跳转登录页面',
                type: 'success',
                onClose: function() {
                  vm.$router.push({ path: '/login' })
                }
              })
            })
            .catch(error => {
              console.log(error)
              vm.loading = false
              vm.$message({
                message: 'dfasdfasdfasdffasdfasdfa',
                type: 'error'
              })
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
$bg: #2d3a4b;
$light_gray: #eee;

/* reset element-ui css */
.register-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
.register-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  .register-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 15px 35px;
    margin: 80px auto;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 20px;
    a:focus,
    a:hover {
      color: #409eff;
    }
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: $light_gray;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
