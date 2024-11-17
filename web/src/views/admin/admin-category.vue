<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({})">
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
          :columns="columns"
          :row-key="(record:any) => record.id"
          :data-source="levelTree"
          :pagination="false"
          :loading="loading"
          :defaultExpandAllRows="true"
      >
        <template #headerCell="{ column }">
          <template v-if="column.dataIndex === 'name'">
            名称
          </template>
<!--          <template v-else-if="column.dataIndex === 'id'">-->
<!--            分类id-->
<!--          </template>-->
          <template v-else-if="column.key === 'parentCategory'">
            父分类
          </template>
          <template v-else-if="column.dataIndex === 'priority'">
            优先级
          </template>
          <template v-else-if="column.key === 'action'">
            Action
          </template>
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'name'">
            {{ record.name }}
          </template>
<!--          <template v-else-if="column.dataIndex === 'id'">-->
<!--            {{ record.id }}-->
<!--          </template>-->
          <template v-else-if="column.key === 'parentCategory'">
            <span> {{ getCategoryName(record.parentId) }} </span>
          </template>
          <template v-else-if="column.dataIndex === 'priority'">
            {{ record.priority }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="primary" danger>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="分类表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
<!--      <a-form-item label="id">-->
<!--        <a-input v-model:value="category.id" />-->
<!--      </a-form-item>-->
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parentId"
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for = "c in levelTree" :key="c.id" :value="c.id" :disabled="category.id===c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="优先级">
        <a-input v-model:value="category.priority" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      // {
      //   title: '父分类id',
      //   key: 'parentId',
      //   dataIndex: 'parentId'
      // },
      {
        title: '父分类',
        key: 'parentCategory',
        // dataIndex: 'parentId'
      },
      {
        title: '优先级',
        dataIndex: 'priority'
      },
      {
        title: 'Action',
        key: 'action',
        // slots: { customRender: 'action' }
      }
    ];
    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const levelTree = ref(); // 一级分类树，children属性就是二级分类
    levelTree.value = [];
    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      levelTree.value = [];
      axios.get("/category/all", {
        params: {
          name: param.value.name // 从响应式变量 param 中取值
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
          console.log("原始分类数组", categorys.value);

          levelTree.value = [];
          levelTree.value = Tool.array2Tree(categorys.value, "0");
          console.log("分类树", levelTree);
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const category = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      // category.value.category1Id = categoryIds.value[0];
      // category.value.category2Id = categoryIds.value[1];
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({});
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
      /*
      *如果我不copy，而是直接赋值，那么当我修改表单数据，然后点击取消.
      * 再点击编辑，查看表单数据时,会发现表单数据已经被修改了.
      * 这是因为直接赋值，是引用赋值，所以修改表单数据，会直接修改原始数据
      * 而copy是深拷贝，所以修改表单数据，不会影响原始数据
      * */
      categoryIds.value = [category.value.category1Id, category.value.category2Id]
    };



    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    const handleDelete = (id: string) => {
      axios.post("/category/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({});
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: string) => {
      console.log("cid",cid)
      let result = "";
      if(cid==="0"){
        result = "无";
        return result;
      }
      categorys.value.forEach((item: any) => {
        //查看item.if类型
        console.log("item.id",item.id)
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
          console.log("result",result)
        }
      });
      return result;
    };


    onMounted(() => {
      handleQuery({});
    });

    return {
      param,
      categorys,
      levelTree,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,

      handleDelete,
      getCategoryName
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