<template>
  <div>
    <a-row type="flex" justify="center">
      <a-col :span="24">
        <a-modal title="填写信息" v-model="loginModalVisible" :footer="null">
          <a-form
            :form="visitorInfo"
            @submit="handleSubmitUserProfile"
            layout="vertical"
          >
            <a-form-item label="称呼">
              <a-input
                v-decorator="[
                  'name',
                  {
                    rules: [
                      { required: true, message: 'Please input your note!' }
                    ]
                  }
                ]"
              />
            </a-form-item>
            <a-form-item label="邮箱">
              <a-input
                v-decorator="[
                  'email',
                  {
                    rules: [
                      {
                        type: 'email',
                        message: 'The input is not valid E-mail!'
                      },
                      {
                        required: true,
                        message: 'Please input your E-mail!'
                      }
                    ]
                  }
                ]"
              />
            </a-form-item>
            <a-form-item label="网站">
              <a-input v-decorator="['link']" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" html-type="submit">
                确认
              </a-button>
            </a-form-item>
          </a-form>
          <a-divider>或者</a-divider>
          <a-row>
            <a-col :span="24" :style="{ textAlign: 'center' }">
              <a href="/api/v1/account/login/github" target="_blank">
                <a-icon
                  type="github"
                  theme="filled"
                  style="font-size: 36px; cursor: pointer;"
                />
              </a>
            </a-col>
          </a-row>
        </a-modal>
        <!-- reply editor -->
        <NewCommentEditor
          :options="{
            submitting: false,
            visible: true,
            showAvatar: true,
            inputDisable: false,
            avatarSize: 64
          }"
          :new-comment-form="newCommentForm"
          :is-authenticated="isAuthenticated"
          @handleSubmitNewComment="replyToPost"
          @handleInputClick="validateAuthState"
        />
        <a-button
          type="primary"
          html-type="submit"
          @click="isAuthenticated = !isAuthenticated"
        >
          认证
        </a-button>
        <!-- comment list -->
        <a-list
          :dataSource="rootComments"
          :pagination="pagination"
          :loading="rootCommentsSpinning"
          itemLayout="horizontal"
          style="margin: 1.5rem 0;"
        >
          <template v-if="rootComments.length" slot="header">
            共 {{ pagination.total }} 条评论
          </template>
          <a-list-item slot="renderItem" slot-scope="item">
            <RootComment :comment="item" :post-id="postId" />
          </a-list-item>
        </a-list>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import NewCommentEditor from '@/components/NewCommentEditor.vue'
import RootComment from '@/components/RootComment.vue'
import moment from 'moment'
import { replyToPost, listAllPostComments } from '@/api/comment'
import { getAccountInfo } from '@/api/user'
import { getAuthState } from '@/utils/auth'

export default {
  name: 'CommentClient',
  components: {
    NewCommentEditor,
    RootComment
  },
  props: {
    postId: {
      type: Number,
      required: true
    }
  },
  created () {
    this.fetchComments()
    this.initUserProfileForm()
    this.getAuthState()
  },
  data () {
    return {
      moment,
      visitorInfo: this.$form.createForm(this, { name: 'coordinated' }),
      loginModalVisible: false,
      rootComments: [],
      rootCommentsSpinning: true,
      pagination: {
        onChange: (page, pageSize) => {
          this.pagination.current = page
          this.fetchComments()
        },
        current: 1,
        hideOnSinglePage: true,
        pageSize: 10
      },
      user: null,
      sort: 'createdAt,DESC', // TODO: add sort condition,
      newCommentForm: {
        name: '',
        link: '',
        email: '',
        content: '',
        author: '',
        avatar: ''
      },
      isAuthenticated: false
    }
  },
  watch: {
    user: function (val) {
      this.newCommentForm.name = val.username
      this.newCommentForm.link = val.link
      this.newCommentForm.avatar = val.avatarUrl
      this.newCommentForm.author = val.name
    }
  },
  methods: {
    getAccountInfo () {
      if (this.isAuthenticated && this.user == null) {
        getAccountInfo().then(res => {
          const info = res.data
          this.user = { ...info }
        })
      }
    },
    initUserProfileForm () {

    },
    handleSubmitUserProfile (e) {
      e.preventDefault()
    },
    fetchComments () {
      listAllPostComments(this.postId, this.pagination, this.sort).then(res => {
        this.rootComments = res.data.content
        this.rootCommentsSpinning = false
        this.pagination.total = res.data.totalElements
      })
    },
    replyToPost (newCommentForm, options) {
      newCommentForm.validateFields((err, values) => {
        if (!err) {
          options.submitting = true
          console.log(values)
          replyToPost(this.postId, values).then(res => {
            options.submitting = false
            if (res.code === 20000) {
              this.$message.success(res.message)
              newCommentForm.resetFields()
              this.fetchComments()
            } else {
              this.$message.error(res.message)
            }
          })
        }
      })
    },
    validateAuthState (newCommentForm, options) {
      this.getAccountInfo()
      this.visitorInfo.validateFields((err, value) => {
        const isEmptyForm = Object.keys(value).length === 0
        const isValidForm = !err && !isEmptyForm
        if (!isValidForm && !this.isAuthenticated()) {
          newCommentForm.content = ''
          this.loginModalVisible = true
        }
      })
    },
    getAuthState () {
      this.isAuthenticated = getAuthState() && getAuthState() === 'true'
    }
  }
}
</script>

<style lang="less">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 60px;
  margin-bottom: 60px;
}
.root-comment {
  width: 100%;
}
.root-comment {
  .ant-comment {
    .ant-comment-inner {
      padding: 0 0;
    }
  }
}
.child-comment {
  .ant-comment {
    .ant-comment-inner {
      padding: 0 0;
    }
  }
}
.new-comment-editor {
  .ant-comment {
    .ant-comment-inner {
      padding: 0 0;
    }
  }
}
.new-comment-editor-area {
  .ant-row.ant-form-item {
    margin-bottom: 12px;
  }
}
.comment-action-button {
  color: rgba(0, 0, 0, 0.65);
}
.comment-action-button:hover {
  color: #1890ff;
}

.comment-author:hover {
  color: #1890ff;
}
.comment-author {
  color: rgba(0, 0, 0, 0.65);
}

.child-comment-list {
  .ant-list-item {
    padding: 6px 0;
  }
}
ul.anticons-list {
  margin: 10px 0;
  list-style: none;
  overflow: hidden;
}
ul.anticons-list li {
  float: left;
  width: 16.66%;
  text-align: center;
  list-style: none;
  cursor: pointer;
  height: 100px;
  color: #555;
  transition: color 0.3s ease-in-out, background-color 0.3s ease-in-out;
  position: relative;
  margin: 3px 0;
  border-radius: 4px;
  background-color: #fff;
  overflow: hidden;
  padding: 10px 0 0;
}
ul.anticons-list li .anticon {
  font-size: 36px;
  margin: 12px 0 8px;
  transition: transform 0.3s ease-in-out;
  will-change: transform;
}
</style>
