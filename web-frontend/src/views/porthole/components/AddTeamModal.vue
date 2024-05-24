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
        <AddTeam :is-adding-team="isAddingTeam" @teamAdded="handleTeamAdded" />
      </div>
      <div class="list-container" v-else>
        <div class="team-list" v-for="team in datas">
          <div class="team-name">{{ team.teamName }}</div>
          <div class="person" v-for="worker in team.workerList">
            <li>
              작업자 번호: {{ worker.memberId }} / 작업자 이름:
              {{ worker.workerName }}
            </li>
          </div>
          <div class="button-wrapper">
            <button class="delete-btn" @click="deleteTeamEvent(team.teamId)">팀 삭제</button>
          </div>
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
      if (res.data.status == "SUCCESS") {
        // console.log(res.data.message);
        datas.value = res.data.data;
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

const deleteTeamEvent = (projectId) => {
  deleteTeam(
    accessToken.value,
    projectId,
    (res) => {
      if (res.data.status == "SUCCESS") {
        // console.log(res.data.message);
        takeTeamList();
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

function handleTeamAdded(success) {
  if (success) {
    takeTeamList(); // 팀 목록 새로고침
    props.toggleModal(); // 모달 닫기
  }
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

.list-container {
  height: 55vh;
  overflow-y: auto;
}

.team-list {
  background-color: #f0f0f0b0;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.162);
  padding: 2vh 1.2vw 2vh 1.2vw;
  margin-bottom: 1.4vh;
  border-radius: 8px;
}

.team-name {
  font-weight: bold;
  color: #515151;
  font-size: 2vh;
  margin-bottom: 1.4vh;
}

.person {
  color: #373737;
  font-size: 1.8vh;
  margin-bottom: 0.7vh;
}

li {
  margin-left: 2px;
}

li::marker {
  color: #1e476d;
}

.add-team-modal {
  margin: 1vh 0px;
  height: 57vh;
  /* overflow-y: auto; */
  /* border: 1px solid black; */
}

.new-team-btn {
  background-color: #1e476d;
  padding: 0.9vh 0.7vw;
  cursor: pointer;
  border-radius: 4px;
  font-size: 1.55vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-bottom: 1vh;
}

.new-team-btn:hover {
  background-color: #17344f;
}

.button-wrapper {
  text-align: right;
  margin-top: -2vh;
}

.delete-btn {
  font-size: 1.55vh;
  padding: 1vh 1.2vw;
  cursor: pointer;
  border: none;
  background-color: #fef1f1;
  border-radius: 8px;
  color: #cd0404;
  border: 1px solid #cd0404;
  transition: background-color 0.3s;
  margin-right: 10px;
}

.delete-btn:hover {
  background-color: #fccccc;
}
</style>
