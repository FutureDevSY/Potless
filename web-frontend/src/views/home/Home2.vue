<template>
  <body>
    <div class="main-layout">
      <Navbar2 />
      <div class="task-info-container"></div>
    </div>
  </body>
</template>

<script setup>
import Navbar2 from "../common/Navbar2.vue";
import List from "../porthole/components/List.vue";
import Select from "../porthole/components/Select.vue";
import Input from "../porthole/components/Input.vue";
import Calendar from "../porthole/components/Calendar.vue";
import PotholeLocationMap from "../porthole/components/PotholeLocationMap.vue";
import { ref, onMounted } from "vue";
import { getPotholeList } from "../../api/pothole/pothole.js";
import { useAuthStore } from "@/stores/user";
import { useMoveStore } from "@/stores/move";
import Pagination from "../porthole/components/Pagination.vue";
import { storeToRefs } from "pinia";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const currentData = ref(null);
const totalPage = ref(null);
const severityMap = {
  양호: 1,
  주의: 2,
  심각: 3,
};

const takeData = (currentPage) => {
  const rawParams = {
    start: formatDate(dateRange.value.start),
    end: formatDate(dateRange.value.end),
    type: selectedType.value,
    status: selectedStatus.value,
    severity: selectedSeverity.value,
    area: store2.areaName,
    searchWord: inputValue.value,
    page: currentPage,
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(([key, value]) => value !== "" && value != null)
  );

  getPotholeList(
    accessToken.value,
    queryParams,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        currentData.value = res.data.data.content;
        totalPage.value = res.data.data.totalPages;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const formatDate = (date) => {
  if (!date) return null;
  return date.toISOString().split("T")[0];
};

// 날짜
const dateRange = ref({ start: null, end: null });

const handleDateRangeUpdate = (newRange) => {
  dateRange.value = newRange;
};

// 심각도
const selectedSeverity = ref("");
const handleSeverity = (option) => {
  selectedSeverity.value = severityMap[option];
};

// 파손 종류
const selectedType = ref("");
const handleType = (option) => {
  selectedType.value = option;
};

// 현재 상황
const selectedStatus = ref("작업전");
const handleStatus = (option) => {
  selectedStatus.value = option;
};

// 검색어
const inputValue = ref("");
const handleInputValue = (value) => {
  inputValue.value = value;
};

// 페이지네이션
const currentPage = ref(1);
function setCurrentPage(page) {
  currentPage.value = page;
}

const handleCurrentPageUpdate = (newPage) => {
  setCurrentPage(newPage);
  takeData(newPage);
};

const potholeInfo = ref({
  dirX: 127.2983403,
  dirY: 36.3549777,
});

const handleMapUpdate = ({ dirX, dirY }) => {
  potholeInfo.value.dirX = dirX;
  potholeInfo.value.dirY = dirY;
};

onMounted(() => {
  takeData();
});
</script>

<style scoped>
body {
  font-family: "Nunito", sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #d3d5ed;
  background-repeat: no-repeat;
  background-size: cover;
  /* background: #212140; */
}

.main-layout {
  display: grid;
  grid-template-columns: 12% 88%;
  width: 100%;
  margin: 25px;
  background: rgb(254, 254, 254);
  box-shadow: 0 0.5px 0 1px rgba(255, 255, 255, 0.23) inset,
    0 1px 0 0 rgba(255, 255, 255, 0.66) inset, 0 4px 16px rgba(0, 0, 0, 0.12);
  border-radius: 15px;
  z-index: 10;
  display: grid;
}

.container {
  display: flex;
  width: 100%;
}

.filter {
  width: 97vw;
  padding: 10px;
  text-align: center;
}

.left {
  flex: 8;
}

.right {
  flex: 6;
}

.search-tab {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 10px;
}

#pagination-control {
  display: flex;
  align-items: center;
}

.control-buttons {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}

.number-and-buttons {
  display: flex;
  align-items: center;
  border: 1px solid black;
}

.number-and-buttons > div:first-child {
  width: 30px;
  text-align: center;
}

.triangle-up,
.triangle-down {
  width: 0;
  height: 0;
  cursor: pointer;
}

.triangle-up {
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-bottom: 15px solid black;
  margin-bottom: 1px;
}

.triangle-down {
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 15px solid black;
  margin-top: 1px;
}

#risk {
  width: 200px;
  height: auto;
  overflow: auto;
}

.multi-select {
  width: 11%;
}

.type {
  width: 8%;
}

.status {
  width: 7%;
}

.search {
  width: 20%;
}

.header {
  position: fixed;
  display: flex;
  width: 55.8vw;
  padding: 10px 0;
  background-color: #f1f1f1;
}
.header-item {
  display: flex;
  align-items: center;
  padding: 0 20px;
  margin-right: 10px;
}

/* 페이지네이션 */
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: -0.82vh;
  /* position: fixed;
  bottom: 2vh;
  width: calc(100% * 0.563);
  text-align: center; */
}

.search-button {
  padding: 1.2vh 15px;
  background-color: #151c62;
  border: none;
  color: white;
  font-size: 1.8vh;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0e1241;
}

.register {
  margin-right: 670px;
}
</style>
