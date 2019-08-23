<template>
  <div class="card-container">
    <el-row>
      <el-card>
        <el-tabs v-model="activePane">
          <el-tab-pane label="所有标签" name="tags">
            <Item
              v-for="tag in allTags"
              :key="tag.id"
              :item="tag"
              @listPosts="listPostByItemId"
              @handleEdit="handleEdit"
            />
            <el-input
              v-if="newItemInputVisible"
              ref="newItemInput"
              class="input-new-tag"
              size="small"
              @keyup.enter.native="handleNewInputConfirm"
              @blur="handleNewInputConfirm"
            />
            <el-button
              v-else
              class="button-new-tag"
              size="small"
              @click="showNewItemInput"
            >+ 添加标签
            </el-button>
          </el-tab-pane>
          <el-tab-pane label="所有分类" name="categories">
            <Item
              v-for="category in allCategories"
              :key="category.id"
              :item="category"
              @listPosts="listPostByItemId"
              @handleEdit="handleEdit"
            />
            <el-input
              v-if="newItemInputVisible"
              ref="newItemInput"
              class="input-new-tag"
              size="small"
              @keyup.enter.native="handleNewInputConfirm"
              @blur="handleNewInputConfirm"
            />
            <el-button
              v-else
              class="button-new-tag"
              size="small"
              @click="showNewItemInput"
            >+ 添加分类
            </el-button>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
    <el-dialog
      :title="typeTextMap[activePane]"
      :visible.sync="editDialogVisible"
    >
      <el-form :model="currentItem" label-position="left" label-width="50px">
        <el-form-item label="名称">
          <el-input v-model="currentItem.name" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-popover v-model="deletePopoverVisible" placement="top" width="160">
          <p>确定删除吗？</p>
          <div style="text-align: right; margin: 0">
            <el-button
              size="mini"
              type="text"
              @click="deletePopoverVisible = false"
            >
              取消
            </el-button>
            <el-button
              type="primary"
              size="mini"
              @click="handleDeleteConfirm(currentItem)"
            >
              确定
            </el-button>
          </div>
          <el-button slot="reference" type="danger">删 除</el-button>
        </el-popover>
        <el-button style="margin-left: 10px" @click="editDialogVisible = false">
          取 消
        </el-button>
        <el-button type="primary" @click="handleEditConfirm(currentItem)">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Item from './components/Item'
import {
  listAllCategories,
  listAllTags,
  updateTagOrCategory,
  removeTagOrCategory,
  createNewTagOrCategory
} from '@/api/post'
export default {
  components: { Item },
  data() {
    return {
      allTags: [],
      allCategories: [],
      editDialogVisible: false,
      deletePopoverVisible: false,
      newItemInputVisible: false,
      activePane: 'tags',
      typeTextMap: {
        tags: '标签',
        categories: '分类'
      },
      currentItem: {
        id: null,
        name: ''
      }
    }
  },
  created() {
    this.initTagsAndCategories()
  },
  methods: {
    getCurrItemList() {
      return this.activePane === 'tags' ? this.allTags : this.allCategories
    },
    showNewItemInput() {
      this.newItemInputVisible = true
      this.$nextTick(_ => {
        this.$refs.newItemInput.$refs.input.focus()
      })
    },
    handleNewInputConfirm(e) {
      const itemName = e.target.value.trim()
      const currItemList = this.getCurrItemList()
      if (itemName) {
        const exited = currItemList.find(item => item.name === itemName)
        if (exited) {
          this.$message({
            message: this.typeTextMap[this.activePane] + itemName + ' 已存在，添加失败',
            type: 'warning'
          })
          return
        } else {
          const newItem = {}
          newItem['id'] = null
          newItem['name'] = itemName
          createNewTagOrCategory(this.activePane, newItem).then(response => {
            currItemList.push(response.data)
            this.$message({
              message: '添加成功',
              type: 'success'
            })
          })
        }
      }
      this.newItemInputVisible = false
      e.target.value = ''
    },
    handleEdit(item) {
      this.currentItem = Object.assign({}, item)
      this.editDialogVisible = true
    },
    initTagsAndCategories() {
      listAllTags().then(response => {
        this.allTags = response.data
      })
      listAllCategories().then(response => {
        this.allCategories = response.data
      })
    },
    listPostByItemId(id) {
      const key = this.activePane === 'tags' ? 'tagId' : 'categoryId'
      const queryValue = {}
      queryValue[key] = id
      this.$router.push({ path: '/posts', query: queryValue })
    },
    handleEditConfirm(newItem) {
      const itemList = this.getCurrItemList()
      updateTagOrCategory(this.activePane, newItem).then(response => {
        const item = response.data
        const indexOfItem = itemList.findIndex(elem => elem.id === item.id)
        itemList.splice(indexOfItem, 1, item)
        this.$message({
          message: response.message,
          type: 'success'
        })
        this.editDialogVisible = false
      })
    },
    handleDeleteConfirm(item) {
      const itemList = this.getCurrItemList()
      removeTagOrCategory(this.activePane, Object.assign({}, item)).then(
        response => {
          const indexOfItem = itemList.findIndex(e => e.id === item.id)
          itemList.splice(indexOfItem, 1)
          this.$message({
            message: response.message,
            type: 'success'
          })
        }
      )
      this.deletePopoverVisible = false
      this.editDialogVisible = false
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
</style>
