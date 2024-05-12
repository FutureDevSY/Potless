<template>
  <div class="data-container">
    <div class="data-header">
      <div class="data-title">{{ title }}</div>
      <div class="data-number">{{ number }} 건</div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  title: String,
  number: [String, Number],
});
</script>

<style scoped>
.data-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  margin: 0px 0px 10px 70px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  background-color: #fff;
  width: 250px;
  padding: 2.3vh 3vh;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
}

.data-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  border-color: #b0bec5;
}

.data-header {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.data-title {
  font-size: 2vh;
  color: #717171;
  font-weight: 600;
}

.data-number {
  font-size: 2.2vh;
  font-weight: bold;
  color: #373737;
}
</style>
