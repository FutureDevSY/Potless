<template>
  <div class="teamName">{{ props.team.teamName }}</div>
  <div v-for="member in props.team.workerList" :key="member.memberId">
      <img v-if="member.profileUrl == undefined" src="../../../assets/icon/profile.png" alt="이미지">
      <img v-else :src="member.profileUrl" alt="이미지">
      <div>{{ member.workerName }}</div>
  </div>
  <div v-for="i in 5 - props.team.workerList.length" :key="i">
    <img src="../../../assets/icon/profile.png" alt="이미지">
    <div>공석</div>
  </div>
</template>

<script setup>
const props = defineProps({
team: {
  type: Object,
  required: true,
},
});
</script>

<style scoped>
img {
  width: 15%;
  height: auto;
  padding-top: 3%;
}

.teamName {
  font-size: large;
  margin-bottom: 3%;
}
</style>