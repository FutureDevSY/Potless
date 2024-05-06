<template>
  <div class="modal-header">
    <span v-if="isDetailOpen" @click="closeDetail" class="back">&larr;</span>
    <span v-if="!isDetailOpen" class="placeholder"></span>
    <span class="close" @click="toggleModal">&times;</span>
  </div>
  <div v-if="!isDetailOpen">
    <button class="primary-btn new-work-btn" @click="addNewTask">새 작업</button>
    <div class="work-list-container">
      <table>
        <tr v-for="task in currentTasks" :key="task.id" @click="showDetail(task)">
          <td>No. {{ task.projectId }}</td>
          <td>{{ task.projectName }}</td>
          <td>{{ task.projectDate }}</td>
          <td>{{ task.projectSize }} 건</td>
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
  </div>
  <div v-if="isDetailOpen">
    <div class="info-details">
      <div class="info-font">No.{{ selectedTask.projectId }} {{ selectedTask.projectName }}</div>
      <div class="manager-section">
        <div class="manager">관리자: {{ selectedTask.managerName }}</div>
        <div class="manager">{{ selectedTask.projectDate }}</div>
      </div>
    </div>
    <div class="detail-list">
      <TaskDetail :task="selectedTask" @close="isDetailOpen = false" />
    </div>
    <div class="detail-controls">
      <div class="control-right">
        <select v-model="selectedTeamId" class="team-select">
          <option value="null" disabled>작업팀 배정</option>
          <option class="teams" v-for="(team, id) in teamlist" :key="id" :value="id">
            {{ team.teamName }}
          </option>
        </select>
        <button class="primary-btn save-btn" @click="saveDetail">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import TaskDetail from "./TaskDetail.vue";
import Teamlist from "./teamData.json";
import { postTaskCreate } from "../../../api/task/taskDetail";
import { getTaskList } from "../../../api/task/taskList";
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
const taskData = ref([]);
const currentTasks = computed(() => {
  return taskData.value || [];
});

// 새 작업(작업 지시서 새로 만들기)
function addNewTask() {
  const newData = ref({
    teamId: null,
    title: "도로 부속 작업 보고서",
    projectDate: "2024-05-02",
    areaId: store2.areaId,
    damageNums: [],
  });

  postTaskCreate(
    accessToken.value,
    newData.value,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        console.log(res);
        taskData.value = res.data.data;
        takeData();
      } else {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.data.message);
    }
  );
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

const takeData = () => {
  const rawParams = {
    areaId: store2.areaId,
    status: "작업전",
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(([key, value]) => value !== "" && value != null)
  );

  getTaskList(
    accessToken.value,
    queryParams,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        taskData.value = res.data.data.content;
      } else {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

onMounted(() => {
  takeData();
});
</script>

<style scoped>
.work-list-container {
  overflow-y: auto;
  height: 55vh;
  border-left: 1px solid #ddd;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

th,
td {
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  text-align: center;
  padding: 1.7vh;
  color: #373737;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #d2d5ef;
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

.detail-list {
  overflow-y: auto;
  max-height: 47vh;
}

/* --- */
.task-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #373737;
}

.info-font {
  text-align: center;
  flex-grow: 1;
  margin-left: 120px;
  font-size: 2.5vh;
  font-weight: bold;
  margin-bottom: 1.8vh;
  color: #373737;
}

.info-details {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-bottom: 1.5vh;
  margin-top: 1vh;
}

.manager-section {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.manager {
  font-size: 1.8vh;
  color: #373737;
}
/* --- */

.add-column {
  width: 100px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

.save-button {
  margin-left: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 2vh 0vh 0.5vh 0vh;
}

.back,
.close,
.placeholder {
  color: #aaa;
  font-size: 4.5vh;
  font-weight: bold;
  margin: -1vh 0vh;
  cursor: pointer;
}

.placeholder {
  visibility: hidden;
}

.close:hover,
.back:hover {
  color: #7d7d7d;
  text-decoration: none;
  cursor: pointer;
}

.team-select {
  padding: 1.2vh 1vw;
}

.teams {
  font-size: 1.55vh;
}

.detail-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2vh;
}

.control-right {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.new-work-btn {
  padding: 0.9vh 0.7vw;
  margin-bottom: 1.8vh;
  font-size: 1.55vh;
}

.save-btn {
  padding: 1vh 1vw;
  font-size: 1.65vh;
  margin-left: 0.5vw;
}

.primary-btn {
  background-color: #151c62;
  cursor: pointer;
  border-radius: 4px;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
}

.primary-btn:hover {
  background-color: #0e1241;
}
</style>
