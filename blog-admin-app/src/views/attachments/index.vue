<template>
  <div
    class="app-container"
    style="background-color: #f0f2f5; padding-top: 5px"
  >
    <div class="attachments-header">
      <template>
        <el-input
          v-model="query.title"
          placeholder="附件名称"
          style="width: 300px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button
          class="filter-item"
          type="info"
          icon="el-icon-search"
          plain
          @click="handleFilter"
        >
          搜 索
        </el-button>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-upload"
          @click="uploadDialogVisible = true"
        >
          上 传
        </el-button>
      </template>
    </div>
    <div class="attachments-body">
      <el-row :gutter="20">
        <el-col
          v-for="attachment in attachments"
          :key="attachment.id"
          :span="4"
          style="margin-bottom: 20px;"
        >
          <el-card
            :body-style="{ padding: '0px' }"
            class="attachment-card"
            shadow="hover"
            @click.native="handleEdit(attachment)"
          >
            <img :src="attachment.qiniuFile.url" class="image" >
            <div style="padding: 10px;">
              <span class="attachment-name">{{ attachment | storeName }}</span>
              <div class="bottom clearfix">
                <span class="attachment-time">
                  {{ attachment.qiniuFile.putTime | formatUnix }}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="query.page"
        :limit.sync="query.size"
        style="background-color: #f0f2f5"
        @pagination="listAttachments"
      />
    </div>
    <el-dialog
      :visible.sync="uploadDialogVisible"
      title="上传附件"
      @close="handleFilter"
    >
      <el-row>
        <el-col :span="24">
          <el-upload
            ref="upload"
            :data="dataObj"
            :before-upload="beforeUpload"
            :on-error="handlingUploadError"
            :on-progress="onProgress"
            :auto-upload="false"
            :action="uploadUrl"
            drag
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击选取文件</em>
            </div>
          </el-upload>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center" style="margin-top: 20px">
        <el-col :span="6">
          <el-button style="width: 100%" type="success" @click="submitUpload">
            上传</el-button
            >
        </el-col>
      </el-row>
    </el-dialog>
    <el-dialog
      v-model="tempAttachment"
      :visible.sync="dialogVisible"
      title="附件信息"
      width="70%"
    ><div>
      <el-row :gutter="20">
        <el-col :span="16">
          <el-image
            :src="tempAttachment.qiniuFile.url"
            class="image"
            fit="scale-down"
          />
        </el-col>
        <el-col
          :span="8"
          :model="tempAttachment"
          style="padding-left: 20px;"
          class="attachment-info-form"
        >
          <el-form label-position="top" label-width="80px" size="small">
            <el-form-item label="名称">
              <el-input v-model="tempAttachment.name" />
            </el-form-item>
            <el-form-item label="路径">
              <el-input v-model="tempAttachment.qiniuFile.path" />
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
              <label>文件大小</label>
              <span class="attachment-time">
                {{ tempAttachment.qiniuFile.size }} KB
              </span>
            </div>
            <div class="info-item">
              <label>更新时间</label>
              <span class="attachment-time">{{
                tempAttachment.qiniuFile.putTime | formatUnix
              }}</span>
            </div>
            <div class="info-item">
              <label>Markdown格式</label>
              <span
                class="copy-link"
                @click="
                  handleCopy(getSrcUrlMarkdownValue(tempAttachment), $event)
                "
              >{{ getSrcUrlMarkdownValue(tempAttachment) }}</span
              >
            </div>
            <el-popover
              v-model="deletePopoverVisible"
              placement="top"
              width="160"
            >
              <p>确定删除吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button
                  size="small"
                  type="text"
                  @click="deletePopoverVisible = false"
                >取消</el-button
                >
                <el-button
                  :loading="deleteButtonLoading"
                  type="primary"
                  size="small"
                  @click="handleRemoveConfirm(tempAttachment.id)"
                >确定</el-button
                >
              </div>
              <el-button
                slot="reference"
                :loading="deleteButtonLoading"
                size="small"
                type="danger"
              >删 除</el-button
              >
            </el-popover>
            <el-button
              style="margin-left: 10px"
              size="small"
              @click="dialogVisible = false"
            >取 消</el-button
            >
            <el-button
              :loading="updateButtonLoading"
              size="mini"
              type="primary"
              @click="saveAttachment"
            >更 新</el-button
            >
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
import { getToken, fecthUploadUrl } from '@/api/qiniu'
import { fetchAttachments, updateAttachment, deleteAttachment } from '@/api/attachment'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'AttachmentList',
  components: { Pagination },
  filters: {
    storeName(attachment) {
      const path = attachment.qiniuFile.path
      const name = attachment.name
      return `${path}${name}`
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
      uploadDialogVisible: false,
      updateButtonLoading: false,
      deleteButtonLoading: false,
      deletePopoverVisible: false,
      attachments: [],
      uploadUrl: '',
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
          path: '',
          hash: '',
          type: null,
          mimeType: '',
          putTime: null,
          url: null,
          size: null
        }
      },
      dataObj: { token: '', key: '' }
    }
  },
  created() {
    this.listAttachments()
    this.getUploadUrl()
  },
  methods: {
    getUploadUrl() {
      fecthUploadUrl().then(response => {
        this.uploadUrl = response.data.uploadUrl
      })
    },
    getSrcUrlMarkdownValue(attachment) {
      const url = attachment.qiniuFile.url
      const name = attachment.name
      return `![${name}](${url})`
    },
    getStoreName(attachment) {
      const path = attachment.qiniuFile.path
      const name = attachment.name
      return `${path}${name}`
    },
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
    handlingUploadError(err, file, fileList) {
      console.log(err)
      this.$message.error('服务器出错，上传失败。')
    },
    handlingUploadSuccess(response, file, fileList) {
      this.$message.error('上传成功')
    },
    onProgress(e, file) {
      console.log(e.percent, file)
    },
    submitUpload() {
      this.$refs.upload.submit()
    },
    saveAttachment() {
      const attachment = Object.assign({}, this.tempAttachment)
      this.updateButtonLoading = true
      updateAttachment(attachment).then(response => {
        this.updateButtonLoading = false
        const newAttachment = response.data
        this.tempAttachment = Object.assign({}, newAttachment)
        this.attachments.splice(this.attachments.findIndex(a => a.id === newAttachment.id), 1, newAttachment)
        this.$message({
          message: response.message,
          type: 'success'
        })
      })
    },
    handleEdit(attachment) {
      this.tempAttachment = Object.assign({}, attachment)
      const qiniuFile = Object.assign({}, this.tempAttachment.qiniuFile)
      this.tempAttachment.qiniuFile = qiniuFile
      this.deleteButtonLoading = false
      this.updateButtonLoading = false
      this.dialogVisible = true
    },
    handleFilter() {
      this.query.page = 1
      this.listAttachments()
    },
    handleCopy(text, event) {
      clip(text, event)
    },
    handleRemoveConfirm(id) {
      this.deleteButtonLoading = true
      deleteAttachment(id).then(response => {
        this.attachments.splice(this.attachments.findIndex(a => a.id === id), 1)
        this.deletePopoverVisible = false
        this.dialogVisible = false
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
.el-upload {
  display: block;
}
.el-upload-dragger {
  width: 100%;
}
</style>
