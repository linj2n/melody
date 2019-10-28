<template>
  <div class="new-comment-editor" v-show="options.visible">
    <a-comment>
      <template slot="avatar" v-if="options.showAvatar">
        <a-avatar
          size="default"
          icon="user"
          :src="newCommentForm.avatar"
          :alt="newCommentForm.author"
        />
      </template>
      <div slot="content" class="new-comment-editor-area">
        <a-form-item>
          <a-textarea
            :rows="4"
            v-model="newCommentForm.content"
            :placeholder="options.placeholder"
          ></a-textarea>
        </a-form-item>
        <a-form-item>
          <a-button
            htmlType="submit"
            :loading="options.submitting"
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
export default {
  name: 'NewCommentEditor',
  props: {
    newCommentForm: {
      type: Object,
      required: true,
      default: () => { }
    },

    options: {
      type: Object,
      required: false,
      default: function () {
        return {
          submitting: false,
          visible: true,
          showAvatar: true
        }
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$emit('handleSubmitNewComment', this.newCommentForm, this.options)
    }
  }
}
</script>
<style scoped>
.new-comment-editor {
  width: 100%;
}
</style>
