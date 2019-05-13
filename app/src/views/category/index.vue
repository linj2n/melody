<template>
  <div>
    <el-row>
      <el-card>
        <el-tabs v-model="activePane">
          <el-tab-pane label="所有标签" name="tags">
            <!-- <ItemTabPane
              :items="allTags"
              @handleItemInputConfirm="handleItemInputConfirm"
              @listPostByItemId="listPostByItemId"
              @handleDeleteConfirm="handleDeleteConfirm"
              @handleEditConfirm="handleEditConfirm"
            />-->
            <el-dropdown
              v-for="item in items"
              :key="item.name"
              :trigger="trigger"
            >
              <span class="el-tag el-tag--info">{{ item.name }}</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="listPostByItemId(item.id)">
                  <span>
                    <i class="el-icon-view el-icon--left" />
                    查看文章
                  </span>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleEdit(item)">
                  <span>
                    <i class="el-icon-edit el-icon--left" />
                    编辑
                  </span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-input
              v-if="itemInputVisible"
              ref="saveTagInput"
              v-model="itemInputValue"
              class="input-new-tag"
              size="small"
              @keyup.enter.native="handleItemInputConfirm"
              @blur="handleItemInputConfirm"
            />
            <el-button
              v-else
              class="button-new-tag"
              size="small"
              @click="showTagInput"
            >+ 新增标签</el-button>
          </el-tab-pane>
          <el-tab-pane label="所有分类" name="categories">
            <!-- <ItemTabPane
              :items="allCategories"
              @handleItemInputConfirm="handleItemInputConfirm"
              @listPostByItemId="listPostByItemId"
              @handleDeleteConfirm="handleDeleteConfirm"
              @handleEditConfirm="handleEditConfirm"
            />-->
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>
    <el-dialog :title="itemType" :visible.sync="editDialogVisible">
      <el-form
        :model="currentEditingItem"
        label-position="left"
        label-width="50px"
      >
        <el-form-item label="名称">
          <el-input v-model="currentEditingItem.name" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-popover :model="deletePopoverVisible" placement="top" width="160">
          <p>确定删除吗？</p>
          <div style="text-align: right; margin: 0">
            <el-button
              size="mini"
              type="text"
              @click="deletePopoverVisible = false"
            >取消</el-button>
            <el-button
type="primary"
size="mini" @click="handleDeleteConfirm">确定</el-button>
          </div>
          <el-button
slot="reference"
type="danger" icon="el-icon-delete">删 除</el-button>
        </el-popover>
        <el-button
style="margin-left: 10px;"
@click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEditConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAllCategories,
  listAllTags,
  updateTagOrCategory,
  removeTagOrCategory,
  createNewTagOrCategory
} from '@/api/post'
// import ItemTabPane from './components/ItemTabPane'
export default {
  // components: { ItemTabPane },
  data() {
    return {
      allTags: [],
      allCategories: [],
      activePane: 'tags',
      itemTypeMap: {
        allTagsPane: 'tags',
        allCategoriesPane: 'categories'
      },
      typeTextMap: {
        tags: '标签',
        categories: '分类'
      },
      item: {
        id: null,
        name: ''
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
    handleItemInputConfirm(newItem) {
      createNewTagOrCategory(this.activePane, newItem).then(response => {
        this.getCurrItemList().push(response.data)
        this.$message({
          message: '添加成功',
          type: 'success'
        })
      })
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
      console.log('hit listPostByItemId in index')
      // TODO:
      const queryValue = {}
      queryValue['tagId'] = id
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
