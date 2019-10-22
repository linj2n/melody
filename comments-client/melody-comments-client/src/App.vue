<template>
  <div id="app">
    <a-row type="flex" justify="center">
      <a-col :span="12">
        <!-- new comment editor -->
        <NewCommentEditor
          :new-comment-form="newCommentForm"
          @handleSubmitNewComment="handleSubmitNewComment"
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
var comments = [{
  id: 1,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  created_at: '2019-10-21 12:00',
  reply_count: 11
}, {
  id: 2,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  created_at: '2019-10-21 12:00',
  reply_count: 11
}, {
  id: 3,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: true
  },
  created_at: '2019-10-21 12:00',
  reply_count: 11
}, {
  id: 4,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  created_at: '2019-10-21 12:00',
  reply_count: 0
}, {
  id: 5,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  created_at: '2019-10-21 12:00',
  reply_count: 11
}, {
  id: 6,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  created_at: '2019-10-21 12:00',
  reply_count: 11
}]
export default {
  name: 'app',
  components: {
    NewCommentEditor,
    RootComment
  },
  created () {
    this.initComments()
  },
  data () {
    return {
      rootComments: [],
      rootCommentsSpinning: true,
      user: {
        avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
        name: 'linj2n'
      },
      pagination: {
        onChange: (page, pageSize) => {
          console.log('page: ' + page)
          console.log('pageSize: ' + pageSize)
        },
        hideOnSinglePage: true,
        pageSize: 3
      },
      post: {
        id: 1
      },
      newCommentForm: {
        replyToAuthorId: null,
        author: 'linj2n',
        avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
        placeholder: '回复@linj2n.....',
        content: '',
        submitting: false
      }
    }
  },
  methods: {
    initComments () {
      const vm = this
      setTimeout(() => {
        vm.rootComments = [...comments]
        vm.rootCommentsSpinning = false
      }, 2000)
    },
    replyToPost () {

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
