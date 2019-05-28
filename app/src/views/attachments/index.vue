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
            class="attachment-card"
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
      title="附件信息"
      :visible.sync="dialogVisible"
      width="70%"
    ><div>
      <el-row :gutter="20">
        <el-col :span="16">
          <el-image
            :src="tempAttachment.qiniuFile.key | fileSrc(qiniuPath)"
            class="image"
            fit="scale-down"
          />
        </el-col>
        <el-col
          :span="8"
          style="padding-left: 20px;"
          :model="tempAttachment"
          class="attachment-info-form"
        >
          <el-form label-position="top" label-width="80px" size="small">
            <el-form-item label="名称">
              <el-input v-model="tempAttachment.name" />
            </el-form-item>
            <el-form-item label="路径">
              <el-input v-model="tempAttachment.name" />
            </el-form-item>
            <el-form-item label="存储类型">
              <el-radio-group v-model="tempAttachment.qiniuFile.type">
                <el-radio :label="0">标准存储</el-radio>
                <el-radio :label="1">低频存储</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="MIME类型">
              <el-input v-model="tempAttachment.qiniuFile.mimeType" />
            </el-form-item>
            <div class="info-item">
              <label>文件大小/尺寸</label>
              <span class="attachment-time">{{
                tempAttachment.qiniuFile.putTime | toSeconds | formatUnix
              }}</span>
            </div>
            <div class="info-item">
              <label>更新时间</label>
              <span class="attachment-time">{{
                tempAttachment.qiniuFile.putTime | toSeconds | formatUnix
              }}</span>
            </div>
            <div class="info-item">
              <label>Markdown格式</label>
              <span
                class="copy-link"
                @click="handleCopy(srcUrlMarkdownVal, $event)"
              >{{ srcUrlMarkdownVal }}</span
              >
            </div>
            <el-form-item>
              <el-button type="primary">更 新</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import moment from 'moment'
import clip from '@/utils/clipboard' // use clipboard directly
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
    toMarkDown(srcUrl, name) {
      return `![${name}](${srcUrl})`
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
  computed: {
    // 计算属性的 getter
    srcUrlMarkdownVal: function() {
      // `this` 指向 vm 实例
      const name = this.tempAttachment.name
      const path = this.qiniuPath + this.tempAttachment.key
      return `![${name}](${path})`
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
    handleCopy(text, event) {
      clip(text, event)
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
.attachment-card {
  cursor: pointer;
}
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
.attachment-info-form {
  border-left: 1px solid #e6e6e6;
}
.info-item {
  margin-bottom: 18px;
}
.info-item label {
  float: none;
  display: block;
  text-align: left;
  padding: 0 0 10px;
}
.copy-link {
  cursor: pointer;
  margin-left: 10px;
}
.copy-link:hover {
  color: rgb(64, 158, 255);
}
</style>

