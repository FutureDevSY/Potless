<template>
  <!-- 리스트 -->
  <div class="list-overflow">
    <table>
      <thead>
        <tr>
          <th class="work-column">
            <button
              class="button"
              :disabled="!hasSelected"
              @click="openModal('add')"
            >
              작업추가
            </button>
          </th>
          <th class="detect-column">탐지 일시</th>
          <th>위험성</th>
          <th>종류</th>
          <th>행정동</th>
          <th>도로명</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="porthole in portholes"
          :key="porthole.id"
          @click="toggleSelect(porthole)"
          @dblclick="store.movePortholeDetail(porthole.id)"
        >
          <td>{{ porthole.isSelected ? "✔️" : "" }}</td>
          <td>
            <div>{{ porthole.detect.split(" ")[0] }}</div>
            <!-- 날짜 -->
            <div>{{ porthole.detect.split(" ")[1] }}</div>
            <!-- 시간 -->
          </td>
          <td :class="dangerClass(porthole.danger)">{{ porthole.danger }}</td>
          <td>{{ porthole.type }}</td>
          <td>{{ porthole.city }}</td>
          <td>{{ porthole.road }}</td>
        </tr>
      </tbody>
    </table>
  </div>

  <button class="button list-button" @click="openModal('list')">
    작업 지시서 리스트
  </button>
  <div v-if="isModalOpen" class="modal">
    <div class="modal-content">
      <TaskList
        :is-adding-tasks="modalMode === 'add'"
        :toggle-modal="toggleModal"
        :selected-count="selectedCount"
      />
    </div>
  </div>

  <Pagination @update:current-page="setCurrentPage" :totalpage="totalPage" />
</template>

<script setup>
import { ref, computed } from "vue";
import { useMoveStore } from "../../../stores/move";
import Pagination from "./Pagination.vue";
import TaskList from "./TaskList.vue";
import data from "./dummyData.json";

// 더미 데이터
const store = useMoveStore();
const dummyData = ref(data);
const portholes = computed(() => {
  return (dummyData.value[currentPage.value] || []).map((item) => ({
    ...item,
    isSelected: selectedIds.value.has(item.id),
  }));
});

// 위험성 필터링
const dangerClass = (danger) => {
  switch (danger) {
    case "심각":
      return "serious";
    case "주의":
      return "cautious";
    case "양호":
      return "safe";
    default:
      return "";
  }
};

// 선택
const selectedIds = ref(new Set());
function toggleSelect(porthole) {
  if (selectedIds.value.has(porthole.id)) {
    selectedIds.value.delete(porthole.id);
  } else {
    selectedIds.value.add(porthole.id);
  }
  selectedIds.value = new Set(selectedIds.value);
}
const selectedCount = computed(() => selectedIds.value.size);

// 작업 추가 버튼
const hasSelected = computed(() => {
  return selectedIds.value.size > 0;
});

// 작업 리스트 모달창
const isModalOpen = ref(false);
const modalMode = ref("");
function toggleModal() {
  isModalOpen.value = !isModalOpen.value;
  if (!isModalOpen.value) {
    selectedIds.value.clear(); // 모달 닫힐 때 선택 초기화
  }
}
function openModal(mode) {
  modalMode.value = mode;
  toggleModal();
}

// 페이지네이션
const currentPage = ref(1);
function setCurrentPage(page) {
  currentPage.value = page;
}
const totalPage = Object.keys(dummyData).length;
const props = defineProps({
  itemData: Object,
});
</script>

<style scoped>
.serious {
  background-color: #f5172d;
  color: white;
}

.cautious {
  background-color: #ffb800;
  color: rgb(255, 255, 255);
}

.safe {
  background-color: #047c02;
  color: white;
}

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

.work-column {
  width: 70px;
  min-width: 50px;
  text-align: center;
  white-space: nowrap;
}

.detect-column {
  width: 150px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

.modal {
  position: fixed;
  z-index: 100;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: white;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 40vw;
  height: 50vh;
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

.list-button {
  margin: 1vh 0px 1vh 0px;
}

.button:hover {
  background-color: #e1e1e1;
}

.new_button {
  margin-bottom: 30px;
}

.task-container {
  width: 100%;
}

.list-overflow {
  overflow-y: auto;
  max-height: 65vh;
  margin-right: 8px;
}

.list-overflow::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.list-overflow::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.list-overflow::-webkit-scrollbar-thumb {
  background-color: darkgrey;
  border-radius: 10px;
  border: 2px solid transparent;
}

.list-overflow::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}
</style>
