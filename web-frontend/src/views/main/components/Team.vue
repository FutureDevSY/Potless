<template>
  <div class="team-navigation">
    <span class="titled">보수 공사팀</span>
    <TeamCarusel :teams="currentTeams" @open-modal="openModal" />
    <!-- <button class="list-button" @click="openModal('team')">공사팀 추가</button> -->
  </div>
  <div v-if="isModalOpen && modalMode === 'team'" class="modal">
    <div class="modal-content">
      <TeamModal :toggle-modal="toggleModal" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTeamList } from "../../../api/team/team";
import TeamCarusel from "./TeamCarusel.vue";
import TeamModal from "../../porthole/components/AddTeamModal.vue";

const store = useAuthStore();
const { accessToken, areaName } = storeToRefs(store);
const currentData = ref([]);
const currentTeams = computed(() => {
  return currentData.value || [];
});

const isModalOpen = ref(false);
const modalMode = ref("");
function toggleModal() {
  isModalOpen.value = !isModalOpen.value;
  if (!isModalOpen.value) {
    takeData();
  }
}
function openModal(mode) {
  modalMode.value = mode;
  toggleModal();
}

const takeData = () => {
  getTeamList(
    accessToken.value,
    areaName.value,
    (res) => {
      if (res.data.status === "SUCCESS") {
        currentData.value = res.data.data;
      } else {
        // console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

onMounted(() => {
  takeData();
});
</script>

<style scoped>
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
  border-radius: 10px;
  height: 65vh;
}

.new_button {
  margin-bottom: 30px;
}

.list-button {
  position: absolute;
  bottom: 5%;
  right: 2.5%;
  padding: 1vh 0.3vw;
  font-size: 1.3vh;
  cursor: pointer;
  border: none;
  background-color: #f6f6f6;
  border-radius: 8px;
  color: #6b6b6b;
  border: 2px solid #c5c5c5;
  transition: all 0.3s;
  justify-self: end;
}

.list-button:hover {
  background-color: #e0e0e0;
}

.team-navigation {
  height: 100%;
  display: grid;
  grid-template-rows: 6% 94%;
}

.titled {
  color: #1e476d;
  font-size: 2.4vh;
  margin-top: 1.5vh;
  font-weight: bold;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  border-bottom: 1px solid #bcbcbc;
}
</style>
