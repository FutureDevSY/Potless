<template>
  <div class="main-box">
    <div class="left-box">
      <div class="top-box">
        <Weather />
        <CurrentStats />
      </div>
      <div class="bottom-box">
        <div class="report-container">
          <ReportList />
        </div>
        <div class="map-containers">
          <SVG1 v-if="areaId === 1"></SVG1>
          <SVG2 v-if="areaId === 2"></SVG2>
          <SVG3 v-if="areaId === 3"></SVG3>
          <SVG4 v-if="areaId === 4"></SVG4>
          <SVG5 v-if="areaId === 5"></SVG5>
        </div>
      </div>
    </div>
    <div class="right-box">
      <div class="worker-container">
        <Team></Team>
      </div>
    </div>
  </div>
</template>

<script setup>
import Weather from "./components/Weather.vue";
import CurrentStats from "./components/CurrentStats.vue";
import ReportList from "./components/TaskList.vue";
import SVG1 from "./components/SVG1.vue";
import SVG2 from "./components/SVG2.vue";
import SVG3 from "./components/SVG3.vue";
import SVG4 from "./components/SVG4.vue";
import SVG5 from "./components/SVG5.vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import Team from "./components/Team.vue";

const store = useAuthStore();
const { areaId } = storeToRefs(store);
</script>

<style scoped>
.main-box {
  display: grid;
  grid-template-columns: 77.8% 21%;
  gap: 1.2%;
  margin: 1%;
}
.left-box {
  display: grid;
  grid-template-rows: 23% 75%;
  gap: 2%;
}
.right-box {
  background-color: #f2f2f2a3;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.top-box {
  display: grid;
  grid-template-columns: 34% 64.5%;
  gap: 1.5%;
}
.bottom-box {
  display: grid;
  grid-template-columns: 51.5% 47%;
  gap: 1.5%;
}
.report-container {
  background-color: rgb(241, 241, 241);
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.map-containers {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
</style>
