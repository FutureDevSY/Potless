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
  border-radius: 10px;
  margin: 0px 0px 15px 40px;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.175);
  background-color: #f5f8fe;
  width: 250px;
  padding: 2.6vh 3.5vh;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
}

.data-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.296);
}

.data-header {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.data-title {
  font-size: 2vh;
  color: #3f415b;
  font-weight: 600;
}

.data-number {
  font-size: 2.2vh;
  font-weight: bold;
  color: #1e476d;
}
</style>
