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
                  <el-dropdown-item>
                    <span>
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
            <el-input
              class="input-new-tag"
              v-if="inputVisible"
              v-model="inputValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
            >
            </el-input>
            <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
            
          </el-tab-pane>
          <el-tab-pane label="所有分类" name="allCategoriesPane">所有分类</el-tab-pane>
          </el-tabs>
        </el-card>
    </el-row>
  </div>
</template>

<script>
import { listAllCategories, listAllTags } from '@/api/post'
export default {
  data() {
    return {
      allTags: null,
      allCategories: null,
      activePane: 'allTagsPane',
      dynamicTags: ['标签一', '标签二', '标签三'],
      inputVisible: false,
      inputValue: ''
    };
  },
  created() {
    this.initTagsAndCategories()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    handleClose(tag) {
      // this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    initTagsAndCategories() {
      listAllTags().then(response => {
        this.allTags = response.data
      })
      listAllCategories().then(response => {
        this.allCategories = response.data
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

