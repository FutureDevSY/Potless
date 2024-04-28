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
        <div class="person">{{ team.teamList }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import data from "./teamData.json";
import AddTeam from "./AddTeam.vue";

const datas = ref(Object.values(data));
const isAddingTeam = ref(false);

const props = defineProps({
  toggleModal: Function,
});

function updateIsAddingTeam(value) {
  isAddingTeam.value = value;
}

function handleAddTeam(newTeamData) {
  datas.value.push({
    teamName: newTeamData.teamName,
    teamList: newTeamData.teamList,
  });
}
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
