<template>
  <el-dropdown>
    <span class="el-tag el-tag--info">{{ item.name }}</span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="listPosts()">
        <span>
          <i class="el-icon-view el-icon--left" />
          查看文章
        </span>
      </el-dropdown-item>
      <el-dropdown-item @click.native="handleEdit()">
        <span>
          <i class="el-icon-edit el-icon--left" />
          编辑
        </span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>
<script>
export default {
  name: 'Item',
  props: {
    item: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  methods: {
    listPosts() {
      this.$emit('listPosts', this.item.id)
    },
    handleEdit() {
      this.$emit('handleEdit', Object.assign({}, this.item))
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
.el-popper[x-placement^="bottom"] {
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
