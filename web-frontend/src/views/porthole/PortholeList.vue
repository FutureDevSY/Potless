<template>
  <div>
    <div class="filter">
      <!-- 필터 -->
      <div class="horizontal-layout">
        <Calendar @update:dateRange="handleDateRangeUpdate" />
        <Select
          :options="['심각', '주의', '양호']"
          defaultText="위험성"
          @update:selected="handleSeverity"
        />
        <Select
          :options="['포트홀', '도로파손']"
          defaultText="종류"
          @update:selected="handleType"
        />
        <Select
          :options="['작업전', '작업완료']"
          defaultText="작업 상태"
          @update:selected="handleStatus"
        />
        <Input @update:value="handleInputValue" />
        <button class="search-button" @click="takeData(0)">검색</button>
      </div>
    </div>

    <div class="container">
      <div class="left">
        <List :current-data="currentData" />
        <Pagination :total-page="totalPage" />
      </div>

      <div class="right">
        <PotholeLocationMap
          :pothole-dirx="pothole_info.dirX"
          :pothole-diry="pothole_info.dirY"
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
import { ref } from "vue";
import { getPotholeList } from "../../api/pothole/pothole.js";
import { useAuthStore } from "@/stores/user";
import Pagination from "./components/Pagination.vue";
import { storeToRefs } from "pinia";

const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const currentData = ref(null);
const totalPage = ref(null);

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
        console.log(res.data.data.content);
        console.log(res.data.data.totalPages);
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
  selectedSeverity.value = option;
};

// 파손 종류
const selectedType = ref("");
const handleType = (option) => {
  selectedType.value = option;
};

// 현재 상황
const selectedStatus = ref("");
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

const pothole_info = ref({
  pothole_id: 1,
  severity: 2,
  type: "포트홀",
  status: 1,
  width: 305,
  address: "대전 광역시 동구 계족로 282",
  dirX: 36.3549777,
  dirY: 127.2983403,
  create_at: "2023-01-04 14:22:33",
});
</script>

<style scoped>
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

.horizontal-layout {
  display: flex;
  align-items: center;
  justify-content: space-between;
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
  position: fixed;
  bottom: 5vh;
  left: 0;
  width: calc(100% * 0.563);
  text-align: center;
  padding: 10px 0;
  box-shadow: 0px -2px 5px rgba(0, 0, 0, 0.1);
  background-color: #f1f1f1;
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
</style>
