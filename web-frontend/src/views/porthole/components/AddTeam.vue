<template>
  <div class="add-team-form">
    <input v-model="newTeam.name" type="text" placeholder="팀 이름" />
    <div v-for="index in 5" :key="index">
      <input
        v-model="newTeam.members[index - 1]"
        type="text"
        :placeholder="'팀원 ' + index"
      />
    </div>
    <button @click="addTeam">팀 추가</button>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";

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
</script>

<style scoped>
.add-team-container {
}

.input-group {
  margin-bottom: 10px;
}

.add-team-btn {
  cursor: pointer;
}
</style>
