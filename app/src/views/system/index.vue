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
              <el-col :span="16">
                <el-form
                  label-position="top"
                  label-width="80px"
                  class="option-form"
                >
                  <el-form-item label="用户名">
                    <el-col :span="20" style="padding-left: 0px;">
                      <el-input :disabled="true" v-model="profile.username" />
                    </el-col>
                    <el-col :span="4" style="padding-left: 20px;">
                      <el-button type="text">编辑</el-button>
                    </el-col>
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <el-col :span="20" style="padding-left: 0px">
                      <el-input :disabled="true" v-model="profile.email" />
                    </el-col>
                    <el-col :span="4" style="padding-left: 20px">
                      <el-button type="text">编辑</el-button>
                    </el-col>
                  </el-form-item>
                  <el-form-item label="密码">
                    <el-input v-model="profile.avatarUrl" />
                  </el-form-item>
                  <!-- <el-form-item label="">
                <el-input/>
              </el-form-item> -->
                  <el-form-item size="large">
                    <el-button type="primary">
                      保存
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="博客备份" name="Backup">
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
                      <el-form-item style="margin-bottom: 0px;">
                        <el-input v-model="profile.username" />
                      </el-form-item>
                      <el-form-item style="margin-bottom: 0px;">
                        <el-button type="primary">保存</el-button>
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
                      <el-form-item style="margin-bottom: 0px;">
                        <el-input v-model="profile.email" />
                      </el-form-item>
                      <el-form-item style="margin-bottom: 0px;">
                        <el-button type="primary">保存</el-button>
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
                    <el-form style="margin-left: 10px; margin-right: 20px;">
                      <el-form-item prop="pass">
                        <el-input
                          v-model="profile.pass"
                          type="password"
                          placeholder="输入新密码"
                          autocomplete="off"
                        />
                      </el-form-item>
                      <el-form-item prop="checkPass">
                        <el-input
                          v-model="profile.checkPass"
                          type="password"
                          placeholder="重复输入密码"
                          autocomplete="off"
                        />
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary">提交</el-button>
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
    <el-dialog :visible.sync="verifyDialogVisible" title="身份认证" width="50%">
      <span>为了保护你的帐号安全，请验证身份，验证成功后进行下一步操作</span>
      <span slot="footer" class="dialog-footer">
        <el-form>
          <el-form-item>
            <el-select v-model="profile.email" placeholder="请选择">
              <el-option
                :key="profile.email"
                :label="'使用邮箱 ' + profile.email + ' 获取验证码'"
                :value="profile.email"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="resetKey" /><el-button>获取验证码</el-button>
          </el-form-item>
        </el-form>
        <el-button @click="verifyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="verifyDialogVisible = false">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import CollapseField from '@/components/CollapseField'
import { getInfo, validateResetKey, requestToSendResetKey } from '@/api/user'

export default {
  name: 'System',
  components: { CollapseField },
  data() {
    return {
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
      activeName: 'Backup',
      resetKey: null,
      profile: {},
      profileView: {},
      verifyDialogVisible: false
    }
  },
  created() {
    this.fetchProfile()
  },
  methods: {
    fetchProfile() {
      getInfo().then(response => {
        const { username, email, avatarUrl, password, checkPass } = response.data
        this.profile = { username, email, avatarUrl, password, checkPass }
        this.usernameEditing.description = username
        this.emailEditing.description = email
        this.passwordEditing.description = '已设置'
        this.avatarEditing = avatarUrl
      })
    },
    requestToSendResetKey() {
      requestToSendResetKey(this.profile.email).then(response => {
        this.$message.success(response.message)
      })
    },
    handleUpdateEmail() {

    },
    handleUpdatePassword() {

    },
    handleUpdateUsername() {

    },
    toggleClick(field) {
      let isValid = false
      validateResetKey(this.profile.email, this.profile.resetKey).then(response => {
        isValid = response.data.result
      })
      if (!isValid) {
        this.resetKey = null
        this.verifyDialogVisible = true
        return
      }
      field.isActive = !field.isActive
    }
  }
}
</script>
<style>
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
