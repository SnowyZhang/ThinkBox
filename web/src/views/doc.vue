<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-row :gutter="24">
        <a-col :span="6">
          <a-tree
              v-if="levelTree.length > 0"
            :treeData="levelTree"
            :defaultExpandAll="true"
            @select="onSelect"
            :field-names="{title: 'name', key: 'id'}"
          />
        </a-col>
        <a-col :span="18">

        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { UserOutlined, MailOutlined } from "@ant-design/icons-vue";
import axios from 'axios';
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const levelTree = ref(); // 一级文档树，children属性就是二级文档
    levelTree.value = [];
    const docs = ref();
    const route = useRoute();

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      levelTree.value = [];
      axios.post("/doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始数组：", docs.value);

          levelTree.value = [];
          levelTree.value = Tool.array2Tree(docs.value, "0");
          console.log("树形结构：", levelTree);

          // 父节点下拉框初始化，相当于点击新增

        } else {
          message.error(data.message);
        }
      });
    };
    onMounted(() => {
      handleQuery();
    });

    return {
      levelTree,
    };
  }
});

</script>