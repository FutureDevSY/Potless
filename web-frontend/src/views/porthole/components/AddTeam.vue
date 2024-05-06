<template>
  <div class="create-team-container">
    <div class="add-team-form">
      <div class="input-container">
        <input v-model="newTeamName" type="text" placeholder="팀 이름을 입력해주세요" />
      </div>

      <!-- <div v-for="index in 5" :key="index">
        <input
          v-model="newTeam.members[index - 1]"
          type="text"
          :placeholder="'팀원 ' + index"
        />
      </div> -->
      <div class="new-member" v-for="member in teamMembers">
        <span class="close-button" @click="removeMember(index)">&times;</span>
        {{ member.workerName }}
      </div>
      <div class="button-container">
        <button class="new-team-btn" @click="addTeam">팀 생성하기</button>
      </div>
    </div>
    <div class="team-list">
      <div>
        <input type="text" placeholder="새로운 작업자 이름" v-model="newworker" />
        <div class="button-container">
          <button class="add-button" @click="addNewWorker()">작업자 추가</button>
        </div>
      </div>
      <div v-for="worker in datas">
        <hr />
        <li>작업자 번호 : {{ worker.memberId }}</li>
        <li>작업자 이름 : {{ worker.workerName }}</li>
        <li>팀 번호 : {{ worker.teamId }}</li>
        <div class="right-align">
          <button class="button" @click="pushTeam(worker)">팀 추가</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { getWokerList, postAddTeam } from "../../../api/team/team";
import { storeToRefs } from "pinia";

const datas = ref([]);
const teamMembers = ref([]);
const newworker = ref("");
const store2 = useAuthStore();
const newTeamName = ref("");
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

const removeMember = (index) => {
  teamMembers.value.splice(index, 1);
};

const emit = defineEmits(["update:isAddingTeam"], ["backToModal"]);

const goBack = () => {
  emit("backToModal");
};

function addTeam() {
  const reqBody = ref({
    teamName: newTeamName.value,
    workerList: teamMembers.value,
    area: areaName.value,
  });

  // api 요청
  postAddTeam(
    accessToken.value,
    reqBody.value,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
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
  gap: 20px;
}

.new-member {
  margin: 1vh 3px;
  font-size: 1.8vh;
  color: #373737;
}

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.add-team-form {
  padding: 20px;
  height: 55vh;
  background-color: #f0f0f0b0;
  margin: 0vh 0px 1.7vh 0px;
  border-radius: 4px;
  overflow-y: auto;
}

.team-list {
  padding: 20px;
  height: 55vh;
  background-color: #f0f0f0b0;
  margin: 0vh 0px 1.7vh 0px;
  border-radius: 4px;
  overflow-y: auto;
}

.add-team-btn {
  cursor: pointer;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
  margin-right: 8px;
  margin-bottom: 1vh;
}

input[type="text"] {
  padding: 1.3vh 2px 1.3vh 8px;
  border: 1px solid #bcbcbc;
  width: 100%;
  font-size: 1.5vh;
}

input[type="text"]:focus {
  outline: 0;
  border: 1px solid #6d6d6d;
}

li {
  font-size: 1.85vh;
  margin-bottom: 6px;
  color: #373737;
}

li::marker {
  color: #151c62;
}

.right-align {
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

.add-button {
  padding: 1.1vh 1.5vw;
  font-size: 1.45vh;
  background-color: #f8f8fc;
  border-radius: 5px;
  cursor: pointer;
  color: #4f58b5;
  border: 1px solid #4f58b5;
  transition: background-color 0.4s;
  margin: 0.7vh 0px 0.5vh 0px;
}

.add-button:hover {
  background-color: #e6e6f6;
}

.new-team-btn {
  background-color: #151c62;
  padding: 1.2vh 3vw;
  cursor: pointer;
  border-radius: 4px;
  font-size: 1.55vh;
  position: relative;
  overflow: hidden;
  color: #ffffff;
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 2vh;
}

.new-team-btn:hover {
  background-color: #0e1241;
}

.button {
  padding: 1vh 1vw;
  font-size: 1.4vh;
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

.close-button {
  cursor: pointer;
  color: #cd0404;
  font-weight: bold;
  font-size: 2.8vh;
  margin-right: 5px;
}

.close-button:hover {
  color: #8f0000;
}
</style>
