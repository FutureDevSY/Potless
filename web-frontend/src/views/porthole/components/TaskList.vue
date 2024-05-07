<template>
  <span class="close" @click="toggleModal">&times;</span>
  <div v-if="!isDetailOpen">
    <button class="button new_button" @click="addNewTask">새 작업</button>
    <table>
      <tr v-for="task in currentTasks" :key="task.id" @click="showDetail(task)">
        <td>No. {{ task.projectId }}</td>
        <td>{{ task.projectName }}</td>
        <td>{{ task.projectDate }}</td>
        <td>{{ task.projectSize }} 건</td>
        <td
          class="add-column"
          v-if="isAddingTasks"
          @click="assignPothole(task.projectId)"
        >
          추가✅
        </td>
      </tr>
    </table>
  </div>
  <div v-if="isDetailOpen">
    <div class="task-info">
      <div class="info-font">{{ selectedTask.projectDate }}</div>
      <div class="info-font">{{ selectedTask.projectName }}</div>
      <div class="manager">관리자: {{ selectedTask.managerName }}</div>
    </div>
    <div class="detail-list">
      <TaskDetail :task="selectedTask" @close="isDetailOpen = false" />
    </div>
    <div class="detail-controls">
      <button class="button back" @click="closeDetail">뒤로가기</button>
      <select v-model="selectedTeamId" class="team-select">
        <option v-for="(team, id) in teamList" :key="id" :value="id">
          {{ team.teamName }}
        </option>
      </select>
      <button class="button save-button" @click="saveDetail">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import TaskDetail from "./TaskDetail.vue";
import Teamlist from "./teamData.json";
import { postTaskCreate, postPothole } from "../../../api/task/taskDetail";
import { getTaskList } from "../../../api/task/taskList";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTeamList } from "../../../api/team/team";

const store2 = useAuthStore();
const { accessToken, areaName } = storeToRefs(store2);
const teamList = ref(null);

const takeTeamList = () => {
  getTeamList(
    accessToken.value,
    areaName.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        teamList.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const props = defineProps({
  isAddingTasks: Boolean,
  toggleModal: Function,
  selectedIds: Array,
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
const assignPothole = (taskId) => {
  const damageIdsArray = Array.from(props.selectedIds);
  const potholeData = ref({
    projectId: taskId,
    damageIds: damageIdsArray,
    origin: {
      x: 36.3556033,
      y: 127.2985515,
    },
  });

  postPothole(
    accessToken.value,
    potholeData.value,
    (res) => {
      console.log(res);
      console.log(accessToken.value);
      console.log(potholeData.value);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
    }
  );
};

const takeData = () => {
  const rawParams = {
    areaId: store2.areaId,
    status: "작업전",
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(
      ([key, value]) => value !== "" && value != null
    )
  );

  getTaskList(
    accessToken.value,
    queryParams,
    (res) => {
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
  takeTeamList();
});
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
