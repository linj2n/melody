<template>
  <div class="child-comment">
    <a-comment
      :avatar="childComment.author.avatar"
      :content="childComment.content"
    >
      <template slot="datetime">
        {{ childComment.createdAt | parseTime("YYYY-MM-DD HH:mm") }}
      </template>
      <template slot="author">
        <span>
          <a class="comment-author" :href="childComment.author.link">
            {{ childComment.author.name }}
          </a>
        </span>
        <span
          v-if="childComment.replyToAuthor"
          style="margin-left: 0.5rem; margin-right: 0.5rem;"
        >
          回复
        </span>
        <span v-if="childComment.replyToAuthor">
          <a class="comment-author" :href="childComment.replyToAuthor.link">
            @{{ childComment.author.name }}
          </a>
        </span>
      </template>
      <template slot="actions">
        <span @click="handleReplyClick" class="comment-action-button">
          <a-icon type="message" style="margin-right: 4px" />
          回复
        </span>
      </template>
      <NewCommentEditor
        :newCommentForm="newCommentForm"
        :options="replyEditorOptions"
        @handleSubmitNewComment="handleSubmitNewComment"
      />
    </a-comment>
  </div>
</template>
<script>
import NewCommentEditor from './NewCommentEditor.vue'
import { parseUnixTime } from '@/utils/time'
export default {
  name: 'ChildComment',
  components: {
    NewCommentEditor
  },
  filters: {
    parseTime: (value, format) => parseUnixTime(value, format)
  },
  created () {
    this.init()
  },
  data () {
    return {
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
  props: {
    childComment: {
      type: Object,
      required: true,
      default: () => { }
    }
  },
  methods: {
    init () {
      const author = this.childComment.author
      this.replyEditorOptions.placeholder = '回复 @' + author.name
      this.newCommentForm.replyToAuthorId = author.id
    },
    handleSubmitNewComment (newCommentForm, options) {
      this.$emit('handleSubmitNewComment', newCommentForm, options)
    },
    handleReplyClick () {
      this.replyEditorOptions.visible = !this.replyEditorOptions.visible
    }
  }
}
</script>
<style scoped>
.child-comment {
  width: 100%;
}
</style>
