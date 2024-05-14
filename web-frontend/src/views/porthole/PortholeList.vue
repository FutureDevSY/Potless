<template>
  <div>
    <div class="filter">
      <!-- 필터 -->
      <div class="search-tab">
        <button class="register" @click="store.moveSelfRegister">
          도로파손 추가
        </button>
        <Calendar @update:dateRange="handleDateRangeUpdate" />
        <Select
          :options="['심각', '주의', '양호']"
          defaultText="위험성"
          @update:selected="handleSeverity"
        />
        <Select
          :options="typeOptions.map((option) => option.text)"
          defaultText="종류"
          @update:selected="handleType"
        />
        <Select
          :options="['작업전', '작업중', '작업완료']"
          defaultText="작업 상태"
          @update:selected="handleStatus"
        />
        <Input @update:value="handleInputValue" />
        <button class="search-button" @click="takeData(0)">검색</button>
      </div>
    </div>

    <div class="container">
      <div class="left">
        <List
          :current-data="currentData"
          :selected-status="selectedStatus"
          @updateMapLocation="handleMapUpdate"
          @refreshData="takeData"
        />
        <Pagination
          :total-page="totalPage"
          @update:current-page="handleCurrentPageUpdate"
        />
      </div>

      <div class="right">
        <PotholeLocationMap
          :current-data="currentData"
          :pothole-dirx="potholeInfo.dirY"
          :pothole-diry="potholeInfo.dirX"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import List from "./components/List.vue";
import Select from "./components/Select.vue";
import Input from "./components/Input.vue";
import Calendar from "./components/Calendar.vue";
import PotholeLocationMap from "./components/PotholeLocationMap.vue";
import { ref, onMounted } from "vue";
import { getPotholeList } from "../../api/pothole/pothole.js";
import { useAuthStore } from "@/stores/user";
import { useMoveStore } from "@/stores/move";
import Pagination from "./components/Pagination.vue";
import { storeToRefs } from "pinia";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const currentData = ref([]);
const totalPage = ref(null);
const severityMap = {
  양호: 1,
  주의: 2,
  심각: 3,
};

const typeOptions = [
  { text: "포트홀", value: "POTHOLE" },
  { text: "도로균열", value: "CRACK" },
  { text: "도로마모", value: "WORNOUT" },
];

const takeData = (currentPage) => {
  const rawParams = {
    start: formatDate(dateRange.value.start),
    end: formatDate(dateRange.value.end),
    dtype: selectedType.value,
    status: selectedStatus.value,
    severity: selectedSeverity.value,
    area: store2.areaName,
    searchWord: inputValue.value,
    page: currentPage,
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(
      ([key, value]) => value !== "" && value != null
    )
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
const handleType = (selectedText) => {
  const selectedOption = typeOptions.find(
    (option) => option.text === selectedText
  );
  if (selectedOption) {
    selectedType.value = selectedOption.value;
  }
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
.container {
  margin-top: 20px;
  display: flex;
  width: 100%;
}

.filter {
  margin-top: 30px;
  margin-left: 17px;
  width: 86vw;
  padding: 10px;
  text-align: center;
}

.left {
  flex: 6;
}

.right {
  flex: 5;
}

.search-tab {
  display: grid;
  grid-template-columns: 27% 22% 9% 9% 9% 15% 8.5%;
  align-items: center;
  justify-content: end;
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
  width: 100px;
  font-size: 1.8vh;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0e1241;
}

.register {
  padding: 1.2vh 15px;
  background-color: #151c62;
  border: none;
  color: white;
  width: 160px;
  font-size: 1.8vh;
  border-radius: 4px;
  cursor: pointer;
}
</style>
