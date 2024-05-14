<template>
  <div class="pdf-container">
    <div class="number">{{ props.taskNumber }}</div>
    <div class="pdf-middle">
      <div class="title">{{ props.taskHeader.projectName }}</div>
      <div class="team">-{{ props.taskHeader.teamName }}-</div>
    </div>
    <div class="pdf-footer">
      <div>작업 일자: {{ props.taskHeader.projectDate }}</div>
      <div>담당자: {{ props.taskHeader.managerName }}</div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  taskHeader: Object,
  taskNumber: String,
});
</script>

<style scoped>
.pdf-container {
  border: 1px solid black;
  width: 210mm;
  height: 297mm;
  display: flex;
  flex-direction: column;
  padding: 30px;
  justify-content: space-between;
  box-sizing: border-box;
  font-family: "돋움체";
}
.number {
  width: fit-content;
  display: inline-block;
  padding: 5px;
  border: 2px solid black;
  font-size: 20px;
}
.title {
  font-size: 40px;
  font-weight: 600;
}
.team {
  font-size: 30px;
}
.pdf-middle {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.pdf-footer {
  display: flex;
  flex-direction: column;
  align-items: end;
  font-size: 15px;
}
</style>
