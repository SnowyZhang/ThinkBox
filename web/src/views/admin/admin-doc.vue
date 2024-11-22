<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查询
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="levelTree.length > 0"
              :columns="columns"
              :row-key="(record:any) => record.id"
              :data-source="levelTree"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template v-slot:headerCell="{ column }">
              <template v-if="column.dataIndex === 'name'">
                名称
              </template>
              <template v-else-if="column.key === 'action'">
                Action
              </template>
            </template>

            <template v-slot:bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'name'">
                {{ record.sort }} {{ record.name }}
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    编辑
                  </a-button>
                  <a-popconfirm
                      title="删除后不可恢复，确认删除?"
                      ok-text="是"
                      cancel-text="否"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="primary" danger size="small">
                      删除
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                v-model:value="doc.parentId"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeSelectData"
                placeholder="请选择父文档"
                tree-default-expand-all
                :fieldNames="{label: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.priority" placeholder="优先级"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="editor-content-view" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref, createVNode } from 'vue';
  import axios from 'axios';
  import {message, Modal} from 'ant-design-vue';
  import {Tool} from "@/util/tool";
  import {useRoute} from "vue-router";
  import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
  import E from 'wangeditor'

  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const route = useRoute();
      const param = ref();
      param.value = {};
      const docs = ref();
      const loading = ref(false);
      // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
      const treeSelectData = ref();
      treeSelectData.value = [];

      const columns = [
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: 'Action',
          key: 'action',
          // slots: { customRender: 'action' }
        }
      ];

      /**
       * 一级文档树，children属性就是二级文档
       * [{
       *   id: "",
       *   name: "",
       *   children: [{
       *     id: "",
       *     name: "",
       *   }]
       * }]
       */
      const levelTree = ref(); // 一级文档树，children属性就是二级文档
      levelTree.value = [];

      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        levelTree.value = [];
        axios.post("/doc/all/" + route.query.ebookId).then((response) => {
          loading.value = false;
          const data = response.data;
          if (data.success) {
            docs.value = data.content;
            console.log("原始数组：", docs.value);

            levelTree.value = [];
            levelTree.value = Tool.array2Tree(docs.value, "0");
            console.log("树形结构：", levelTree);

            // 父节点下拉框初始化，希望一打开界面就有数据，而不是点击新增时才有数据
            treeSelectData.value = Tool.copy(levelTree.value) || [];
            // 为选择树添加一个"无"
            treeSelectData.value.unshift({id: 0, name: '无'});
          } else {
            message.error(data.message);
          }
        });
      };

      // -------- 表单 ---------
      const doc = ref();
      doc.value = {
        ebookId: route.query.ebookId
      };
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      // const editor = new E('#content');
      // editor.config.zIndex = 0;
      let editor: any;

      const handleSave = () => {
        modalLoading.value = true;
        doc.value.content = editor.txt.html();
        axios.post("/doc/save", doc.value).then((response) => {
          modalLoading.value = false;
          const data = response.data; // data = commonResp
          if (data.success) {
            // modalVisible.value = false;
            message.success("保存成功！");

            // 重新加载列表
            handleQuery();
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * 将某节点及其子孙节点全部置为disabled
       */
      const setDisabled = (data: any, id: string) => {
        data.forEach((item: any) => {
          if(item.id === id){
            item.disabled = true;
            const children = item.children;
            if(Tool.isNotEmpty(children)){
              children.forEach((citem: any) => {
                setDisabled(children,citem.id);
              });
            }
          }else{
            const children = item.children;
            if(Tool.isNotEmpty(children)){
              setDisabled(children,id);
            }
          }

        });
      };

      const deleteIds: Array<string> = [];
      const deleteNames: Array<string> = [];
      /**
       * 查找整根树枝
       */
      const getDeleteIds = (data: any, id: string) => {
        data.forEach((item: any) => {
          if(item.id === id){
            deleteIds.push(item.id);
            deleteNames.push(item.name);
            const children = item.children;
            if(Tool.isNotEmpty(children)){
              children.forEach((citem: any) => {
                getDeleteIds(children,citem.id);
              });
            }
          }else{
            const children = item.children;
            if(Tool.isNotEmpty(children)){
              getDeleteIds(children,id);
            }
          }

        });
      };

      /**
       * 内容查询
       **/
      const handleQueryContent = () => {
        axios.get("/doc/content/" + doc.value.id).then((response) => {
          const data = response.data;
          if (data.success) {
            editor.txt.html(data.content)
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * 编辑
       */
      const edit = (record: any) => {
        // 清空富文本框
        editor.txt.html("");
        modalVisible.value = true;
        doc.value = Tool.copy(record);
        handleQueryContent();

        // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
        treeSelectData.value = Tool.copy(levelTree.value);
        setDisabled(treeSelectData.value, record.id);

        // 为选择树添加一个"无"
        treeSelectData.value.unshift({id: "0", name: '无'});
      };

      /**
       * 新增
       */
      const add = () => {
        // 清空富文本框
        editor.txt.html("");
        modalVisible.value = true;
        doc.value = {
          ebookId: route.query.ebookId
        };

        treeSelectData.value = Tool.copy(levelTree.value) || [];

        // 为选择树添加一个"无"
        treeSelectData.value.unshift({id: "0", name: '无'});
      };

      const handleDelete = (id: string) => {
        // console.log(levelTree, levelTree.value, id)
        // 清空数组，否则多次删除时，数组会一直增加
        deleteIds.length = 0;
        deleteNames.length = 0;
        getDeleteIds(levelTree.value, id);
        Modal.confirm({
          title: '重要提醒',
          icon: createVNode(ExclamationCircleOutlined),
          content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
          onOk() {
            // console.log(ids)
            axios.post("/doc/delete/" + deleteIds.join(",")).then((response) => {
              const data = response.data; // data = commonResp
              if (data.success) {
                // 重新加载列表
                handleQuery();
              } else {
                message.error(data.message);
              }
            });
          },
        });
      };

      // ----------------富文本预览--------------
      const drawerVisible = ref(false);
      const previewHtml = ref();
      const handlePreviewContent = () => {
        const html = editor.txt.html();
        previewHtml.value = html;
        drawerVisible.value = true;
      };
      const onDrawerClose = () => {
        drawerVisible.value = false;
      };

      onMounted(() => {
        handleQuery();
        editor = new E('#content');
        editor.config.zIndex = 0;
        editor.create();
        // editor.create();
      });

      return {
        param,
        // docs,
        levelTree,
        columns,
        loading,
        handleQuery,

        edit,
        add,

        doc,
        modalVisible,
        modalLoading,
        handleSave,

        handleDelete,

        treeSelectData,

        drawerVisible,
        previewHtml,
        handlePreviewContent,
        onDrawerClose,
      }
    }
  });
</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>
