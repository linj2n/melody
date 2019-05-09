<template>
  <div class="card-container">
    <el-row>
        <el-card>
          <el-tabs v-model="activePane" @tab-click="handleClick">
          <el-tab-pane label="所有标签" name="allTagsPane">
            <el-dropdown  
              trigger="click"
              v-for="tag in allTags"
              :key="tag.name"
            >
              <span class="el-tag el-tag--info">
                {{tag.name}}
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="listPostByTagId(tag.id)">
                  <span>
                    <i class="el-icon-view el-icon--left"></i>
                    查看文章
                  </span>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleEdit(tag)">
                  <span>
                    <i class="el-icon-edit el-icon--left"></i>
                    编辑
                  </span>
                </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <el-input
              class="input-new-tag"
              v-if="tagInputVisible"
              v-model="tagInputValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleTagInputConfirm"
              @blur="handleTagInputConfirm"
            >
            </el-input>
            <el-button v-else class="button-new-tag" size="small" @click="showTagInput">
              + 新增标签
            </el-button>
          </el-tab-pane>
          <el-tab-pane label="所有分类" name="allCategoriesPane">
            <el-dropdown  
              trigger="click"
              v-for="category in allCategories"
              :key="category.name"
            >
              <span class="el-tag el-tag--info">
                {{category.name}}
              </span>
              <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <span @click="listPostByCategoryId(category.id)">
                      <i class="el-icon-view el-icon--left"></i>
                      查看文章
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <span>
                      <i class="el-icon-edit el-icon--left"></i>
                      编辑
                    </span>
                  </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            
          </el-tab-pane>
            
          </el-tabs>
        </el-card>
        <el-dialog
          :title="typeTextMap[editDialogType]"
          :visible.sync="editDialogVisible"
          :before-close="handleClose"
          >
          <el-form label-position="left" label-width="50px" :model="item">
            <el-form-item label="名称">
              <el-input v-model="item.name"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button type="danger" icon="el-icon-delete" >
              删 除
            </el-button>
            <el-button @click="editDialogVisible = false">
              取 消
            </el-button>
            <el-button type="primary" @click="handleEditConfirm">
              确 定
            </el-button>
          </span>
        </el-dialog>
    </el-row>
    
  </div>
</template>

<script>
import { listAllCategories, listAllTags, createNewTag, updateTagOrCategory } from '@/api/post'
export default {
  data() {
    return {
      allTags: null,
      allCategories: null,
      activePane: 'allTagsPane',
      tagInputVisible: false,
      tagInputValue: '',
      inputVisible: false,
      inputValue: '',
      editDialogVisible: false,
      editDialogType: '',
      typeTextMap: {
        tags: '标签',
        categories: '分类',
        allTagsPane: 'tags',
        allCategoriesPane: 'categories'
      },
      item: {
        id: null,
        name: ''
      }
    };
  },
  created() {
    this.initTagsAndCategories()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    handleClose(event) {
      console.log(event);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm() {
      let inputValue = this.inputValue
      if (inputValue) {
        this.dynamicTags.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    showTagInput() {
      this.tagInputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleTagInputConfirm() {
      let tagName = this.tagInputValue
      if (tagName) {
        let exited = this.allTags.find(tag => tag.name === tagName)
        if (exited) {
          this.$message({
            message: '标签已存在',
            type: 'danger'
          })
        } else {
          var newTag = {}
          newTag['id'] = null
          newTag['name'] = tagName
          createNewTag(newTag).then(response => {
            this.allTags.push(response.data)
            this.$message({
              message: '添加成功',
              type: 'success'
            })
          })
        }
      }
      this.tagInputVisible = false
      this.tagInputValue = ''
    },
    initTagsAndCategories() {
      listAllTags().then(response => {
        this.allTags = response.data
      })
      listAllCategories().then(response => {
        this.allCategories = response.data
      })
    },
    listPostByTagId(id) {
      this.$router.push({ path: '/posts', query: { tagId: id }})
    },
    listPostByCategoryId(id) {
      this.$router.push({ path: '/posts', query: { categoryId: id }})
    },
    handleEdit(val) {
      this.item = Object.assign({}, val)
      this.editDialogType = this.typeTextMap[this.activePane]
      this.editDialogVisible = true
    },
    handleEditConfirm() {
      const type = this.editDialogType
      let item = Object.assign({}, this.item)
      updateTagOrCategory(type, item).then(response => {
        this.$message({
          message: response.message,
          type: 'success'
        })
      })
    },
    handleDeleteConfirm() {
      
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
.component-item{
  min-height: 100px;
}
.el-tag {
  display: inline-block;
  margin-bottom: 1rem;
  margin-right: 1rem;
}
.el-tag:hover {
    background-color: rgba(64,158,255,.1);
    display: inline-block;
    padding: 0 10px;
    height: 32px;
    line-height: 30px;
    font-size: 12px;
    color: #409eff;
    border-radius: 4px;
    box-sizing: border-box;
    border: 1px solid rgba(64,158,255,.2);
    white-space: nowrap;
    cursor: pointer;
}
.el-popper[x-placement^=bottom] {
    margin-top: -10px;
}
.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.button-new-tag:hover{
  background-color: rgba(103,194,58,.1);
  border-color: rgba(103,194,58,.2);
  color: #67c23a;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
}
</style>

