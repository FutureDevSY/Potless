<template>
  <div class="main-box">
    <div class="left-box">
      <div class="top-box">
        <Weather />
        <CurrentStats />
      </div>
      <div class="bottom-box">
        <div class="report-container">
          <ReportList />
        </div>
        <div class="map-containers">
          <SVG1 v-if="areaId === 1"></SVG1>
          <SVG2 v-if="areaId === 2"></SVG2>
          <SVG3 v-if="areaId === 3"></SVG3>
          <SVG4 v-if="areaId === 4"></SVG4>
          <SVG5 v-if="areaId === 5"></SVG5>
        </div>
      </div>
    </div>
    <div class="right-box">
      <div class="worker-container">
        <div>보수 공사팀</div>
        <button class="button team-button" @click="openModal('team')">작업팀</button>
        <div v-if="isModalOpen && modalMode === 'team'" class="modal">
          <div class="modal-content">
            <TeamModal :toggle-modal="toggleModal" />
          </div>
        </div>
        <Team></Team>
      </div>
    </div>
  </div>
</template>

<script setup>
import Weather from "./components/Weather.vue";
import CurrentStats from "./components/CurrentStats.vue";
import ReportList from "./components/TaskList.vue";
import SVG1 from "./components/SVG1.vue";
import SVG2 from "./components/SVG2.vue";
import SVG3 from "./components/SVG3.vue";
import SVG4 from "./components/SVG4.vue";
import SVG5 from "./components/SVG5.vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { ref } from "vue";
import Team from "./components/Team.vue";
import TeamModal from "../porthole/components/AddTeamModal.vue";

const store = useAuthStore();
const { areaId } = storeToRefs(store);

const isModalOpen = ref(false);
const modalMode = ref("");
function toggleModal() {
  isModalOpen.value = !isModalOpen.value;
  if (!isModalOpen.value) {
    selectedIds.value.clear();
    emit("refreshData");
  }
}
function openModal(mode) {
  modalMode.value = mode;
  toggleModal();
}
</script>

<style scoped>
.main-box {
  display: grid;
  grid-template-columns: 72.2% 27%;
  gap: 0.8%;
  margin: 1%;
}
.left-box {
  display: grid;
  grid-template-rows: 23% 74.5%;
  gap: 2%;
}
.right-box {
  background-color: #f2f2f2a3;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.top-box {
  display: grid;
  grid-template-columns: 34% 64.5%;
  gap: 1.5%;
}
.bottom-box {
  display: grid;
  grid-template-columns: 54% 45%;
  gap: 1%;
}
.report-container {
  background-color: rgb(241, 241, 241);
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.map-containers {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
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
  margin: 16vh auto;
  padding: 0vh 1.8vw 6vh 1.8vw;
  border: 1px solid #dddddda1;
  width: 40vw;
  height: 65vh;
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
</style>
