<template>
  <div class="create-team-container">
    <div class="add-team-form">
      <input v-model="newTeam.name" type="text" placeholder="팀 이름" />
      <!-- <div v-for="index in 5" :key="index">
        <input
          v-model="newTeam.members[index - 1]"
          type="text"
          :placeholder="'팀원 ' + index"
        />
      </div> -->
      <div v-for="member in teamMembers">
        {{ member.workerName }}
      </div>
      <button @click="addTeam">팀 추가</button>
    </div>
    <div class="team-list">
      <div v-for="worker in datas">
        <div>작업자 번호: {{ worker.memberId }}</div>
        <div>작업자 이름: {{ worker.workerName }}</div>
        <div>팀 번호: {{ worker.teamId }}</div>
        <button @click="pushTeam(worker)">팀에 넣기</button>
        <hr />
      </div>
      <div>
        <input
          type="text"
          placeholder="새로운 작업자 이름"
          v-model="newworker"
        />
        <button @click="addNewWorker()">작업자 추가</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { getWokerList } from "../../../api/team/team";
import { storeToRefs } from "pinia";

const datas = ref([]);
const teamMembers = ref([]);
const newworker = ref("");
const store2 = useAuthStore();
const { accessToken, areaName } = storeToRefs(store2);
const takeWorkerList = () => {
  getWokerList(
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

const addNewWorker = () => {
  datas.value.push({
    memberId: null,
    workerName: newworker.value,
  });
};

const pushTeam = (workerInfo) => {
  teamMembers.value.push({
    memberId: workerInfo.memberId,
    workerName: workerInfo.workerName,
  });
};

const newTeam = ref({
  name: "",
  members: Array(5).fill(""),
});
const emit = defineEmits(["update:isAddingTeam", "addTeam"]);

function addTeam() {
  // 입력된 새 팀 정보를 이벤트로 전달합니다.
  emit("addTeam", {
    teamName: newTeam.value.name,
    teamList: newTeam.value.members.filter((member) => member),
  });

  // 모달을 닫기 위한 이벤트를 발생시킵니다.
  emit("update:isAddingTeam", false);
}

onMounted(() => {
  takeWorkerList();
});
</script>

<style scoped>
.create-team-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  height: 50vh;
}

.add-team-form {
  border: 1px solid black;
  padding: 20px;
}
.team-list {
  border: 1px solid black;
  padding: 20px;
}

.input-group {
  margin-bottom: 10px;
}

.add-team-btn {
  cursor: pointer;
}
</style>
