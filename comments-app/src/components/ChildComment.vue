<template>
  <div class="child-comment">
    <a-comment
      :avatar="childComment.author.avatar"
      :datetime="childComment.createdAt"
      :content="childComment.content"
    >
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
        @handleSubmitNewComment="handleSubmitNewComment"
      />
    </a-comment>
  </div>
</template>
<script>
import NewCommentEditor from './NewCommentEditor.vue'
export default {
  name: 'ChildComment',
  components: {
    NewCommentEditor
  },
  data () {
    return {
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
  props: {
    childComment: {
      type: Object,
      required: true,
      default: () => { }
    }
  },
  methods: {
    handleSubmitNewComment (newCommentForm) {
      this.$emit('handleSubmitNewComment', newCommentForm)
    },
    handleReplyClick () {
      this.newCommentForm.editorVisible = !this.newCommentForm.editorVisible
    }
  }
}
</script>
<style scoped>
</style>
