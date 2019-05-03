<template>
  <div class="app-container">
    <el-form ref="postInfo" :model="postInfo"  class="form-container">
    <div class="editor-menu">
        <el-row>
          <el-col >
            <el-form-item style="margin-bottom: 40px;" prop="title">
              <el-input v-model="postInfo.title" :maxlength="100" name="name" required placeholder="Titile">
              </el-input>
              <el-button type="primary" @click="updatePost">发布</el-button>
            </el-form-item>
            <!-- <div class="postInfo-container">
              <el-row>

              </el-row>
            </div> -->
          </el-col>
          <el-col>
          <el-select v-model="postInfo.tags" multiple style=" width:300px;" placeholder="请选择文章标签" class="filter-item">
            <el-option v-for="tag in tagOptions" :key="tag.id" :label="tag.name" :value="tag.id" />
          </el-select>
          <el-select v-model="postInfo.categories" multiple placeholder="请选择文章分类" class="filter-item" style="width:300px;">
            <el-option v-for="category in categoryOptions" :key="category.id" :label="category.name" :value="category.id"/>
          </el-select>
          </el-col>
        </el-row>
    </div>
    <div class="editor-container">
      <el-form-item>
        <markdown-editor v-model="postInfo.content" :options="{hideModeSwitch:true,previewStyle:'tab',toolbarItems: []}" height="580px"/>
      </el-form-item>
    </div>
    </el-form>
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
      postInfo: Object.assign({}, postInfoForm),
      categoryOptions: null,
      tagOptions: null
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    this.getCategoryOptions()
    this.getTagOptions()
    this.fecthPostInfo(id)
  },
  methods: {
    fecthPostInfo(id) {
      this.fecthPostLoading = true
      fecthPost(id).then(response => {
        console.log(response.data)
        // Update post form
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
      console.log(this.postInfo)
      updatePost(this.postInfo).then(response => {
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

</style>
