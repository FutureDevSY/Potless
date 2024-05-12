<template>
  <div class="main-box">
    <div class="left-box">
      <div class="top-box">
        <Weather />
        <div class="today-container"></div>
      </div>
      <div class="bottom-box">
        <div class="report-container"></div>
        <div class="map-container"></div>
      </div>
    </div>
    <div class="right-box">
      <div class="worker-container"></div>
    </div>
  </div>
</template>
<script setup>
import Weather from "./components/Weather.vue";
</script>
<style scoped>
.main-box {
  display: grid;
  grid-template-columns: 72.2% 27%;
  gap: 0.8%;
  margin: 1%;
}
.left-box {
  display: grid;
  grid-template-rows: 23% 75.5%;
  gap: 1.5%;
}
.right-box {
  background-color: rgb(195, 254, 235);
}
.top-box {
  display: grid;
  grid-template-columns: 34% 65%;
  gap: 1%;
}
.bottom-box {
  display: grid;
  grid-template-columns: 59% 40%;
  gap: 1%;
}
.weather-container {
  background-color: rgb(255, 168, 168);
}
.today-container {
  background-color: rgb(255, 218, 255);
}
.report-container {
  background-color: rgb(246, 246, 176);
}
.map-container {
  background-color: blue;
}
</style>
