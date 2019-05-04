<template>
  <div class="app-container">
    <el-form :model="postInfo" label-position="left" label-width="50px" >
      <el-form-item prop="title" label="标 题">
        <el-input v-model="postInfo.title"  required placeholder="Titile">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="settingDialogVisible = true">
          发布
        </el-button>
      </el-form-item>
      <div class="editor-container" >
        <markdown-editor v-model="postInfo.content" :options="{hideModeSwitch:true,previewStyle:'tab',toolbarItems: []}" height="580px"/>
      </div>
    </el-form>

    <el-dialog :visible.sync="settingDialogVisible" width="80%">
      <el-form label-position="left" label-width="50px" style="width: 100%; margin-left:20px; margin-right:20px;">
        <el-form-item label="分类">
          <el-select v-model="tagOptionsValue" multiple placeholder="请选择文章标签" class="setting-item">
            <el-option v-for="tag in tagOptions" :key="tag.id" :label="tag.name" :value="tag.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="categoryOptionsValue" multiple placeholder="请选择文章分类" class="setting-item">
            <el-option v-for="category in categoryOptions" :key="category.id" :label="category.name" :value="category.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="settingDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePost">
          发布
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fecthPost, updatePost, listAllTags, listAllCategories } from '@/api/post'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import MarkdownEditor from '@/components/MarkdownEditor'
const postInfoForm = {
  id: null,
  title: '',
  status: '',
  summay: null,
  views: null,
  createdAt: '',
  updatedAt: null,
  content: '',
  tags: null,
  categories: null,
  url: null
}
export default {
  name: 'PosetEdit',
  components: { MarkdownEditor },
  data() {
    return {
      fecthPostLoading: true,
      settingDialogVisible: false,
      postInfo: Object.assign({}, postInfoForm),
      categoryOptions: null,
      tagOptions: null
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    this.fecthPostInfo(id)
    this.getCategoryOptions()
    this.getTagOptions()
  },
  computed: {
    tagOptionsValue: {
      get() {
        return this.postInfo.tags && this.postInfo.tags.map(tag => tag.id)
      },
      set(val) {
        const vm = this
        vm.postInfo.tags = val.map(function (tagId) {
          var tag = {}
          tag['id'] = tagId
          tag['name'] = vm.tagOptions.find(tag => tag.id === tagId).name
          return tag
        })
      }
    },
    categoryOptionsValue: {
      get() {
        return this.postInfo.categories && this.postInfo.categories.map(category => category.id)
      },
      set(val) {
        const vm = this
        vm.postInfo.categories = val.map(function (categoryId) {
          var category = {}
          category['id'] = categoryId
          category['name'] = vm.categoryOptions.find(category => category.id === categoryId).name
          return category
        })
      }
    }
  },
  methods: {
    fecthPostInfo(id) {
      this.fecthPostLoading = true
      fecthPost(id).then(response => {
        this.postInfo = Object.assign({}, response.data)
        this.fecthPostLoading = false
      })
    },
    getTagOptions() {
      listAllTags().then(response => {
        this.tagOptions = response.data
      })
    },
    getCategoryOptions() {
      listAllCategories().then(response => {
        this.categoryOptions = response.data
      })
    },
    updatePost() {
      updatePost(this.postInfo).then(response => {
        this.settingDialogVisible = false
        this.$message({
          message: response.message,
          type: 'success'
        });
      })
    }
  }
}
</script>

<style >
.setting-item {
  width: 100%;
  padding-left: 20px;
  padding-right: 20px;
}
</style>
