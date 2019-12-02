<template>
  <div>
    <a-row type="flex" justify="center">
      <a-col :span="24">
        <a-modal
          title="使用社交媒体应用登录"
          v-model="loginModalVisible"
          :footer="null"
        >
          <a-row>
            <a-col :span="24" :style="{ textAlign: 'center' }">
              <a
                href="/api/v1/account/login/github"
                target="_blank"
                @click="requireToLogin"
              >
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
          :options="replyEditorOptions"
          @handleSubmitNewComment="replyToPost"
          @handleSocialLoginButton="handleSocialLoginButton"
        />
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
            <RootComment
              :comment="item"
              :post-id="postId"
              @handleSubmitNewComment="replyToComment"
            />
          </a-list-item>
        </a-list>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import NewCommentEditor from '@/components/NewCommentEditor.vue'
import RootComment from '@/components/RootComment.vue'
import { replyToPost, listAllPostComments, replyToComment } from '@/api/comment'
import { mapActions, mapState } from 'vuex'

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
  },
  computed: {
    ...mapState({
      id: state => state.id,
      userName: state => state.name,
      avatarUrl: state => state.avatarUrl,
      siteUrl: state => state.siteUrl,
      isAuthenticated: state => state.isAuthenticated
    })
  },
  data () {
    return {
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
      sort: 'createdAt,DESC', // TODO: add sort condition,
      replyEditorOptions: {
        submitting: false,
        visible: true,
        showAvatar: true,
        inputDisable: false,
        avatarSize: 64
      }
    }
  },
  methods: {
    ...mapActions({
      updateUserInfo: 'GetInfo'
    }),
    fetchComments () {
      listAllPostComments(this.postId, this.pagination, this.sort).then(res => {
        this.rootComments = res.data.content
        this.rootCommentsSpinning = false
        this.pagination.total = res.data.totalElements
      })
    },
    replyToPost (replyToAuthorId, newCommentForm, options) {
      newCommentForm.validateFields((err, values) => {
        if (!err) {
          options.submitting = true
          replyToPost(this.postId, { replyToAuthorId, ...values }).then(res => {
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
    replyToComment (parentCommentId, replyToAuthorId, newCommentForm, options) {
      newCommentForm.validateFields((err, values) => {
        if (!err) {
          options.submitting = true
          replyToComment(this.postId, parentCommentId, { replyToAuthorId, ...values }).then(res => {
            options.submitting = false
            if (res.code === 20000) {
              options.visible = false
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
    handleSocialLoginButton () {
      this.loginModalVisible = true
    },
    requireToLogin () {
      window.onfocus = function () {
        this.loginModalVisible = false
        window.location.reload()
      }
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
