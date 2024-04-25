<template>
  <div class="task-info-container">
    <div class="search-tab">
      <Calendar />
      <Select />
      <Input />
      <button class="search-button" @click="search">검색</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>작업 보고서</th>
          <th>작업 갯수</th>
          <th>작업 관리자</th>
          <th>작업 예정 일자</th>
          <th>등록 시점</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="task in currentTasks"
          :key="task.id"
          @click="store.moveTaskDetail(task.id)"
        >
          <td>{{ task.serviceSubCategory }}</td>
          <td>{{ task.projectEnd }} 건</td>
          <td>{{ task.sinceConstruction }}</td>
          <td>{{ task.workOrder }}</td>
          <td>{{ task.registrationTime }}</td>
        </tr>
      </tbody>
    </table>
    <Pagenation @update:current-page="setCurrentPage" :totalpage="totalPage" />
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import Calendar from "./components/Calendar.vue";
import Select from "./components/Select.vue";
import Input from "./components/Input.vue";
import Pagenation from "./components/Pagenation.vue";
import data from "./TaskData.json";
import { useMoveStore } from "../../stores/move.js";

const store = useMoveStore();
const currentPage = ref(1);
function setCurrentPage(page) {
  currentPage.value = page;
}
const taskData = data;
const totalPage = Object.keys(data).length;

const currentTasks = computed(() => {
  return taskData[currentPage.value] || [];
});
</script>

<style scoped>
.task-info-container {
  overflow-x: auto;
}

.search-tab {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 10px;
  padding: 10px;
}

.search-button {
  padding: 5px 10px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  height: 37.78px;
}

.search-button:hover {
  background-color: #e1e1e1;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

th,
td {
  border: 1px solid #ddd;
  text-align: left;
  padding: 8px;
}

thead {
  background-color: #f9f9f9;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #ddd;
  cursor: pointer;
}
</style>
