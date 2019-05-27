<template>
  <div
    class="app-container"
    style="background-color: #f0f2f5; padding-top: 5px"
  >
    <div class="attachments-header">
      <template>
        <el-input
          v-model="query.title"
          size="small"
          placeholder="附件名称"
          style="width: 300px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button
          class="filter-item"
          size="small"
          type="info"
          icon="el-icon-search"
          plain
          @click="handleFilter"
        >
          搜 索
        </el-button>
        <el-button
          class="filter-item"
          size="small"
          type="primary"
          icon="el-icon-upload"
          style="margin-left: 10px;"
        >
          上传
        </el-button>
      </template>
    </div>
    <div class="attachments-body">
      <el-row :gutter="20">
        <el-col
          v-for="attachment in attachments"
          :key="attachment.qiniuFile.key"
          :span="4"
          style="margin-bottom: 20px;"
        >
          <el-card
            :body-style="{ padding: '0px' }"
            shadow="hover"
            @click.native="handleEdit(attachment)"
          >
            <img
              :src="attachment.qiniuFile.key | fileSrc(qiniuPath)"
              class="image"
            >
            <div style="padding: 10px;">
              <span class="attachment-name">{{
                attachment.qiniuFile.key
              }}</span>
              <div class="bottom clearfix">
                <span class="attachment-time">
                  {{ attachment.qiniuFile.putTime | toSeconds | formatUnix }}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <pagination
        v-show="total > 0"
        style="background-color: #f0f2f5"
        :total="total"
        :page.sync="query.page"
        :limit.sync="query.size"
        @pagination="listAttachments"
      />
    </div>
    <el-dialog
      v-model="tempAttachment"
      title="附件"
      :visible.sync="dialogVisible"
      width="80%"
    ><div class="image-container">
       <el-image
         :src="tempAttachment.qiniuFile.key | fileSrc(qiniuPath)"
         fit="contain"
       />
     </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import moment from 'moment'
import { getToken } from '@/api/qiniu'
import { fetchAttachments } from '@/api/attachment'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'AttachmentList',
  components: { Pagination },
  filters: {
    fileSrc(key, domain) {
      return domain + key
    },
    toSeconds(nanoseconds) {
      if (nanoseconds) {
        return Math.round(nanoseconds / 10000000)
      }
    },
    formatUnix(value) {
      if (value) {
        return moment.unix(value).format('YYYY-DD-MM hh:mm')
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      qiniuPath: 'http://qiniuyunimage.cdn.linj2n.cn/',
      attachments: [],
      total: 0,
      query: {
        title: '',
        page: 1,
        limit: 20,
        sort: 'id,DESC' // TODO: add sort condition
      },
      tempAttachment: {
        id: null,
        name: '',
        description: '',
        qiniuFile: {
          id: null,
          key: '',
          hash: '',
          type: null,
          mimeType: '',
          putTime: null
        }
      }
    }
  },
  created() {
    this.listAttachments()
  },
  methods: {
    listAttachments() {
      fetchAttachments(this.query).then(response => {
        this.attachments = response.data.content
        this.total = response.data.totalElements
      })
    },
    beforeUpload(file) {
      const _self = this
      return new Promise((resolve, reject) => {
        getToken().then(response => {
          console.log('upload token' + response.data.uploadToken)
          // const key = response.data.qiniu_key
          const token = response.data.uploadToken
          _self._data.dataObj.token = token
          _self._data.dataObj.key = file.name
          // TODO: set upload url
          // this.uploadUrl = response.data.qiniu_url
          resolve(true)
        }).catch(err => {
          console.log(err)
          reject(false)
        })
      })
    },
    handleEdit(attachment) {
      this.tempAttachment = Object.assign({}, attachment)
      this.dialogVisible = true
    },
    handleFilter() {
      this.query.page = 1
      this.listAttachments()
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    }
  }
}
</script>
<style>
.attachment-name {
  font-size: 13px;
  color: #999;
}
.attachment-time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  margin-bottom: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.attachments-header {
  padding: 0px 10px;
  background-color: #ffffff;
}
.attachments-body {
  padding: 20px 10px;
  min-height: calc(100vh - 84px);
}
.filter-container {
  padding-bottom: 10px;
}
.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin: 10px;
}
.image-container {
}
</style>

