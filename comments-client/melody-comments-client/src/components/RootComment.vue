<template>
  <div class="root-comment">
    <a-comment :avatar="comment.author.avatar" :datetime="comment.created_at">
      <template slot="author">
        <a class="comment-author" :href="comment.author.link">
          {{ comment.author.name }}
        </a>
      </template>
      <!-- comment content -->
      <template slot="content">
        <p slot="content">{{ comment.content }}</p>
      </template>
      <!-- comment actions -->
      <template slot="actions">
        <span @click="handleReplyClick" class="comment-action-button">
          <a-icon type="message" style="margin-right: 4px" />
          回复
        </span>
        <span
          v-if="comment.reply_count > 0"
          @click="handleMoreClick"
          class="comment-action-button"
        >
          <a-icon :type="getIcon" style="margin-right: 4px" />
          更多({{ comment.reply_count }})
        </span>
      </template>
      <div class="new-comment-editor" v-show="showReplyEditor">
        <NewCommentEditor
          :newCommentForm="newCommentForm"
          @handleSubmitNewComment="handleSubmitNewComment"
        />
      </div>
      <!-- child comment list -->
      <div
        class="child-comment-list"
        v-if="showChildComment && comment.reply_count"
      >
        <a-list
          :dataSource="childComments"
          :pagination="pagination"
          :loading="childCommentsSpinning"
          itemLayout="horizontal"
        >
          <a-list-item
            slot="renderItem"
            @handleSubmitNewComment="handleSubmitNewComment"
            slot-scope="item"
          >
            <!-- Child comment -->
            <ChildComment :child-comment="item" />
          </a-list-item>
        </a-list>
      </div>
    </a-comment>
  </div>
</template>
<script>
import NewCommentEditor from './NewCommentEditor.vue'
import ChildComment from './ChildComment.vue'
var replies = [{
  id: 1,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  created_at: '2019-10-21 12:00',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  replyToAuthor: {
    id: 2,
    name: 'Jerry',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  }
}, {
  id: 1,
  content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
  created_at: '2019-10-21 12:00',
  author: {
    id: 1,
    name: 'Tommy',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  },
  replyToAuthor: {
    id: 2,
    name: 'Jerry',
    avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
    link: 'http://tommy.cn',
    is_post_author: false
  }
}]
export default {
  name: 'RootComment',
  components: {
    NewCommentEditor,
    ChildComment
  },
  props: {
    comment: {
      type: Object,
      required: false,
      default: () => { }
    }
  },
  created () {
    this.initNewCommentForm()
  },
  computed: {
    getIcon: function () {
      return this.showChildComment ? 'down-circle' : 'right-circle'
    }
  },
  data () {
    return {
      showReplyEditor: false,
      showChildComment: false,
      childCommentsSpinning: true,
      childComments: [],
      pagination: {
        onChange: (page, pageSize) => {
          console.log('page: ' + page)
          console.log('pageSize: ' + pageSize)
        },
        pageSize: 3,
        hideOnSinglePage: true,
        size: 'small'
      },
      newCommentForm: {
        replyToAuthorId: null,
        author: '',
        avatar: '',
        placeholder: '',
        content: '',
        submitting: false
      }
    }
  },
  methods: {
    initNewCommentForm () {
      this.newCommentForm.parentCommentId = this.comment.id
      this.newCommentForm.placeholder = '回复 @' + this.comment.author.name
      this.newCommentForm.content = ''
      this.newCommentForm.replyToAuthorId = this.comment.author.id
      this.newCommentForm.submitting = false
      this.newCommentForm.avatar = ''
      this.newCommentForm.author = ''
    },
    handleSubmitNewComment (newCommentForm) {
      this.$emit('handleSubmitNewComment', newCommentForm)
    },
    handleReplyClick () {
      this.showReplyEditor = !this.showReplyEditor
    },
    handleMoreClick () {
      const vm = this
      vm.childCommentsSpinning = true
      setTimeout(() => {
        this.childComments = [...replies]
        vm.childCommentsSpinning = false
      }, 2000)
      this.showChildComment = !this.showChildComment
    }
  }
}
</script>
<style scoped>
.child-comment-list {
  margin-left: 2rem;
  padding-left: 1rem;
}
</style>
