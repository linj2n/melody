<template>
  <div id="app">
    <a-row type="flex" justify="center">
      <a-col :span="12">
        <!-- new comment editor -->
        <NewCommentEditor
          :new-comment-form="newCommentForm"
          @handleSubmitNewComment="replyToPost"
        />
        <!-- comment list -->
        <a-list
          :dataSource="rootComments"
          :pagination="pagination"
          :loading="rootCommentsSpinning"
          itemLayout="horizontal"
        >
          <template v-if="rootComments.length" slot="header">
            共 {{ rootComments.length }} 条评论
          </template>
          <a-list-item slot="renderItem" slot-scope="item">
            <RootComment
              :comment="item"
              @handleSubmitNewComment="handleSubmitNewComment"
            />
          </a-list-item>
        </a-list>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import NewCommentEditor from './components/NewCommentEditor.vue'
import RootComment from './components/RootComment.vue'
import moment from 'moment'
import { listAllPostComments } from '@/api/comment'
export default {
  name: 'app',
  components: {
    NewCommentEditor,
    RootComment
  },
  created () {
    this.fetchComments()
  },
  data () {
    return {
      moment,
      rootComments: [],
      rootCommentsSpinning: true,
      user: {
        avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
        name: 'linj2n'
      },
      pagination: {
        onChange: (page, pageSize) => {
          this.pagination.current = page
          this.fetchComments()
          window.scrollTo(0, 0)
        },
        current: 1,
        hideOnSinglePage: true,
        pageSize: 10
      },
      sort: 'createdAt,DESC', // TODO: add sort condition,
      post: {
        id: 1
      },
      newCommentForm: {
        replyToAuthorId: null,
        author: 'linj2n',
        avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
        placeholder: '回复@linj2n.....',
        content: '',
        submitting: false,
        editorVisible: true
      }
    }
  },
  methods: {
    fetchComments () {
      // const vm = this
      // setTimeout(() => {
      //   vm.rootComments = [...comments]
      //   vm.rootCommentsSpinning = false
      // }, 2000)
      listAllPostComments(1, this.pagination, this.sort).then(res => {
        this.rootComments = res.data.content
        this.rootCommentsSpinning = false
        this.pagination.total = res.data.totalElements
      })
    },
    replyToPost (newCommentForm) {
      if (!newCommentForm.content) {
        return
      }

      newCommentForm.submitting = true

      setTimeout(() => {
        newCommentForm.submitting = false
        this.rootComments = [
          {
            id: this.rootComments.length,
            content: newCommentForm.content,
            author: {
              id: 1,
              name: 'linj2n',
              avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
              link: 'http://linj2n.cn',
              is_post_author: false
            },
            created_at: moment().fromNow(),
            reply_count: 0
          },
          ...this.rootComments
        ]
        newCommentForm.content = ''
      }, 1000)
    },
    replyToComment (newCommentForm) {

    },
    handleSubmitNewComment (newCommentForm) {
      // validate
      if (!newCommentForm.content) {
        return
      }
      newCommentForm.submitting = true
      setTimeout(() => {
        newCommentForm.submitting = false
        console.log('submit to post : ' + newCommentForm.content)
        newCommentForm.content = ''
      }, 1000)
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
</style>
