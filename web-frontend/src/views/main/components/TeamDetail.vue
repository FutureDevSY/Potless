<template>
  <div class="teamName">{{ props.team.teamName }}</div>
  <div class="slotWrapper">
    <div v-for="member in props.team.workerList" :key="member.memberId" class="memberSlot">
      <img v-if="member.profileUrl == undefined" src="../../../assets/icon/profile.png" alt="이미지">
      <img v-else :src="member.profileUrl" alt="이미지">
      <div class="memberName">{{ member.workerName }}</div>
    </div>
  </div>
  <div class="slotWrapper">
    <div v-for="i in 5 - props.team.workerList.length" :key="i" class="memberSlot">
      <img src="../../../assets/icon/profile.png" alt="이미지">
      <div>공석</div>
    </div>
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
  width: 35%;
  height: auto;
  padding-top: 3%;
}

.teamName {
  font-size: 20px;
  margin-bottom: 10%;
}


.slotWrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.memberSlot {
  width: 65%;
  height: 90px;
  border-radius: 15px;
  background-color: #ffffff;
  color: rgb(131, 131, 131);
  cursor: pointer;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.079);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  margin-bottom: 8%;
}
.memberSlot:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.memberName {
  position: relative;
  top: -25px;
  color: black;
}
</style>