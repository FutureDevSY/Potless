<template>
  <div class="team-navigation">
    <span class="titled">보수 공사팀</span>
    <TeamCarusel :teams="currentTeams" />
    <button class="list-button" @click="openModal('team')">수정/추가</button>
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
        console.log(res.data.message);
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
  bottom: 4%;
  padding: 1vh 0.5vw;
  font-size: 1.4vh;
  cursor: pointer;
  border: none;
  background-color: #ffffff;
  border-radius: 8px;
  color: #4f58b5;
  border: 1px solid #4f58b5;
  transition: all 0.3s;
  justify-self: end;
}

.list-button:hover {
  background-color: #e6e6f6;
}

.team-navigation {
  height: 100%;
  display: grid;
  grid-template-rows: 6% 94%;
}

.titled {
  color: #373737;
  font-size: 2.2vh;
  margin-top: 1vh;
  font-weight: bold;
  /* margin: 0.5vh 0 0vh 0; */
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.chevron-left {
  position: relative;
  width: 2.5vh;
  height: 2.5vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-left:after {
  content: "";
  position: absolute;
  top: 40%;
  left: 50%;
  border-style: solid;
  border-color: #575757;
  border-width: 0 5px 5px 0;
  display: block;
  padding: 8px;
  transform: translate(-50%, -50%) rotate(135deg);
  transition: transform 0.3s ease;
  border-radius: 3px;
}

.chevron-left:hover:after {
  transform: translate(-70%, -50%) rotate(135deg);
}
.chevron-right {
  position: relative;
  width: 2.5vh;
  height: 2.5vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-right:after {
  content: "";
  position: absolute;
  top: 40%;
  left: 50%;
  border-style: solid;
  border-color: #575757;
  border-width: 0 5px 5px 0;
  display: block;
  padding: 8px;
  transform: translate(-50%, -50%) rotate(-45deg);
  transition: transform 0.3s ease;
  border-radius: 3px;
}

.chevron-right:hover:after {
  transform: translate(-30%, -50%) rotate(-45deg);
}
.chevron-left:disabled:after,
.chevron-right:disabled:after {
  border-color: #ccc;
  cursor: default;
}

.chevron-left:disabled,
.chevron-right:disabled {
  cursor: default;
}
</style>
