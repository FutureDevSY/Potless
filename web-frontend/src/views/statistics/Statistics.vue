<template>
  <div class="statics-container">
    <div class="left-box">
      <p class="incident-title">위험물 발생 현황</p>
      <div class="incident-report">
        <IncidentReport
          v-for="(item, index) in dataItems"
          :key="index"
          :title="item.title"
          :number="item.number"
          :subtitle="item.subtitle"
          :percent="item.percent"
        />
      </div>
      <p class="totalincident-title">위험물 누적 탐지 건수</p>
      <div class="incident-graph">
        <IncidentGraph />
      </div>
    </div>
    <div class="right-box">
      <p class="road-title">지역별 발생 현황</p>
      <RoadTypeIncidentsGraph />
      <p class="work-title">보수 공사 현황</p>
      <WorkChart class="work-chart" />
    </div>
  </div>
</template>

<script setup>
import IncidentGraph from "./components/IncidentGraph.vue";
import IncidentReport from "./components/IncidentReport.vue";
import RoadTypeIncidentsGraph from "./components/RoadTypeIncidentsGraph.vue";
import WorkChart from "./components/WorkChart.vue";

const dataItems = [
  { title: "오늘", number: "250", subtitle: "전날 대비", percent: "+30%" },
  { title: "1주일", number: "1,500", subtitle: "전주 대비", percent: "-3%" },
  { title: "1개월", number: "7,420", subtitle: "전달 대비", percent: "+10%" },
];
</script>

<style scoped>
.statics-container {
  max-width: 1920px;
  max-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.left-box,
.right-box {
  display: flex;
  flex-direction: column;
  padding: 5px 15px;
}
.incident-report {
  display: grid;
  margin-bottom: 30px;
  grid-template-columns: 1fr 1fr 1fr;
}

.totalincident-title, .incident-title, .road-title, .work-title {
  font-size: 18px;
  font-weight: 600;
  color: #373737;
}

.incident-title {
  margin: 10px 0px 10px 10px;
}

.totalincident-title {
  margin: 10px 0px 0px 10px;
}

.road-title {
  margin: 10px 0px 15px 0px;
}

.work-title {
  margin: 30px 0px 15px 0px;
}

.incident-graph {
  width: 100%;
}
.work-chart {
  width: 100%;
  margin: 20px 0;
}
</style>
