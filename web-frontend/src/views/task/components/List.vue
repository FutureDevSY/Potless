<template>
  <div class="list-overflow">
    <table>
      <thead>
        <tr>
          <th class="detect-column">탐지 일시</th>
          <th>위험성</th>
          <th>종류</th>
          <th>행정동</th>
          <th>도로명</th>
          <th>너비</th>
          <th>사진</th>
          <th>작업 상태</th>
          <th>삭제</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="porthole in portholes"
          :key="porthole.id"
          @dblclick="store.movePortholeDetail(porthole.damageId)"
        >
          <td>
            <div>{{ porthole.createAt.split(" ")[0] }}</div>
            <div>{{ porthole.createAt.split(" ")[1] }}</div>
          </td>
          <td :class="dangerClass(porthole.severity)">
            {{ porthole.severity }}
          </td>
          <td>{{ porthole.dtype }}</td>
          <td>{{ porthole.area }}</td>
          <td>{{ porthole.roadName }}</td>
          <td>{{ porthole.width }}</td>
          <td>
            <button
              class="button list-button"
              @click="openModal(porthole.imgUrl)"
            >
              사진 보기
            </button>
          </td>
          <td>{{ porthole.status }}</td>
          <td><button @click="removePorthole(index)">삭제하기</button></td>
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
import { ref, watch, onMounted } from "vue";
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
</script>

<style scoped>
.serious {
  background-color: #f5172d;
  color: white;
}

.cautious {
  background-color: #ffb800;
  color: white;
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

.detect-column {
  width: 150px;
  min-width: 150px;
  text-align: center;
  white-space: nowrap;
}

.list-overflow {
  overflow-y: auto;
  max-height: 83vh;
  margin-right: 8px;
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
</style>
