<template>
  <div class="root-comment">
    <a-comment :avatar="comment.author.avatar">
      <template slot="avatar">
        <a-avatar size="default" icon="user" :src="comment.author.avatar" />
      </template>
      <template slot="datetime">
        {{ comment.createdAt | parseTime("YYYY-MM-DD HH:mm") }}
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
          v-if="childComments.length > 0"
          @click="handleMoreClick"
          class="comment-action-button"
        >
          <a-icon :type="getIcon" style="margin-right: 4px" />
          更多 ({{ childComments.length }})
        </span>
      </template>
      <NewCommentEditor
        :newCommentForm="newCommentForm"
        :options="replyEditorOptions"
        :showAvatar="false"
        @handleSubmitNewComment="replyToComments"
      />
      <!-- child comment list -->
      <div class="child-comment-list" v-show="showChildComment">
        <a-list
          :dataSource="childComments"
          :pagination="pagination"
          :loading="childCommentsLoading"
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
import NewCommentEditor from './NewCommentEditor.vue'
import ChildComment from './ChildComment.vue'
import { parseUnixTime } from '@/utils/time'
import { replyToComment, listChildComments } from '@/api/comment'
export default {
  name: 'RootComment',
  components: {
    NewCommentEditor,
    ChildComment
  },
  filters: {
    parseTime: (value, format) => parseUnixTime(value, format)
  },
  props: {
    comment: {
      type: Object,
      required: false,
      default: () => { }
    }
  },
  created () {
    this.init()
  },
  computed: {
    getIcon: function () {
      return this.showChildComment ? 'down-circle' : 'right-circle'
    }
  },
  watch: {
    comment: function (newComment, oldComment) {
      this.init()
    }
  },
  data () {
    return {
      showReplyEditor: false,
      showChildComment: false,
      childCommentsLoading: true,
      childComments: [],
      pagination: {
        onChange: (page, pageSize) => {
          console.log('page: ' + page)
          console.log('pageSize: ' + pageSize)
        },
        current: 1,
        pageSize: 10,
        hideOnSinglePage: true,
        size: 'small'
      },
      sort: 'createdAt,DESC',
      newCommentForm: {
        replyToAuthorId: null,
        content: ''
      },
      replyEditorOptions: {
        visible: false,
        submitting: false,
        placeholder: ''
      }
    }
  },
  methods: {
    init () {
      const author = this.comment.author
      this.replyEditorOptions.placeholder = '回复 @' + author.name
      this.fetchChildComments()
    },
    replyToComments (newCommentForm, options) {
      options.submitting = true
      replyToComment(1, this.comment.id, { email: '', content: newCommentForm.content, replyToAuthorId: newCommentForm.replyToAuthorId }).then(res => {
        options.submitting = false
        options.visible = false
        newCommentForm.content = ''
        this.fetchChildComments()
      })
    },
    fetchChildComments () {
      this.childCommentsLoading = true
      listChildComments(1, this.comment.id, this.sort).then(res => {
        this.childComments = res.data.content
        this.childCommentsLoading = false
      })
    },
    handleReplyClick () {
      this.replyEditorOptions.visible = !this.replyEditorOptions.visible
    },
    handleMoreClick () {
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
