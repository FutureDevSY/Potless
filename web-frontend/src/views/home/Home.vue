<template>
  <div class="map-container">
    <!-- <p>서비스 로고</p> -->
    <SVG />
    <!-- <Chart
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
    /> -->
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
  colors: ["#BC7FCD", "#FB9AD1", "#FFCDEA"],
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
.map-container {
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: #555555;
}

.chart {
  position: absolute;
  width: 150px;
  height: 150px;
  width: 20%;
  margin: auto;
}

.chart1 {
  top: 100px;
  left: 200px;
}
.chart2 {
  top: 50px;
  right: 300px;
}
.chart3 {
  top: 550px;
  right: 350px;
}
.chart4 {
  top: 550px;
  left: 250px;
}
.chart5 {
  top: 310px;
  right: 100px;
}
</style>
