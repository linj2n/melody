<template>
  <div class="app-container">
    <div class="filter-container">
      <template>
        <el-input v-model="listQuery.title" placeholder="标题" style="width:330px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-select v-model="listQuery.categoryId" multiple placeholder="请选择分类" class="filter-item" style="margin-left: 25px; width:330px;">
          <el-option v-for="category in categoryOptions" :key="category.id" :label="category.name" :value="category.id"/>
        </el-select>
        <el-select v-model="listQuery.tagId" multiple style="margin-left: 25px; width:330px;" placeholder="请选择标签" class="filter-item">
          <el-option v-for="tag in tagOptions" :key="tag.id" :label="tag.name" :value="tag.id" />
        </el-select>
        <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 20px;" @click="handleFilter">
          确定
        </el-button>
      </template>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column align="center" label="ID" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="300px" label="标题">
        <template slot-scope="{row}">
          <router-link :to="'/example/edit/'+row.id" class="link-type">
            <span>{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column>

      <el-table-column width="180px" align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createdAt }}</span>
        </template>
      </el-table-column>

      <!-- <el-table-column width="120px" align="center" label="Author">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>-->

      <!-- <el-table-column width="100px" label="Importance">
        <template slot-scope="scope">
          <svg-icon v-for="n in +scope.row.importance" :key="n" icon-class="star" class="meta-item__icon" />
        </template>
      </el-table-column>-->

      <el-table-column class-name="status-col" label="状态" width="110">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">{{ row.status }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/posts/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">Edit</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listPosts, listAllCategories, listAllTags } from '@/api/post'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'PostList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        PUBLISHED: 'success',
        DRAFT: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      categoryOptions: null,
      tagOptions: null,
      total: 0,
      listLoading: true,
      listQuery: {
        title: '',
        categoryId: null,
        tagId: null,
        page: 1,
        limit: 20,
        sort: 'createdAt,DESC' // TODO: add sort condition
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
    this.getTagOptions()
  },
  methods: {
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    getList() {
      this.listLoading = true
      listPosts(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements
        this.listLoading = false
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
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
