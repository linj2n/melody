<template>
  <div class="root-comment">
    <a-comment :avatar="comment.author.avatar">
      <template slot="datetime">
        {{ comment.createdAt | formatTime }}
      </template>
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
          v-if="comment.replyCount > 0"
          @click="handleMoreClick"
          class="comment-action-button"
        >
          <a-icon :type="getIcon" style="margin-right: 4px" />
          更多 ({{ comment.replyCount }})
        </span>
      </template>
      <NewCommentEditor
        :newCommentForm="newCommentForm"
        @handleSubmitNewComment="replyToComments"
      />
      <!-- child comment list -->
      <div class="child-comment-list" v-show="showChildComment">
        <a-list
          :dataSource="childComments"
          :pagination="pagination"
          :loading="childCommentsSpinning"
          itemLayout="horizontal"
        >
          <a-list-item slot="renderItem" slot-scope="item">
            <!-- Child comment -->
            <ChildComment
              :child-comment="item"
              @handleSubmitNewComment="replyToComments"
            />
          </a-list-item>
        </a-list>
      </div>
    </a-comment>
  </div>
</template>
<script>
import moment from 'moment'
import NewCommentEditor from './NewCommentEditor.vue'
import ChildComment from './ChildComment.vue'
// import parseTime from '../utils/time.js'
// var replies = [{
//   id: 1,
//   content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
//   created_at: '2019-10-21 12:00',
//   author: {
//     id: 1,
//     name: 'Tommy',
//     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
//     link: 'http://tommy.cn',
//     is_post_author: false
//   },
//   replyToAuthor: {
//     id: 2,
//     name: 'Jerry',
//     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
//     link: 'http://tommy.cn',
//     is_post_author: false
//   }
// }, {
//   id: 1,
//   content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure).',
//   created_at: '2019-10-21 12:00',
//   author: {
//     id: 1,
//     name: 'Tommy',
//     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
//     link: 'http://tommy.cn',
//     is_post_author: false
//   },
//   replyToAuthor: {
//     id: 2,
//     name: 'Jerry',
//     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
//     link: 'http://tommy.cn',
//     is_post_author: false
//   }
// }]
export default {
  name: 'RootComment',
  components: {
    NewCommentEditor,
    ChildComment
  },
  filters: {
    formatTime: function (value) {
      if (value) {
        return moment.unix(value).format('YYYY-DD-MM hh:mm')
      }
    }
  },
  props: {
    comment: {
      type: Object,
      required: false,
      default: () => { }
    }
  },
  created () {
    // this.initNewCommentForm()
  },
  computed: {
    getIcon: function () {
      return this.showChildComment ? 'down-circle' : 'right-circle'
    }
  },
  data () {
    return {
      moment,
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
        submitting: false,
        editorVisible: false
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
    replyToComments (newCommentForm) {
      // console.log('reply to comments , content: ' + newCommentForm.content)
      // const newComment = {
      //   id: 1,
      //   content: newCommentForm.content,
      //   created_at: moment().fromNow(),
      //   author: {
      //     id: 1,
      //     name: 'linj2n',
      //     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
      //     link: 'http://tommy.cn',
      //     is_post_author: false
      //   },
      //   replyToAuthor: {
      //     id: 2,
      //     name: 'Jerry',
      //     avatar: 'http://qiniuyunimage.cdn.linj2n.cn/avatar_db.jpeg',
      //     link: 'http://tommy.cn',
      //     is_post_author: false
      //   }
      // }
      // replies = [newComment, ...replies]
      // newCommentForm.submitting = true
      // this.childCommentsSpinning = true
      // this.showChildComment = true
      // setTimeout(() => {
      //   this.childComments = [...replies]
      //   this.childCommentsSpinning = false
      //   newCommentForm.editorVisible = false
      //   newCommentForm.submitting = false
      //   newCommentForm.content = ''
      // }, 2000)
    },
    handleReplyClick () {
      this.newCommentForm.editorVisible = !this.newCommentForm.editorVisible
    },
    handleMoreClick () {
      this.childCommentsSpinning = true
      // setTimeout(() => {
      //   // this.childComments = [...replies]
      //   replies.forEach(reply => {
      //     this.childComments.push(Object.assign({}, reply))
      //   })
      //   this.childCommentsSpinning = false
      // }, 2000)
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
