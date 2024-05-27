<template>
  <div class="teamName">< {{ props.team.teamName }} ></div>
  <div class="slotWrapper">
    <div
      v-for="member in props.team.workerList"
      :key="member.memberId"
      class="memberSlot"
      @mouseover="handleMouseOver"
      @mouseleave="handleMouseLeave"
      :class="{ darken: isHovered }"
      @click="handleButtonClick"
    >
      <img
        v-if="member.profileUrl == undefined"
        class="default-img"
        src="../../../assets/icon/profile2.png"
        alt="이미지"
      />
      <img class="worker-img" v-else :src="member.profileUrl" alt="이미지" />
      <img v-if="isHovered" class="edit-img" src="../../../assets/icon/edit.png" alt="편집" />
      <div class="memberName" :class="{ hovered: isHovered }">{{ member.workerName }}</div>
    </div>
  </div>

  <div class="slotWrapper">
    <div v-for="i in 5 - props.team.workerList.length" :key="i" class="memberSlot">
      <img class="default-img" src="../../../assets/icon/profile2.png" alt="이미지" />
      <div>공석</div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";

const props = defineProps({
  team: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["open-modal"]);

const isHovered = ref(false);

const handleMouseOver = () => {
  isHovered.value = true;
};

const handleMouseLeave = () => {
  isHovered.value = false;
};

const handleButtonClick = () => {
  emit("open-modal", "team");
};
</script>

<style scoped>
.default-img {
  width: auto;
  height: 11vh;
  margin-top: 10%;
}

.worker-img {
  width: 88%;
  height: 12vh;
  margin-top: 4.5%;
}

.edit-img {
  position: absolute;
  height: 2.5vh;
  margin-top: 12.4vh;
  left: 47%;
}

.teamName {
  font-size: 1.6vh;
  font-weight: 500;
  color: #1e476d;
  margin-top: -0.5vh;
  margin-bottom: 0.5vh;
}

.slotWrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  width: 100%;
  /* cursor: pointer; */
}

.memberSlot {
  width: 45%;
  height: 15.3vh;
  border-radius: 15px;
  background-color: #f5f8fe;
  cursor: pointer;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.162);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  margin-top: 1vh;
}

.darken {
  background-color: #efefef;
  transition: background-color 0.4s ease;
}

.memberName {
  position: relative;
  top: 0vh;
  font-size: 1.6vh;
  font-weight: bold;
  color: #565656;
  transition: background-color 0.4s ease;
}
.hovered {
  color: #efefef00;
}

.list-button {
  position: relative;
}
</style>
