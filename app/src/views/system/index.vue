<template>
  <div class="card-container">
    <el-row>
      <el-card>
        <el-tabs v-model="activeName">
          <el-tab-pane label="个人资料" name="ProfileSetting">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="profile-avatar-container">
                  <div
                    style="z-index: 1; height: 150px; width: 150px;"
                    class="pan-item"
                  >
                    <img :src="profile.avatarUrl" class="pan-thumb" >
                  </div>
                </div>
              </el-col>
              <el-col :span="16" style="margin-top: 50px; padding-right: 50px;">
                <CollapseField
                  :field="usernameEditing"
                  label="用户名"
                  @toggleClick="toggleClick"
                >
                  <template>
                    <el-form :inline="true" style="margin-left: 10px;">
                      <el-form-item required style="margin-bottom: 0px;">
                        <el-input v-model="profile.username" />
                      </el-form-item>
                      <el-form-item style="margin-bottom: 0px;">
                        <el-button type="primary" @click="handleUpdateUsername">
                          保存
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </template>
                </CollapseField>
                <CollapseField
                  :field="emailEditing"
                  label="邮箱地址"
                  @toggleClick="toggleClick"
                >
                  <template>
                    <el-form :inline="true" style="margin-left: 10px;">
                      <el-form-item prop="email" style="margin-bottom: 0px;">
                        <el-input v-model="profile.email" />
                      </el-form-item>
                      <el-form-item style="margin-bottom: 0px;">
                        <el-button type="primary" @click="handleUpdateEmail">
                          保存
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </template>
                </CollapseField>
                <CollapseField
                  :field="passwordEditing"
                  label="密码"
                  @toggleClick="toggleClick"
                >
                  <template>
                    <el-form
                      ref="passwordForm"
                      :model="passwordForm"
                      :rules="rules"
                      status-icon
                      label-width="100px"
                    >
                      <el-form-item label="密码" prop="password">
                        <el-input
                          v-model="passwordForm.password"
                          type="password"
                          autocomplete="off"
                        />
                      </el-form-item>
                      <el-form-item label="确认密码" prop="checkPassword">
                        <el-input
                          v-model="passwordForm.checkPassword"
                          type="password"
                          autocomplete="off"
                        />
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" @click="handleUpdatePassword">
                          提交
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </template>
                </CollapseField>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
    <el-dialog
      :visible.sync="verifyDialogVisible"
      title="身份认证"
      width="30%"
      class="verifyDialog"
    >
      <span>为了保护你的帐号安全，请验证身份，验证成功后进行下一步操作</span>
      <el-form style="margin-top: 20px;">
        <el-form-item>
          <el-select
            v-model="profile.email"
            placeholder="请选择"
            style="width: 100%;"
          >
            <el-option
              :key="profile.email"
              :label="'使用邮箱 ' + profile.email + ' 获取验证码'"
              :value="profile.email"
            />
          </el-select>
        </el-form-item>
        <el-form-item style="width: 100%;">
          <el-input v-model="verificationCode" style="width: 228px;" />
          <el-button @click="requestToSendVerificationCode">
            获取验证码
          </el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="verifyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="validateVerificationCode">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import CollapseField from '@/components/CollapseField'
import { getInfo, validateVerificationCode, requestToSendVerificationCode, changeEmail, changePassword, changeUsername } from '@/api/user'

export default {
  name: 'System',
  components: { CollapseField },
  data() {
    var validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.passwordForm.checkPassword !== '') {
          this.$refs.passwordForm.validateField('checkPassword')
        }
        callback()
      }
    }
    var validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.passwordForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      passwordForm: {
        password: '',
        checkPassword: ''
      },
      rules: {
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        checkPassword: [
          { validator: validatePassword2, trigger: 'blur' }
        ]
      },
      isActive: false,
      usernameEditing: {
        description: null,
        isActive: false
      },
      passwordEditing: {
        description: null,
        isActive: false
      },
      emailEditing: {
        description: null,
        isActive: false
      },
      avatarEditing: {
        url: null
      },
      currToggleField: {},
      activeName: 'ProfileSetting',
      verificationCode: null,
      profile: {},
      verifyDialogVisible: false
    }
  },
  created() {
    this.fetchProfile()
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    fetchProfile() {
      getInfo().then(response => {
        const { username, email, avatarUrl } = response.data
        this.profile = { username, email, avatarUrl }
        this.usernameEditing.description = username
        this.emailEditing.description = email
        this.passwordEditing.description = '已设置'
        this.avatarEditing = avatarUrl
      })
    },
    requestToSendVerificationCode() {
      requestToSendVerificationCode().then(response => {
        this.$message.success(response.message)
      })
    },
    validateVerificationCode() {
      validateVerificationCode(this.verificationCode).then(response => {
        const isValid = response.data.result
        const message = response.message
        if (!isValid) {
          console.log(message)
          this.$message({
            message: message,
            type: 'error'
          })
        } else {
          this.currToggleField.isActive = true
          this.verifyDialogVisible = false
        }
      })
    },
    handleUpdateEmail() {
      changeEmail(this.profile.email, this.verificationCode).then(response => {
        if (response.code === 20000) {
          this.emailEditing.description = this.profile.email
          this.$message.success(response.message)
        } else if (response.code === 40000) {
          this.profile.email = this.emailEditing.description
          this.verificationCode = null
          this.verifyDialogVisible = true
        }
      })
    },
    handleUpdatePassword() {
      changePassword(this.passwordForm.password, this.verificationCode).then(response => {
        if (response.code === 20000) {
          this.passwordEditing.isActive = false
          this.$message.success(response.message)
          this.$store.dispatch('LogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        } else if (response.code === 40000) {
          this.verificationCode = null
          this.verifyDialogVisible = true
        }
      })
    },
    handleUpdateUsername() {
      changeUsername(this.profile.username, this.verificationCode).then(response => {
        if (response.code === 20000) {
          this.usernameEditing.description = this.profile.username
          this.usernameEditing.isActive = false
          this.$message.success(response.message)
        } else if (response.code === 40000) {
          this.profile.username = this.usernameEditing.description
          this.verificationCode = null
          this.verifyDialogVisible = true
        }
      })
    },
    toggleClick(field) {
      if (field.isActive === true) {
        field.isActive = false
        return
      }
      validateVerificationCode(this.verificationCode).then(response => {
        const isValid = response.data.result
        if (!isValid) {
          this.verificationCode = null
          this.verifyDialogVisible = true
          this.currToggleField = field
        } else {
          field.isActive = !field.isActive
        }
      })
    }
  }
}
</script>
<style>
.verifyDialog .el-dialog__body {
  padding-bottom: 10px;
}
.card-container {
  background-color: #f0f2f5;
  padding: 20px;
  min-height: calc(100vh - 84px);
}
.option-form {
  margin-top: 20px;
  margin-right: 50px;
}
.option-form .el-form-item__label {
  font-weight: 400;
}
.profile-avatar-container {
  position: relative;
  padding-left: 50px;
  padding-bottom: 50px;
  margin: 50px;
}

.pan-item {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  display: inline-block;
  position: relative;
  cursor: default;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.pan-thumb {
  width: 100%;
  height: 100%;
  background-size: 100%;
  border-radius: 50%;
  overflow: hidden;
  position: absolute;
  transform-origin: 95% 40%;
  transition: all 0.3s ease-in-out;
}

.pan-thumb:after {
  content: "";
  width: 8px;
  height: 8px;
  position: absolute;
  border-radius: 50%;
  top: 40%;
  left: 95%;
  margin: -4px 0 0 -4px;
  background: radial-gradient(
    ellipse at center,
    rgba(14, 14, 14, 1) 0%,
    rgba(125, 126, 125, 1) 100%
  );
  box-shadow: 0 0 1px rgba(255, 255, 255, 0.9);
}
</style>
