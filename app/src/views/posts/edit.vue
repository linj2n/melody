<template>
  <div class="app-container">
    <el-form :model="postInfo" >
      <el-form-item prop="title">
        <md-input v-model="postInfo.title" name="title" placeholder="请输入标题" style="width: 100%;">
          标题
        </md-input>
      </el-form-item>
      <el-form-item>
      </el-form-item>
      <div class="editor-container" >
        <markdown-editor v-model="postInfo.content" :options="{hideModeSwitch:true,previewStyle:'tab',toolbarItems: [
        'heading',
        'bold',
        'italic',
        'strike',
        'divider',
        'hr',
        'quote',
        'divider',
        'ul',
        'ol',
        'task',
        'indent',
        'outdent',
        'divider',
        'table',
        'image',
        'link',
        'divider',
        'code',
        'codeblock'
    ]}" height="580px"/>
      </div>
    </el-form>
    <div class="postSettingButton" @click="settingDialogVisible = true">  
          <i class="el-icon-setting" />
    </div>
    
    <el-dialog :visible.sync="settingDialogVisible" width="70%" title="设置">
      <el-form label-position="left" label-width="50px" style="width: 100%; margin-left:20px;">
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
        <el-form-item label="状态">
          <el-radio-group v-model="postInfo.status" size="small">
            <el-radio-button label="PUBLISHED">发布</el-radio-button>
            <el-radio-button label="DRAFT">草稿</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="概述">
          <el-input type="textarea" :rows="5" v-model="postInfo.summary" class="setting-item" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" icon="el-icon-delete"  >
          删除
        </el-button>
        <el-button type="info" icon="el-icon-view" @click="settingDialogVisible = true">
          预览
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="updatePost">
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
import MdInput from '@/components/MDinput'


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
  components: { 
    MarkdownEditor,     
    MdInput
  },
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
.postSettingButton {
  position: fixed;
  right: 0;
  top: 25%;
  border-radius: 6px 0 0 6px !important;
  width: 48px;
  height: 48px;
  pointer-events: auto;
  z-index: 10;
  cursor: pointer;
  pointer-events: auto;
  font-size: 24px;
  text-align: center;
  background-color:#1890ff;
  color: #fff;
  line-height: 48px;
}
.postSettingButton:hover {
    background: #66b1ff;
    border-color: #66b1ff;
    color: #fff;
}
.setting-item {
  width: 100%;
  /* padding-left: 20px; */
  padding-right: 20px;
}
.te-toolbar-section {
    height: 33px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border-bottom: 1px solid #e5e5e5;
    /* border-top: 1px solid #e5e5e5;
    border-right: 1px solid #e5e5e5; */
  
}
.tui-editor-defaultUI .te-markdown-tab-section {
    float: left;
    height: 31px;
    background: #fff;
    border-bottom: 1px solid #e5e5e5;
}
.tui-editor-defaultUI {
  /* border-top: 1px solid #e5e5e5; */
  border-right: 1px solid #e5e5e5;
}

.te-markdown-tab-section .te-tab {
  margin: 0px;
  height: 31px;
}
.tui-editor-defaultUI .te-tab button {
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    line-height: 100%;
    height: 31px;
    position: relative;
    cursor: pointer;
    z-index: 1;
    font-size: 13px;
    font-weight: 600;
    background-color: #f9f9f9;
    border: solid 1px #e5e5e5;
    border-left: 0px;
    border-bottom: 0px;
    border-top: 0;
    padding: 0 9px;
    color: #777;
    border-radius: 0;
    outline: 0;
}
.te-markdown-tab-section .te-tab button.te-tab-active, .te-markdown-tab-section .te-tab button:hover.te-tab-active {
    background-color: #fff;
    color: #1890ff;
    border-bottom: 1px solid #fff;
    z-index: 2;
}
</style>
