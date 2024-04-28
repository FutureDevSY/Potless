<template>
  <div class="task-detail-container">
    <div class="header">
      <div>{{ taskHeader.projectId }}</div>
      <div>{{ taskHeader.projectName }}</div>
      <div>담당자 : {{ taskHeader.managerName }}</div>
      <div>작업 건수: {{ taskHeader.projectSize }} 건</div>
      <div>작업 일자: {{ taskHeader.projectDate }}</div>
      <button>최적의 경로 찾기</button>
      <button>PDF로 변환하기</button>
    </div>
    <List :data="taskData" />
  </div>
</template>

<script setup>
import { ref } from "vue";
import List from "./components/List.vue";
import data from "./DummyData.json";

const taskHeader = ref(data.projectResponse);
const taskData = ref(data.projectDetailResponse);
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background-color: #f0f0f0;
}
</style>
