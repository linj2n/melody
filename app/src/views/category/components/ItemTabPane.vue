<template>
  <div class="ItemTabPane">
    <el-dropdown v-for="item in items" :trigger="trigger" :key="item.name">
      <span class="el-tag el-tag--info">{{ item.name }}</span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="listPostByItemId(item.id)">
          <span>
            <i class="el-icon-view el-icon--left"/>
            查看文章
          </span>
        </el-dropdown-item>
        <el-dropdown-item @click.native="handleEdit(item)">
          <span>
            <i class="el-icon-edit el-icon--left"/>
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
    <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ 新增标签</el-button>
    <el-dialog :title="itemType" :visible.sync="editDialogVisible">
      <el-form :model="currentEditingItem" label-position="left" label-width="50px" >
        <el-form-item label="名称">
          <el-input v-model="currentEditingItem.name"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-popover :model="deletePopoverVisible" placement="top" width="160" >
          <p>确定删除吗？</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="deletePopoverVisible = false">取消</el-button>
            <el-button type="primary" size="mini" @click="handleDeleteConfirm">确定</el-button>
          </div>
          <el-button slot="reference" type="danger" icon="el-icon-delete">删 除</el-button>
        </el-popover>
        <el-button style="margin-left: 10px;" @click="editDialogVisible = false" >取 消</el-button>
        <el-button type="primary" @click="handleEditConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'ItemTabPane',
  props: {
    label: {
      type: String,
      default: 'Item'
    },
    name: {
      type: String,
      default: 'Pane'
    },
    itemType: {
      type: String,
      default: 'Edit item'
    },
    trigger: {
      type: String,
      default: 'click'
    },
    items: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      currentEditingItem: {
        id: null,
        name: ''
      },
      editDialogVisible: false,
      deletePopoverVisible: false,
      itemInputVisible: false,
      itemInputValue: ''
    }
  },
  methods: {
    showTagInput() {
      this.itemInputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleItemInputConfirm() {
      const itemName = this.itemInputValue
      if (itemName) {
        const exited = this.items.find(item => item.name === itemName)
        if (exited) {
          this.$message({
            message: '标签已存在，请勿重复添加',
            type: 'warning'
          })
          return
        } else {
          const newItem = {}
          newItem['id'] = null
          newItem['name'] = itemName
          this.$emit('handleItemInputConfirm', newItem)
        }
      }
      this.itemInputVisible = false
      this.itemInputValue = ''
    },
    listPostByItemId(itemId) {
      console.log('hit listPostByItemId in ItemTabPane')
      this.$emit('listPostByItemId', itemId)
    },
    handleEdit(item) {
      this.currentEditingItem = Object.assign({}, item)
      this.editDialogVisible = true
    },
    handleDeleteConfirm() {
      this.deletePopoverVisible = false
      this.editDialogVisible = false
      this.$emit(
        'handleDeleteConfirm',
        Object.assign({}, this.currentEditingItem)
      )
    },
    handleEditConfirm() {
      this.deletePopoverVisible = false
      this.editDialogVisible = false
      const newItem = Object.assign({}, this.currentEditingItem)
      this.$emit('handleEditConfirm', newItem)
    }
  }
}
</script>
<style>
.el-tag {
  display: inline-block;
  margin-bottom: 1rem;
  margin-right: 1rem;
}
.el-tag:hover {
  background-color: rgba(64, 158, 255, 0.1);
  display: inline-block;
  padding: 0 10px;
  height: 32px;
  line-height: 30px;
  font-size: 12px;
  color: #409eff;
  border-radius: 4px;
  box-sizing: border-box;
  border: 1px solid rgba(64, 158, 255, 0.2);
  white-space: nowrap;
  cursor: pointer;
}
.el-popper[x-placement^='bottom'] {
  margin-top: -10px;
}
.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.button-new-tag:hover {
  background-color: rgba(103, 194, 58, 0.1);
  border-color: rgba(103, 194, 58, 0.2);
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
