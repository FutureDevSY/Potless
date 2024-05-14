<template>
  <div class="list-overflow">
    <table class="table-container">
      <thead>
        <tr>
          <th>탐지 일시</th>
          <th>위험성</th>
          <th>종류</th>
          <th>행정동</th>
          <th class="address-column">지번</th>
          <th>너비(mm)</th>
          <th>상세 정보</th>
          <th>작업 상태</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="porthole in portholes"
          :key="porthole.id"
          @click="store.movePortholeDetail(porthole.damageId)"
        >
          <td class="detect-column">
            <div>{{ formatDate(porthole.createdDateTime) }}</div>
            <div>{{ formatTime(porthole.createdDateTime) }}</div>
          </td>
          <td class="danger-column">
            <div class="danger-type" :class="dangerClass(porthole.severity)">
              {{ dangerClass2(porthole.severity) }}
            </div>
          </td>
          <td>{{ dtypeDisplay(porthole.dtype) }}</td>
          <td>{{ porthole.location }}</td>
          <td>{{ porthole.address }}</td>
          <td>{{ porthole.width }}</td>
          <td>
            <button class="list-button" @click="openModal(porthole.imgUrl)">
              확인하기
            </button>
          </td>
          <td>{{ porthole.status }}</td>
          <td class="delete-column">
            <button class="delete-btn">
              <img
                class="delete-img"
                src="../../../assets/icon/delete.png"
                alt="delete"
              />
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <div v-if="isModalOpen" class="modal">
    <div class="modal-content">
      <CaruselModal :toggle-modal="toggleModal" :img-info="imgURL" />
    </div>
  </div>
</template>

<script setup>
import { useMoveStore } from "../../../stores/move";
import { ref } from "vue";
import CaruselModal from "../components/CaruselModal.vue";

const store = useMoveStore();
const imgURL = ref(null);

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

const dangerClass2 = (danger) => {
  switch (danger) {
    case 3:
      return "심각";
    case 2:
      return "주의";
    case 1:
      return "양호";
    default:
      return "";
  }
};

const dtypeDisplay = (dtype) => {
  switch (dtype) {
    case "POTHOLE":
      return "포트홀";
    case "CRACK":
      return "도로균열";
    case "WORNOUT":
      return "도로마모";
    default:
      return "알 수 없는 유형";
  }
};

const props = defineProps({
  data: Object,
});
const portholes = props.data;
// 작업 리스트 모달창
const isModalOpen = ref(false);
function toggleModal() {
  isModalOpen.value = !isModalOpen.value;
  if (!isModalOpen.value) {
  }
}
function openModal(img) {
  imgURL.value = img;
  toggleModal();
}

// 날짜 형식을 YYYY-MM-DD 로 변경
function formatDate(dateTime) {
  const date = new Date(dateTime);
  return date.toISOString().split("T")[0];
}

// 시간 형식을 HH:MM:SS 로 변경
function formatTime(dateTime) {
  const time = new Date(dateTime);
  return time.toTimeString().split(" ")[0];
}
</script>

<style scoped>
.danger-column {
  text-align: center;
  vertical-align: middle;
  padding: 20px 0px;
}

.danger-type {
  display: inline-block;
  width: 42px;
  height: 42px;
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

td {
  border-top: 1px solid #dddddda1;
  border-bottom: 1px solid #dddddda1;
  border-left: none;
  border-right: none;
  text-align: center;
  padding: 22px;
  font-size: 18px;
  color: #373737;
}

thead th:last-child {
  width: 50px;
}

thead th {
  position: sticky;
  top: 0;
  background-color: #d3d5ed;
  z-index: 10;
  padding: 13px 10px;
  font-size: 16px;
  color: #6c6c6c;
}

tbody tr:hover {
  background-color: #dddddd44;
  cursor: pointer;
}

.detect-column {
  font-size: 16px;
}

.delete-column {
  padding: 0px;
}

.address-column {
  width: 16vw;
  text-align: center;
  white-space: nowrap;
}

.list-button {
  font-size: 15px;
  padding: 10px 14px;
  cursor: pointer;
  border: none;
  background-color: #ffffff;
  border-radius: 8px;
  color: #4f58b5;
  border: 1px solid #4f58b5;
  transition: all 0.3s;
}

.list-button:hover {
  background-color: #e6e6f6;
}

.list-overflow {
  overflow-y: auto;
  max-height: 68vh;
  padding-right: 4px;
  margin-bottom: 50px;
}

.list-overflow::-webkit-scrollbar {
  width: 8px;
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
  position: fixed;
  background-color: white;
  margin: 10% auto;
  padding: 20px;
  border: 1px solid #888;
  left: 25%;
  top: -10%;
  width: 50vw !important;
  height: 60vh !important;
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
