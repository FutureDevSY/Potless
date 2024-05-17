<template>
  <div class="teamName">< {{ props.team.teamName }} ></div>
  <div class="slotWrapper">
    <div v-for="member in props.team.workerList" :key="member.memberId" class="memberSlot">
      <img
        v-if="member.profileUrl == undefined"
        class="default-img"
        src="../../../assets/icon/profile.png"
        alt="이미지"
      />
      <img v-else :src="member.profileUrl" alt="이미지" />
      <div class="memberName">{{ member.workerName }}</div>
    </div>
  </div>
  <div class="slotWrapper">
    <div v-for="i in 5 - props.team.workerList.length" :key="i" class="memberSlot">
      <img class="default-img" src="../../../assets/icon/profile.png" alt="이미지" />
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
.default-img {
  width: auto;
  height: 10vh;
  margin-top: 10%;
}
/* img {
  width: 35%;
  height: auto;
  padding-top: 3%;
} */

.teamName {
  font-size: 1.6vh;
  margin-bottom: 1.5vh;
}

.slotWrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 97%;
  width: 100%;
}

.memberSlot {
  width: 45%;
  height: 15vh;
  border-radius: 15px;
  background-color: #f0f0f0;
  color: rgb(131, 131, 131);
  cursor: pointer;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.162);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  margin-bottom: 1vh;
}
.memberSlot:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.memberName {
  position: relative;
  top: 0.3vh;
  font-size: 1.6vh;
  font-weight: bold;
  color: rgb(43, 43, 43);
}
</style>
