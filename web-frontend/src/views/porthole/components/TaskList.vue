<template>
  <div class="modal-header">
    <span v-if="isDetailOpen" @click="closeDetail" class="back">&larr;</span>
    <span v-if="!isDetailOpen" class="placeholder"></span>
    <span class="close" @click="toggleModal">&times;</span>
  </div>
  <div v-if="!isDetailOpen">
    <button class="primary-btn new-work-btn" @click="addNewTask">
      새 작업
    </button>
    <div class="work-list-container">
      <table>
        <tr v-for="task in currentTasks" :key="task.id">
          <td class="cursor" @click="showDetail(task)">
            No. {{ task.projectId }}
          </td>
          <td class="cursor name-col" @click="showDetail(task)">
            {{ task.projectName }}
          </td>
          <td class="cursor" @click="showDetail(task)">
            {{ task.projectDate }}
          </td>
          <td class="cursor" @click="showDetail(task)">
            {{ task.teamName }}
          </td>
          <td class="cursor" @click="showDetail(task)">
            {{ task.projectSize }} 건
          </td>
          <td class="add-column" v-if="isAddingTasks">
            <button
              class="add-button"
              @click.stop="assignPothole(task.projectId)"
            >
              작업추가
            </button>
          </td>
        </tr>
      </table>
    </div>
  </div>
  <div v-if="isDetailOpen">
    <div class="info-details">
      <div class="info-font">
        No.{{ selectedTask.projectId }} {{ selectedTask.projectName }}
      </div>
      <div class="manager-section">
        <div class="manager">관리자: {{ selectedTask.managerName }}</div>
        <div class="manager">{{ selectedTask.projectDate }}</div>
      </div>
    </div>
    <div class="detail-list">
      <TaskDetail
        :task="propData"
        @close="isDetailOpen = false"
        @updateDetail="showDetailAgain"
      />
    </div>
    <div class="detail-controls">
      <div class="control-right">
        <select v-model="selectedTeamId" class="team-select">
          <option value="null" disabled>작업팀 배정</option>
          <option
            class="teams"
            v-for="(team, id) in teamList"
            :key="id"
            :value="team.teamId"
          >
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
import {
  postTaskCreate,
  postPothole,
  getTaskDetail,
  postTeam,
} from "../../../api/task/taskDetail";
import { getTaskList } from "../../../api/task/taskList";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTeamList } from "../../../api/team/team";
import { useSwal } from "../../../composables/useSwal";

const store2 = useAuthStore();
const { accessToken, areaName, coordinates } = storeToRefs(store2);
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

const swal = useSwal();
const selectedTeamId = ref(null);
const taskData = ref([]);
const currentTasks = computed(() => {
  return taskData.value || [];
});
// 자식으로 부터 이벤트 송신
const showDetailAgain = () => {
  showDetail(selectedTask.value);
};

const showAlert = (text) => {
  swal({
    title: text,
    icon: "success",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const showAlert2 = () => {
  swal({
    title: "중복된 도로 파손 데이터 입니다.",
    icon: "error",
    confirmButtonText: "확인",
    width: "700px",
  });
};

// 새 작업(작업 지시서 새로 만들기)
function addNewTask() {
  const damageIdsArray = Array.from(props.selectedIds);
  const newData = ref({
    teamId: null,
    title: "도로 부속 작업 지시서",
    projectDate: "2024-05-13",
    areaId: store2.areaId,
    damageNums: damageIdsArray,
    origin: coordinates.value,
  });

  postTaskCreate(
    accessToken.value,
    newData.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        taskData.value = res.data.data;
        takeData();
        showAlert("새 작업 지시서가 생성되었습니다.");
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
const propData = ref(null);
function showDetail(task) {
  selectedTask.value = task;
  isDetailOpen.value = true;

  getTaskDetail(
    accessToken.value,
    task.projectId,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        propData.value = res.data.data.damageDetailToProjectDtos;
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

// 디테일 닫기
function closeDetail() {
  isDetailOpen.value = false;
}

// 팀 할당
function saveDetail() {
  isDetailOpen.value = false;
  const assignData = ref({
    projectId: selectedTask.value.projectId,
    teamId: selectedTeamId,
  });

  postTeam(
    accessToken.value,
    assignData.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        takeData();
        showAlert("프로젝트에 팀이 할당되었습니다.");
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

// 작업 보고서에 포트홀 할당
const assignPothole = (taskId) => {
  const damageIdsArray = Array.from(props.selectedIds);
  const potholeData = ref({
    projectId: taskId,
    damageIds: damageIdsArray,
    origin: coordinates.value,
  });

  postPothole(
    accessToken.value,
    potholeData.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        takeData();
        showAlert("프로젝트에 도로 파손이 할당되었습니다.");
        closeDetail();
      } else {
        showAlert2();
      }
    },
    (error) => {
      console.log(error);
    }
  );
};

// 작업 보고서 리스트 조회
const takeData = () => {
  const rawParams = {
    areaId: store2.areaId,
    status: "작업전",
    size: 20,
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
.work-list-container {
  overflow-y: auto;
  height: 55vh;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  border: 1px solid #ddd;
}

th,
td {
  border-left: none; /* 왼쪽 경계 없앰 */
  border-right: none; /* 오른쪽 경계 없앰 */
  border-top: 1px solid #ddd; /* 상단 경계 설정 */
  border-bottom: 1px solid #ddd; /* 하단 경계 설정 */
  text-align: center;
  padding: 2.7vh;
  color: #373737;
}

tr:hover,
tr:nth-child(odd):hover {
  background-color: #e7e9fb;
  /* cursor: pointer; */
}

tr:nth-child(odd) {
  background-color: #f2f2f2;
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

.name-col {
  width: 160px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

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

.cursor {
  cursor: pointer;
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

.add-button {
  padding: 5px 15px;
  font-size: 1.55vh;
  background-color: #f8f8fc;
  border-radius: 5px;
  cursor: pointer;
  height: 4.4vh;
  color: #4f58b5;
  border: 1px solid #4f58b5;
  transition: background-color 0.4s;
}

.add-button:hover {
  background-color: #e6e6f6;
}
</style>
