<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h2 v-if="levelTree.length === 0">暂无文档</h2>
      <a-row >
        <a-col :span="6">
          <a-tree
              v-if="levelTree.length > 0"
            :treeData="levelTree"
            :defaultExpandAll="true"
            @select="onSelect"
            :field-names="{title: 'name', key: 'id'}"
              :defaultSelectedKeys="defaultSelectedKeys"
          />
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>阅读数：{{doc.viewCount}}</span> &nbsp; &nbsp;
              <span>点赞数：{{doc.voteCount}}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="editor-content-view" v-html="contentHtml"> </div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon><LikeOutlined /> &nbsp;点赞：{{doc.voteCount}} </template>
            </a-button>
          </div>
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
import {createEditor} from "@wangeditor/editor";

export default defineComponent({
  name: 'Doc',
  setup() {
    const levelTree = ref(); // 一级文档树，children属性就是二级文档
    levelTree.value = [];
    const docs = ref();
    const route = useRoute();
    const contentHtml = ref("");
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];
    const doc = ref();
    doc.value = { }
    // const editor = createEditor({
    //   html: '<p>hello <strong>world</strong></p>', // 从 editor.getHtml() 获取的 html 内容
    //   // 其他属性...
    // })

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

          if(Tool.isNotEmpty(levelTree.value)){
            defaultSelectedKeys.value = [levelTree.value[0].id];
            handleQueryContent(levelTree.value[0].id);
            doc.value = levelTree.value[0];
            console.log("doc:", doc.value);
          }

        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryContent = (id:string) => {
      axios.get("/doc/content/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          contentHtml.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = async (selectedKeys: any, info: any) => {
      console.log("selected", selectedKeys, info);

      if (Tool.isNotEmpty(selectedKeys)) {
        const selectedId = selectedKeys[0]; // 获取选中的文档 ID
        console.log("selectedKeys[0]:", selectedId);

        // 加载文档内容
        await handleQueryContent(selectedId);

        // 从树的节点信息中更新文档详情
        const selectedNode = info.node; // 获取选中的节点对象
        if (selectedNode) {
          doc.value = {
            id: selectedNode.id,
            name: selectedNode.name,
            viewCount: selectedNode.viewCount || 0,
            voteCount: selectedNode.voteCount || 0,
          };
          console.log("Updated doc:", doc.value);
        } else {
          console.warn("未能获取选中的节点信息，文档详情未更新。");
        }
      }
    };

    const vote = () => {
      if (!doc.value.id) {
        message.warn("请选择一个文档后再点赞！");
        return;
      }

      axios
          .get('/doc/vote/' + doc.value.id)
          .then((response) => {
            console.log("点赞ID：", doc.value.id);
            const data = response.data;
            if (data.success) {
              doc.value.voteCount++;
              message.success("点赞成功！");
            } else {
              message.error(data.message);
            }
          })
          .catch((error) => {
            console.error("点赞请求失败：", error);
            message.error("点赞失败，请稍后重试！");
          });
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      levelTree,
      contentHtml,
      onSelect,
      defaultSelectedKeys,
      doc,
      vote
    };
  }
});

</script>

<style>
.vote-div {
  padding: 15px;
  text-align: center;
}
.editor-content-view {
  border: 0px solid #ccc;
  border-radius: 5px;
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
}

.editor-content-view p,
.editor-content-view li {
  white-space: pre-wrap; /* 保留空格 */
}

.editor-content-view blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.editor-content-view code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.editor-content-view pre>code {
  display: block;
  padding: 10px;
}

.editor-content-view table {
  border-collapse: collapse;
}
.editor-content-view td,
.editor-content-view th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.editor-content-view th {
  background-color: #f1f1f1;
}

.editor-content-view ul,
.editor-content-view ol {
  padding-left: 20px;
}

.editor-content-view input[type="checkbox"] {
  margin-right: 5px;
}
</style>
