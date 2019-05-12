<template>
  <div class="card-container">
    <el-row>
        <el-card>
          <el-tabs v-model="activePane">
          <el-tab-pane label="所有标签" name="tags">
            <ItemTabPane
              :items="allTags"
              @handleItemInputConfirm="handleItemInputConfirm"
              @listPostByItemId="listPostByItemId"
              @handleDeleteConfirm="handleDeleteConfirm"
              @handleEditConfirm="handleEditConfirm"
            />
          </el-tab-pane>
          <el-tab-pane label="所有分类" name="categories">
            <ItemTabPane
              :items="allCategories"
              @handleItemInputConfirm="handleItemInputConfirm"
              @listPostByItemId="listPostByItemId"
              @handleDeleteConfirm="handleDeleteConfirm"
              @handleEditConfirm="handleEditConfirm"
            />
          </el-tab-pane>
            
          </el-tabs>
        </el-card>
    </el-row>
    
  </div>
</template>

<script>
import { listAllCategories, listAllTags, createNewTag, updateTagOrCategory, removeTagOrCategory, createNewTagOrCategory } from '@/api/post'
import ItemTabPane from './components/ItemTabPane'
export default {
  components: { ItemTabPane },
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
      }
    };
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
      removeTagOrCategory(this.activePane, Object.assign({}, item)).then(response => {
        const indexOfItem = itemList.findIndex(e => e.id === item.id)
        itemList.splice(indexOfItem, 1)
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
</style>

