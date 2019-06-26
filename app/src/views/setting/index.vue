<template>
  <div class="card-container">
    <el-row>
      <el-card>
        <el-tabs v-model="activeName">
          <el-tab-pane label="站点配置" name="SiteSetting">
            <el-form
              v-model="siteSetting"
              label-position="left"
              label-width="80px"
              class="option-form"
            >
              <el-form-item label="标 题">
                <el-input v-model="siteSetting.siteTitle" />
              </el-form-item>
              <el-form-item label="副标题">
                <el-input v-model="siteSetting.siteSubtitle" />
              </el-form-item>
              <el-form-item label="描述">
                <el-input
                  v-model="siteSetting.siteDescription"
                  type="textarea"
                />
              </el-form-item>
              <el-form-item size="large">
                <el-button type="primary" @click="updateOptions(siteSetting)">
                  保存
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="社交资料" name="SocialSetting">
            <el-form
              v-model="socialSetting"
              label-position="left"
              label-width="100px"
              class="option-form"
            >
              <el-form-item label="作者名称">
                <el-input v-model="socialSetting.siteAuthorName" />
              </el-form-item>
              <el-form-item label="头 像">
                <el-input v-model="socialSetting.siteAuthorAvatar" />
              </el-form-item>
              <el-form-item label="Email">
                <el-input v-model="socialSetting.social_email" />
              </el-form-item>
              <el-form-item label="Github">
                <el-input v-model="socialSetting.social_github" />
              </el-form-item>
              <el-form-item label="StackOverFlow">
                <el-input v-model="socialSetting.social_stackOverflow" />
              </el-form-item>
              <el-form-item label="微 博">
                <el-input v-model="socialSetting.social_weibo" />
              </el-form-item>
              <el-form-item size="large">
                <el-button type="primary" @click="updateOptions(socialSetting)">
                  保存
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import { updateOptions, fetchOptions } from '@/api/setting'

export default {
  name: 'Setting',
  data() {
    return {
      activeName: 'SiteSetting',
      siteSetting: {
        siteTitle: null,
        siteSubtitle: null,
        siteDescription: null
      },
      socialSetting: {
        siteAuthorName: null,
        siteAuthorAvatar: null,
        social_github: null,
        social_email: null,
        social_weibo: null,
        social_stackOverflow: null
      }
    }
  },
  created() {
    this.fetchAllSettingOptions()
  },
  methods: {
    fetchAllSettingOptions() {
      fetchOptions(Object.keys(this.siteSetting)).then(response => {
        this.siteSetting = response.data
      })
      fetchOptions(Object.keys(this.socialSetting)).then(response => {
        this.socialSetting = response.data
      })
    },
    updateOptions(options) {
      updateOptions(Object.assign({}, options)).then(response => {
        this.$message({
          message: response.message,
          type: 'success'
        })
      })
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
</style>
