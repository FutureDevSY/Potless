<template>
  <!-- 리스트 -->
  <div class="list-overflow">
    <table>
      <thead>
        <tr>
          <th class="work-column">
            <button class="add-button" :disabled="!hasSelected" @click="openModal('add')">
              작업추가
            </button>
          </th>
          <!-- <th class="detect-column">탐지 일시</th> -->
          <th class="danger-column">위험성</th>
          <th class="type-column">종류</th>
          <th class="city-column">행정동</th>
          <th class="address-column">지번 주소</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="pothole in potholes"
          :key="pothole.id"
          @mouseover="updateMapLocation(pothole.dirX, pothole.dirY)"
          @click="handleRowClick(pothole)"
        >
          <td class="select-column" @click.stop="toggleSelect(pothole)">
            <div class="checkbox">
              <div v-if="pothole.isSelected" class="checkmark"></div>
            </div>
          </td>
          <!-- <td>
            <div>{{ porthole.detect.split(" ")[0] }}</div>
          </td> -->
          <td class="dangers-column">
            <div class="danger-type" :class="dangerClass(pothole.severity)">
              <p>{{ pothole.severity }}</p>
            </div>
          </td>
          <td>{{ pothole.dtype }}</td>
          <td>{{ pothole.location }}</td>
          <td>{{ pothole.address }}</td>
        </tr>
      </tbody>
    </table>
  </div>

  <button class="button list-button" @click="openModal('list')">작업 지시서 리스트</button>
  <div v-if="isModalOpen && (modalMode === 'list' || modalMode === 'add')" class="modal">
    <div class="modal-content">
      <TaskList
        :is-adding-tasks="modalMode === 'add'"
        :toggle-modal="toggleModal"
        :selected-Ids="selectedIds"
      />
    </div>
  </div>
  <button class="button team-button" @click="openModal('team')">팀 추가</button>
  <div v-if="isModalOpen && modalMode === 'team'" class="modal">
    <div class="modal-content">
      <TeamModal :toggle-modal="toggleModal" />
    </div>
  </div>

  <!-- <Pagination @update:current-page="setCurrentPage" :totalpage="totalPage" /> -->
</template>

<script setup>
import { ref, computed } from "vue";
import { useMoveStore } from "../../../stores/move";
import TaskList from "./TaskList.vue";
import TeamModal from "./AddTeamModal.vue";
import { postPothole } from "../../../api/task/taskDetail";

const store = useMoveStore();
const props = defineProps({
  currentData: Object,
});



const potholes = computed(() => {
  return (props.currentData || []).map((item) => ({
    ...item,
    isSelected: selectedIds.value.has(item.id),
  }));
});

function handleRowClick(pothole) {
  if (!pothole.isSelected) {
    store.movePortholeDetail(pothole.id);
  }
}

// 위험성 필터링
const dangerClass = (danger) => {
  switch (danger) {
    case 3:
      return "serious";
    case 2:
      return "cautious";
    case 1:
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

const hasSelected = computed(() => {
  return selectedIds.value.size > 0;
});

// 작업 리스트 모달창
const isModalOpen = ref(false);
const modalMode = ref("");
function toggleModal() {
  isModalOpen.value = !isModalOpen.value;
  if (!isModalOpen.value) {
    selectedIds.value.clear();
  }
}
function openModal(mode) {
  modalMode.value = mode;
  toggleModal();
}

const emit = defineEmits(["updateMapLocation"]);
const updateMapLocation = (dirX, dirY) => {
  emit("updateMapLocation", { dirX, dirY });
};
</script>

<style scoped>
/* .select-column {
} */

.checkbox {
  width: 2.8vh;
  height: 2.8vh;
  border: 2px solid #ccc;
  background: white;
  display: inline-block;
  z-index: -2;
  position: relative;
}

.checkmark {
  width: 2.2vh;
  height: 1vh;
  z-index: -1;
  position: relative;
  border-bottom: 4px solid #151c62;
  border-left: 4px solid #151c62;
  transform: translateX(9px) translateY(8px) rotate(-45deg);
  transform-origin: left bottom;
}

p {
  margin: -3px;
}

.danger-type {
  display: inline-block;
  width: 35px;
  height: 35px;
  border-radius: 100%;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  line-height: 42px;
  background-color: inherit;
}

.serious {
  background-color: #f5172d;
}

.cautious {
  background-color: #ffb700bf;
}

.safe {
  background-color: #008a1e75;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

/* th,
td {
  border: 1px solid #ddd;
  text-align: center;
  padding: 15px;
} */

/* thead {
  background-color: #f9f9f9;
} */

td {
  border-top: 1px solid #dddddda1;
  border-bottom: 1px solid #dddddda1;
  border-left: none;
  border-right: none;
  text-align: center;
  padding: 1.7vh;
  font-size: 1.9vh;
  color: #373737;
}

thead th {
  position: sticky;
  top: 0;
  background-color: #d3d5ed;
  z-index: 0;
  padding: 1vh 1vh;
  font-size: 1.7vh;
  color: #6c6c6c;
}

tbody tr:hover {
  background-color: #dddddd44;
  cursor: pointer;
}

.work-column {
  width: 5.4vw;
  text-align: center;
  white-space: nowrap;
}

/* .detect-column {
  width: 150px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
} */

.danger-column {
  width: 5vw;
  text-align: center;
  white-space: nowrap;
}

.type-column {
  width: 10vw;
  text-align: center;
  white-space: nowrap;
}

.city-column {
  width: 10vw;
  text-align: center;
  white-space: nowrap;
}

/* .address-column {
  width: 10vw;
  text-align: center;
  white-space: nowrap;
} */

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

.add-button:disabled {
  color: #a0a0a0;
  cursor: default;
  background-color: #f0f0f0;
  border-color: #d0d0d0;
}

.button {
  padding: 5px 15px;
  height: 4.4vh;
  font-size: 1.55vh;
  cursor: pointer;
  border: none;
  background-color: #f8f8f8;
  border-radius: 5px;
  color: #373737;
  border: 1px solid #acacac;
  transition: background-color 0.3s;
}

.button:hover {
  background-color: #d8d8d8;
}

.list-button {
  margin-top: 1.3vh;
  margin-left: 30px;
}

.team-button {
  margin-top: 1.3vh;
  margin-left: 8px;
}

.button:disabled:hover {
  background-color: #f0f0f0;
}

.new_button {
  margin-bottom: 30px;
}

.task-container {
  width: 100%;
}

.list-overflow {
  overflow-y: auto;
  height: 62vh;
  margin-right: 6px;
  margin-left: 30px;
  border: 2px solid rgb(223, 223, 223);
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
