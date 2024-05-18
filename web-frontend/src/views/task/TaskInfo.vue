<template>
  <div class="task-info-container">
    <div class="search-tab">
      <Calendar @update:dateRange="handleDateRangeUpdate" />
      <Select @statusSelected="handleStatus" />
      <Input @update:value="handleInputValue" />
      <button class="search-button" @click="takeData(0)">검색</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>작업 보고서</th>
          <th>작업 갯수</th>
          <th>작업 관리자</th>
          <th>작업 예정 일자</th>
          <th>등록 시점</th>
          <th class="delete-column"></th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="task in currentData"
          :key="task.projectId"
          @click="store.moveTaskDetail(task.projectId)"
        >
          <td>{{ task.projectName }}</td>
          <td>{{ task.projectSize }} 건</td>
          <td>{{ task.managerName }}</td>
          <td>{{ task.projectDate }}</td>
          <td class="detect-column">
            <div>{{ formatDated(task.createdDate) }}</div>
            <div>{{ formatTimed(task.createdDate) }}</div>
          </td>
          <td class="delete-div">
            <button class="delete-btn" @click.stop="deleteTask(task.projectId)">
              <img class="delete-img" src="../../assets/icon/delete.png" alt="delete" />
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <Pagenation
      :total-page="totalPage"
      @update:current-page="handleCurrentPageUpdate"
      :page-info="currentPage"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import Calendar from "./components/Calendar.vue";
import Select from "./components/Select.vue";
import Input from "./components/Input.vue";
import Pagenation from "./components/Pagenation.vue";
import { useMoveStore } from "../../stores/move.js";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTaskList } from "../../api/task/taskList";
import { deleteTaskDetail } from "../../api/task/taskDetail";
import { useSwal } from "@/composables/useSwal";

const router = useRouter();
const route = useRoute();
const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken, areaName } = storeToRefs(store2);
const currentData = ref(null);
const totalPage = ref(null);
const swal = useSwal();

// 상태 검색
const selectedStatus = ref("작업전");
const handleStatus = (option) => {
  selectedStatus.value = option;
};

// 날짜 검색
const formatDate = (date) => {
  if (!date) return null;
  return date.toISOString().split("T")[0];
};
const dateRange = ref({ start: null, end: null });

const handleDateRangeUpdate = (newRange) => {
  dateRange.value = newRange;
};

// 날짜 형식을 YYYY-MM-DD 로 변경
function formatDated(dateTime) {
  const date = new Date(dateTime);
  return date.toISOString().split("T")[0];
}

// 시간 형식을 HH:MM:SS 로 변경
function formatTimed(dateTime) {
  const time = new Date(dateTime);
  return time.toTimeString().split(" ")[0];
}

// 쿼리 검색
const inputValue = ref("");
const handleInputValue = (value) => {
  inputValue.value = value;
};

// 페이지네이션
const currentPage = ref(1);
function setCurrentPage(page) {
  currentPage.value = page;
}

const showAlert = () => {
  swal({
    title: "해당 프로젝트가 삭제되었습니다.",
    icon: "success",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const showAlert2 = () => {
  swal({
    title: "프로젝트에 이미 작업 중인 도로 파손 데이터가 있습니다.",
    icon: "error",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const handleCurrentPageUpdate = (newPage) => {
  setCurrentPage(newPage);
  takeData(newPage);
  router.replace({ query: { ...route.query, page: newPage } });
};

// 작업 지시서 삭제
const deleteTask = (projectId) => {
  deleteTaskDetail(
    accessToken.value,
    projectId,
    (res) => {
      if (res.data.status == "SUCCESS") {
        // console.log(res.data.message);
        takeData(0);
        showAlert();
      } else {
        showAlert2();
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

// 작업지시서 리스트 조회
const takeData = (currentPage) => {
  const rawParams = {
    areaId: store2.areaId,
    start: formatDate(dateRange.value.start),
    end: formatDate(dateRange.value.end),
    status: selectedStatus.value,
    word: inputValue.value,
    page: currentPage,
    size: 10,
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(([key, value]) => value !== "" && value != null)
  );

  getTaskList(
    accessToken.value,
    queryParams,
    (res) => {
      if (res.data.status == "SUCCESS") {
        // console.log(res.data.message);
        currentData.value = res.data.data.content;
        totalPage.value = res.data.data.totalPages;
      } else {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

onMounted(() => {
  const page = parseInt(route.query.page) || 0;
  takeData(page);
});
</script>

<style scoped>
.task-info-container {
  animation: fadein 1.5s;
}
@keyframes fadein {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.search-tab {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 10px;
  padding: 1.5vh 34px 1.5vh 10px;
}

.search-button {
  padding: 0.9vh 20px;
  background-color: #6d6d6d;
  border: none;
  color: white;
  font-size: 1.7vh;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #8c8c8c;
}

table {
  width: 96%;
  margin: 0 auto;
  border-collapse: collapse;
  table-layout: fixed;
  color: #373737;
  border: 2px solid #dfdfdf;
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
  padding: 1vh;
  font-size: 1.8vh;
  color: #373737;
}

thead {
  background-color: #f9f9f9;
}

tbody tr {
  background-color: #ffffff;
}

tbody tr:hover {
  background-color: #ececec98;
  cursor: pointer;
}

.delete-column {
  width: 60px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

.delete-div {
  padding: 0px;
}

.delete-img {
  width: auto;
  height: 30px;
  cursor: pointer;
}

.delete-btn {
  border-radius: 100px;
  margin-right: 20px;
  border: none;
  background-color: white;
  padding: 8px 10px;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #fbd6d8;
}
</style>
