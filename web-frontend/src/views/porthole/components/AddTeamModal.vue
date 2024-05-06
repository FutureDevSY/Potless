<template>
  <div class="modal-header">
    <span v-if="isAddingTeam" @click="goBack" class="back">&larr;</span>
    <span v-else class="placeholder"></span>
    <span class="close" @click="toggleModal">&times;</span>
  </div>
  <button class="new-team-btn" @click="isAddingTeam = true" v-if="!isAddingTeam">
    새로운 팀 추가
  </button>
  <div class="add-team-modal">
    <div class="content-container">
      <div v-if="isAddingTeam">
        <AddTeam :is-adding-team="isAddingTeam" @update:isAddingTeam="updateIsAddingTeam" />
      </div>
      <div v-else>
        <div class="team-list" v-for="team in datas">
          <div class="team-name">{{ team.teamName }}</div>
          <div class="person" v-for="worker in team.workerList">
            작업자 번호: {{ worker.memberId }} / 작업자 이름:
            {{ worker.workerName }}
          </div>
          <button @click="deleteTeamEvent(team.teamId)">팀 삭제</button>
          <hr />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import AddTeam from "./AddTeam.vue";
import { useAuthStore } from "@/stores/user";
import { getTeamList, deleteTeam } from "../../../api/team/team";
import { storeToRefs } from "pinia";

const store2 = useAuthStore();
const { accessToken, areaName } = storeToRefs(store2);
const datas = ref(null);
const isAddingTeam = ref(false);
const props = defineProps({
  toggleModal: Function,
});

const takeTeamList = () => {
  getTeamList(
    accessToken.value,
    areaName.value,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        datas.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const deleteTeamEvent = (projectId) => {
  deleteTeam(
    accessToken.value,
    projectId,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        takeTeamList();
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

function updateIsAddingTeam(value) {
  isAddingTeam.value = value;
  takeTeamList();
}

const goBack = () => {
  isAddingTeam.value = false;
};

onMounted(() => {
  takeTeamList();
});
</script>

<style scoped>
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 2vh 0vh 0.5vh 0vh;
}

.back,
.close,
.placeholder {
  color: #aaa;
  font-size: 4.5vh;
  font-weight: bold;
  margin: -1vh 0vh;
  cursor: pointer;
}

.placeholder {
  visibility: hidden;
}

.close:hover,
.back:hover {
  color: #7d7d7d;
  text-decoration: none;
  cursor: pointer;
}

.add-team-modal {
  margin: 1vh 0px;
  height: 57vh;
  /* overflow-y: auto; */
  /* border: 1px solid black; */
}

.team-list {
}

.new-team-btn {
  background-color: #151c62;
  padding: 0.9vh 0.7vw;
  cursor: pointer;
  border-radius: 4px;
  font-size: 1.55vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 2vh;
}

.new-team-btn:hover {
  background-color: #0e1241;
}
</style>
