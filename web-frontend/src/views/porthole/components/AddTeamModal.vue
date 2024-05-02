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
        @addTeam="handleAddTeam"
      />
    </div>
    <div v-else>
      <div class="team-list" v-for="team in datas">
        <div class="team-name">{{ team.teamName }}</div>
        <div class="person">{{ team.workerList }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import AddTeam from "./AddTeam.vue";
import { useAuthStore } from "@/stores/user";
import { getTeamList } from "../../../api/team/team";
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

function updateIsAddingTeam(value) {
  isAddingTeam.value = value;
}

function handleAddTeam(newTeamData) {
  datas.value.push({
    teamName: newTeamData.teamName,
    teamList: newTeamData.teamList,
  });
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
