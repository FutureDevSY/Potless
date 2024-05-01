<template>
  <span class="close" @click="toggleModal">&times;</span>
  <div v-if="!isDetailOpen">
    <button class="button new_button" @click="addNewTask">새 작업</button>
    <table>
      <tr v-for="task in currentTasks" :key="task.id" @click="showDetail(task)">
        <td>{{ task.id }}</td>
        <td>{{ task.serviceSubCategory }}</td>
        <td>{{ task.projectEnd }} 건</td>
        <td
          class="add-column"
          v-if="isAddingTasks"
          @click.stop="incrementProjectEnd(task, $event)"
        >
          추가✅
        </td>
      </tr>
    </table>
  </div>
  <div v-if="isDetailOpen">
    <div class="task-info">
      <div class="info-font">{{ selectedTask.id }}</div>
      <div class="info-font">{{ selectedTask.serviceSubCategory }}</div>
      <div class="manager">관리자: {{ selectedTask.sinceConstruction }}</div>
    </div>
    <div class="detail-list">
      <TaskDetail :task="selectedTask" @close="isDetailOpen = false" />
    </div>
    <div class="detail-controls">
      <button class="button back" @click="closeDetail">뒤로가기</button>
      <select v-model="selectedTeamId" class="team-select">
        <option v-for="(team, id) in teamlist" :key="id" :value="id">
          {{ team.teamName }}
        </option>
      </select>
      <button class="button save-button" @click="saveDetail">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from "vue";
import TaskDetail from "./TaskDetail.vue";
import Teamlist from "./teamData.json";
import { postTaskCreate } from "../../../api/task/taskDetail";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";

const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);

const props = defineProps({
  isAddingTasks: Boolean,
  toggleModal: Function,
  selectedCount: Number,
});

const teamlist = ref(Teamlist);
const selectedTeamId = ref(null);
// 모달창 작업 지시서 리스트 + 더미데이터
const taskData = reactive({
  1: [
    {
      id: 113,
      serviceSubCategory: "도로 부속 작업 보고서",
      projectEnd: 10,
      sinceConstruction: "정휘원",
      workOrder: "2024-04-23",
      registrationTime: "2024-04-21",
    },
    {
      id: 114,
      serviceSubCategory: "도로 부속 작업 보고서",
      projectEnd: 10,
      sinceConstruction: "정휘원",
      workOrder: "2024-04-23",
      registrationTime: "2024-04-21",
    },
  ],
});
const currentPage2 = ref(1);
const currentTasks = computed(() => {
  return taskData[currentPage2.value] || [];
});

// 새 작업(작업 지시서 새로 만들기)
function addNewTask() {
  const taskData = ref({
    managerId: 0,
    teamId: null,
    title: "도로 부속 작업 보고서",
    projectDate: null,
    areaId: store2.areaId,
    damageNums: [],
  });

  // postTaskCreate(
  //   accessToken.value,
  //   potholeId,
  //   (res) => {
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       pothole_info.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  // taskData[currentPage2.value].push(newTask);
}

// 작업 보고서 디테일
const selectedTask = ref(null);
const isDetailOpen = ref(false);

function showDetail(task) {
  selectedTask.value = task;
  isDetailOpen.value = true;
}

function closeDetail() {
  isDetailOpen.value = false;
}

function saveDetail() {
  isDetailOpen.value = false;
}

// 작업 보고서 추가
function incrementProjectEnd(task, event) {
  event.stopPropagation();
  task.projectEnd += props.selectedCount;
}
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

th,
td {
  border: 1px solid #ddd;
  text-align: center;
  padding: 15px;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #ddd;
  cursor: pointer;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.button {
  padding: 5px 10px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  height: 37.78px;
}

.button:hover {
  background-color: #e1e1e1;
}

.new_button {
  margin-bottom: 30px;
}

.detail-list {
  overflow-y: auto;
  max-height: 35vh;
  padding-right: 10px;
}

.detail-list::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.detail-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.detail-list::-webkit-scrollbar-thumb {
  background-color: darkgrey;
  border-radius: 10px;
  border: 2px solid transparent;
}

.detail-list::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}

.task-info {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-bottom: 25px;
}

.info-font {
  font-size: 24px;
  font-weight: bold;
}

.manager {
  font-size: 16px;
}

.add-column {
  width: 100px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

.team-select {
  margin-left: 15px;
  padding: 5px 10px;
}
.detail-controls {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}

.save-button {
  margin-left: auto;
}
</style>
