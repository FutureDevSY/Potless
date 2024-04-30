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
        <tr v-for="task in currentTasks" :key="task.id" @dblclick="store.moveTaskDetail(task.id)">
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
.search-tab {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 10px;
  padding: 1.5vh 34px 1.5vh 10px;
}

.search-button {
  padding: 10px 15px;
  background-color: #151c62;
  border: none;
  color: white;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0e1241;
}

table {
  width: 96%;
  margin: 0 auto;
  border-collapse: collapse;
  table-layout: fixed;
  color: #373737;
}

th,
td {
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

th {
  cursor: default;
  padding: 1.5vh;
  font-size: 2vh;
  color: #6c6c6c;
  background-color: #d3d5ed;
}

td {
  padding: 1.5vh;
  font-size: 1.8vh;
  color: #373737;
}

thead {
  background-color: #f9f9f9;
}

tbody tr:hover {
  background-color: #dddddd44;
  cursor: pointer;
}
</style>
