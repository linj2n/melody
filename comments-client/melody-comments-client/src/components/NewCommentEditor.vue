<template>
  <div class="new-comment-editor">
    <a-comment>
      <a-avatar
        v-if="newCommentForm.avatar && newCommentForm.author"
        slot="avatar"
        :src="newCommentForm.avatar"
        :alt="newCommentForm.author"
      />
      <div slot="content" class="new-comment-editor-area">
        <a-form-item>
          <a-textarea
            :rows="4"
            v-model="newCommentForm.content"
            :placeholder="newCommentForm.placeholder"
          ></a-textarea>
        </a-form-item>
        <a-form-item>
          <a-button
            htmlType="submit"
            :loading="newCommentForm.submitting"
            @click="handleSubmit"
            type="primary"
          >
            评论
          </a-button>
        </a-form-item>
      </div>
    </a-comment>
  </div>
</template>
<script>
import moment from 'moment'
export default {
  name: 'NewCommentEditor',
  props: {
    newCommentForm: {
      type: Object,
      required: true,
      default: () => { }
    }
  },
  data () {
    return {
      moment
    }
  },
  methods: {
    handleSubmit () {
      this.$emit('handleSubmitNewComment', this.newCommentForm)
    }
  }
}
</script>
<style scoped>
</style>
