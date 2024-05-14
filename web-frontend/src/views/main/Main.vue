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
          <div>지역별 현황</div>
          <SVG></SVG>
        </div>
      </div>
    </div>
    <div class="right-box">
      <div class="worker-container"><div>보수 공사팀</div></div>
    </div>
  </div>
</template>
<script setup>
import Weather from "./components/Weather.vue";
import CurrentStats from "./components/CurrentStats.vue";
import ReportList from "./components/TaskList.vue";
import SVG from "./components/SVG.vue";
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
  grid-template-rows: 23% 74.5%;
  gap: 2%;
}
.right-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.top-box {
  display: grid;
  grid-template-columns: 34% 64.5%;
  gap: 1.5%;
}
.bottom-box {
  display: grid;
  grid-template-columns: 59% 40%;
  gap: 1%;
}
.report-container {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.map-containers {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
</style>
