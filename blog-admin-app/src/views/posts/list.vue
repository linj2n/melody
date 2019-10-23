<template>
  <div class="app-container">
    <div class="filter-container">
      <template>
        <el-input
          v-model="listQuery.title"
          placeholder="标题"
          style="width:310px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.tagId"
          placeholder="请选择标签"
          multiple
          style=" width:310px;"
          class="filter-item"
        >
          <el-option
            v-for="tag in tagOptions"
            :key="tag.id"
            :label="tag.name"
            :value="tag.id"
          />
        </el-select>
        <el-select
          v-model="listQuery.categoryId"
          placeholder="请选择分类"
          multiple
          class="filter-item"
          style="width:310px;"
        >
          <el-option
            v-for="category in categoryOptions"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
        <div class="filter-item">
          <el-button
            type="info"
            icon="el-icon-search"
            plain
            @click="handleFilter"
          >
            搜 索
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-plus"
            style="margin-left: 10px;"
            @click="createNewPost"
          >
            新文章
          </el-button>
        </div>
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
        <template slot-scope="{ row }">
          <a
            :href="'http://localhost:8080/posts/' + row.id"
            class="link-type"
            target="_blank"
          >
            {{ row.title }}
          </a>
        </template>
      </el-table-column>

      <el-table-column width="180px" align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createdAt }}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="状态" width="110">
        <template slot-scope="{ row }">
          <el-tag :type="row.status | statusFilter">{{ row.status }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="浏览数" width="110">
        <template slot-scope="scope">
          <span>{{ scope.row.views }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/posts/' + scope.row.id + '/edit'">
            <el-button type="primary" size="small" icon="el-icon-edit" plain>
              编辑
            </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listPosts, listAllCategories, listAllTags, requestToNewPost } from '@/api/post'
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
        categoryId: this.$route.query.categoryId && Array.of(this.$route.query.categoryId),
        tagId: this.$route.query.tagId && Array.of(this.$route.query.tagId),
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
    },
    createNewPost() {
      requestToNewPost().then(response => {
        this.$router.push({ path: `/posts/${response.data}/edit` }) // -> /user/123
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
.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;
}
.link-type:hover,
.link-type:focus:hover {
  color: #20a0ff;
}
</style>
