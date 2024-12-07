<template>
  <a-layout-footer style="text-align: center">
    ThinkBox © 2024 | 赋能知识，助力成长
    「学习 · 构建 · 分享」
  </a-layout-footer>
</template>
<script lang="ts">
import {computed, defineComponent} from 'vue';
import {useRef} from "preact/compat";
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup () {
    const user = computed(() => store.state.user);
    return { user };
  }
});
</script>