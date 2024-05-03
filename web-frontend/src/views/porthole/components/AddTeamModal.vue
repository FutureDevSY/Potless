<template>
  <span class="close" @click="toggleModal">&times;</span>
  <button
    class="new-team-btn"
    @click="isAddingTeam = true"
    v-if="!isAddingTeam"
  >
    새로운 팀 추가
  </button>
  <div class="add-team-modal">
    <div v-if="isAddingTeam">
      <AddTeam
        v-if="isAddingTeam"
        :is-adding-team="isAddingTeam"
        @update:isAddingTeam="updateIsAddingTeam"
      />
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

onMounted(() => {
  takeTeamList();
});
</script>

<style scoped>
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
.add-team-modal {
}

.new-team-btn {
  margin-top: 15px;
}
</style>
