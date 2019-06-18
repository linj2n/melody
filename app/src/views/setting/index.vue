<template>
  <div class="card-container">
    <el-row>
      <el-card>
        <el-tabs v-model="activeName">
          <el-tab-pane label="站点配置" name="SiteSetting">
            <el-form
              label-position="left"
              label-width="80px"
              class="option-form"
            >
              <el-form-item label="标 题">
                <el-input v-model="options.siteTitle" />
              </el-form-item>
              <el-form-item label="副标题">
                <el-input v-model="options.siteSubtitle" />
              </el-form-item>
              <el-form-item label="描述">
                <el-input v-model="options.siteDescription" type="textarea" />
              </el-form-item>
              <el-form-item size="large">
                <el-button 
type="primary" 
@click="updateOptions"
                >保存</el-button
                >
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="社交资料" name="SocialSetting">
            <el-form
              label-position="left"
              label-width="80px"
              class="option-form"
            >
              <el-form-item label="作者名称">
                <el-input v-model="options.siteAuthorName" />
              </el-form-item>
              <el-form-item label="头像">
                <el-input v-model="options.siteAuthorAvatar" />
              </el-form-item>
              <el-form-item label="Github">
                <el-input v-model="options.social_github" />
              </el-form-item>
              <el-form-item label="Email地址">
                <el-input v-model="options.social_email" />
              </el-form-item>
              <el-form-item label="微博地址">
                <el-input v-model="options.social_weibo" />
              </el-form-item>
              <el-form-item size="large">
                <el-button 
type="primary" 
@click="updateOptions"
                >保存</el-button
                >
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import { fetchAllOptions, updateOptions } from '@/api/setting'

export default {
  name: 'Setting',
  data() {
    return {
      activeName: 'SiteSetting',
      options: null
    }
  },
  created() {
    this.fetchAllOptions()
  },
  methods: {
    fetchAllOptions() {
      fetchAllOptions().then(response => {
        this.options = response.data
      })
    },
    updateOptions() {
      updateOptions(Object.assign({}, this.options)).then(response => {
        console.log('success')
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
