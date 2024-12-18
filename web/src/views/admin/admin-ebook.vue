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
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #headerCell="{ column }">
          <template v-if="column.dataIndex === 'cover'">
            封面
          </template>
          <template v-else-if="column.dataIndex === 'name'">
            名称
          </template>
          <template v-else-if="column.key === 'category'">
            分类
          </template>
<!--          <template v-else-if="column.dataIndex === 'category1Id'">-->
<!--            分类1-->
<!--          </template>-->
<!--          <template v-else-if="column.dataIndex === 'category2Id'">-->
<!--            分类2-->
<!--          </template>-->
          <template v-else-if="column.dataIndex === 'docCount'">
            文档数
          </template>
          <template v-else-if="column.dataIndex === 'viewCount'">
            阅读数
          </template>
          <template v-else-if="column.dataIndex === 'voteCount'">
            点赞数
          </template>
          <template v-else-if="column.key === 'action'">
            Action
          </template>
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'cover'">
            <img v-if="record.cover" :src="record.cover" alt="avatar" />
          </template>
          <template v-else-if="column.dataIndex === 'name'">
            {{ record.name }}
          </template>
          <template v-else-if="column.key === 'category'">
            <span> {{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }} </span>
          </template>
<!--          <template v-else-if="column.dataIndex === 'category1Id'">-->
<!--            {{ record.category1Id }}-->
<!--          </template>-->
<!--          <template v-else-if="column.dataIndex === 'category2Id'">-->
<!--            {{ record.category2Id }}-->
<!--          </template>-->
          <template v-else-if="column.dataIndex === 'docCount'">
            {{ record.docCount }}
          </template>
          <template v-else-if="column.dataIndex === 'viewCount'">
            {{ record.viewCount }}
          </template>
          <template v-else-if="column.dataIndex === 'voteCount'">
            {{ record.voteCount }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <router-link :to="'/admin/doc?ebookId=' + record.id">
                <a-button type="primary">
                  文档管理
                </a-button>
              </router-link>
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
      title="电子书表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="levelTree"
        />
      </a-form-item>
<!--      <a-form-item label="分类1">-->
<!--        <a-input v-model:value="ebook.category1Id" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="分类2">-->
<!--        <a-input v-model:value="ebook.category2Id" />-->
<!--      </a-form-item>-->
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
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
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref([]);
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        key: 'category'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action'
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      axios.get("/ebook/list", {
        params: {
          page: params.page, //从参数params中取值
          size: params.size, //从参数params中取值
          name: param.value.name //从响应式变量param中取值
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          console.log("Ebooks data:", ebooks.value);

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("查看自带的分页参数：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
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
      ebook.value = Tool.copy(record);
      /*
      *如果我不copy，而是直接赋值，那么当我修改表单数据，然后点击取消.
      * 再点击编辑，查看表单数据时,会发现表单数据已经被修改了.
      * 这是因为直接赋值，是引用赋值，所以修改表单数据，会直接修改原始数据
      * 而copy是深拷贝，所以修改表单数据，不会影响原始数据
      * */
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };



    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    const handleDelete = (id: string) => {
      axios.post("/ebook/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const levelTree =  ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          levelTree.value = [];
          levelTree.value = Tool.array2Tree(categorys, "0");
          console.log("树形结构：", levelTree.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: string) => {
      console.log("cid",cid)
      let result = "";
      categorys.forEach((item: any) => {
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
      // handleQuery({ page: pagination.value.current, size: pagination.value.pageSize });
      handleQueryCategory();
    });

    return {
      handleQueryCategory,
      param,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      getCategoryName,

      edit,
      add,

      ebook,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,
      levelTree,

      handleDelete
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