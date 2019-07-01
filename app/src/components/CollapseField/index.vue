<template>
  <div :class="{ active: open.isActive }" class="collapse-field">
    <div class="collapse-field-title-view">
      <h4 class="collapse-field-title">{{ label }}</h4>
      <div class="collapse-field-descr">{{ description }}</div>
      <div class="collapse-field-button" @click="toggleClick(open)">
        {{ open.isActive ? "收起" : "编辑" }}
      </div>
    </div>
    <div :class="{ active: open.isActive }" class="collapse-field-content">
      <slot />
    </div>
  </div>
</template>
<script>
export default {
  name: 'CollapseField',
  props: {
    label: {
      type: String,
      default: 'title'
    },
    description: {
      type: String,
      default: 'description'
    },
    open: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  methods: {
    toggleClick (open) {
      this.$emit('toggleClick', open)
    }
  }
}
</script>
<style>
.collapse-field {
  display: block;
}
.collapse-field-title-view {
  padding: 10px 0;
  border-top: 1px solid #ebebeb;
  overflow: hidden;
  position: relative;
}

.collapse-field-title {
  font-weight: 500;
  color: #1a1a1a;
  font-size: 15px;
  height: 21px;
  line-height: 21px;
  margin: 0;
  padding-bottom: 5px;
}
.collapse-field-descr {
  color: #8590a6;
  font-size: 14px;
  line-height: 1.5;
}
.collapse-field-button {
  text-align: center;
  cursor: pointer;
  background: none;
  color: #0084ff;
  display: inline-block;
  font-size: 15px;
  height: 21px;
  line-height: 21px;
  text-decoration: none;
  position: absolute;
  right: 20px;
  top: 50%;
  -webkit-transform: translateY(-50%);
  transform: translateY(-50%);
  z-index: 1;
}
.collapse-field-button:hover {
  color: #144583;
}
.collapse-field-content {
  border-top: 1px solid #ebebeb;
  display: none;
  padding: 10px 0;
}
.collapse-field-content.active {
  display: block;
  background: rgba(133, 144, 166, 0.08);
}
</style>
