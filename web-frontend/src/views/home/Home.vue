<template>
  <div class="map-container">
    <p class="logo">POTLESS</p>
    <SVG />
    <Chart
      class="chart chart1"
      :options="chartOptions"
      :series="chart1Series"
      :section="section[0]"
    />
    <Chart
      class="chart chart2"
      :options="chartOptions"
      :series="chart2Series"
      :section="section[1]"
    />
    <Chart
      class="chart chart3"
      :options="chartOptions"
      :series="chart3Series"
      :section="section[2]"
    />
    <Chart
      class="chart chart4"
      :options="chartOptions"
      :series="chart4Series"
      :section="section[3]"
    />
    <Chart
      class="chart chart5"
      :options="chartOptions"
      :series="chart5Series"
      :section="section[4]"
    />
  </div>
</template>

<script setup>
import SVG from "./components/SVG.vue";
import Chart from "./components/Chart.vue";

const chartOptions = {
  chart: {
    type: "pie",
    toolbar: {
      show: true,
    },
  },
  labels: ["완료", "미완료"],
  legend: {
    position: "bottom",
    labels: {
      colors: ["#373737", "#373737"],
    },
  },
  responsive: [
    {
      breakpoint: 480,
      options: {
        chart: {
          width: 100,
        },
        legend: {
          position: "bottom",
        },
      },
    },
  ],
  colors: ["#273a86", "#4f5e9d6f"],
  tooltip: {
    y: {
      formatter: (value) => `${value}%`,
    },
  },
};

const chart1Series = [40, 60];
const chart2Series = [30, 70];
const chart3Series = [20, 80];
const chart4Series = [10, 90];
const chart5Series = [50, 50];
const section = ["유성구", "대덕구", "중구", "서구", "동구"];
</script>

<style scoped>
.et {
  color: #4f5e9d6f;
}
.map-container {
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: #f3f3f3;
}

.logo {
  font-size: 55px;
  color: #373737;
  font-weight: bold;
  margin: 4.5vh 0px 0px 0px;
}

.chart {
  position: absolute;
  width: 34vh;
  margin: auto;
}

/* 유성구 */
.chart1 {
  top: 33vh;
  left: 8vw;
}

/* 대덕구 */
.chart2 {
  top: 15vh;
  right: 10vw;
}

/* 중구 */
.chart3 {
  top: 70vh;
  right: 15vw;
}

/* 서구 */
.chart4 {
  top: 70vh;
  left: 8vw;
}

/* 동구 */
.chart5 {
  top: 47vh;
  right: 1vw;
}
</style>
