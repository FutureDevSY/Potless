<template>
    <div>{{ props.team.teamName }}</div>
    <div v-for="member in props.team.workerList" :key="member.memberId">
        <div>{{ member.workerName }}</div>
        <img :src="member.profileUrl" alt="">
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

<style scoped></style>