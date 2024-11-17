<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          :openKeys="openKeys"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
<!--          <router-link to="/"></router-link>-->
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in levelTree" :key="item.id" :disabled="false">
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item key="tip" :disabled="true">
          <span>以上菜单在分类管理配置</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="showWelcome">
        <h1>欢迎</h1>
        <p>欢迎使用本系统</p>
      </div>
      <a-list v-show="!showWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :pagination="pagination" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item :key="item.name">
            <template #actions>
              <span v-for="{ icon, text } in actions" :key="icon">
                <component :is="icon" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { UserOutlined, MailOutlined } from "@ant-design/icons-vue";
import axios from 'axios';
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

export default defineComponent({
  name: 'Home',
  setup() {
    const openKeys = ref([]);
    const levelTree = ref();
    let categorys: any;
    const ebooks = ref([]);
    const pagination = ref({
      current: 1,
      pageSize: 100,
      total: 0
    });
    let category2Id = "0";
    const showWelcome = ref(true);

    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          openKeys.value = categorys.map((category: any) => category.id);
          console.log('openkeys:', openKeys)
          levelTree.value = Tool.array2Tree(categorys, "0");
        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryEbooks =()=>{
      axios.get("/ebook/list", {
        params: {
          page: pagination.value.current,
          size: pagination.value.pageSize,
          category2Id: category2Id
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
        pagination.value.total = data.content.total;
        // pagination.value.current = data.content.page;
      });
    }

    const handleClick = (e: any) => {
      console.log('click ', e);
      if (e.key === 'welcome') {
        showWelcome.value = true;
      } else {
        showWelcome.value = false;
        category2Id = e.key;
        handleQueryEbooks();
      }
    };

    onMounted(() => {
      // showWelcome.value = true;
      handleQueryCategory();

    });


    return {
      ebooks,
      pagination,
      openKeys,
      levelTree,
      showWelcome,
      handleClick
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>