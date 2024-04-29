<template>
  <div>
    <apexchart type="line" :options="chartOptions" :series="series" />
  </div>
</template>

<script setup>
import { ref } from "vue";

const chartOptions = ref({
  chart: {
    height: 300,
    type: "line",
    stacked: false,
    toolbar: {
      tools: {
        selection: false,
      },
    },
    zoom: {
      enabled: false,
    },
  },
  plotOptions: {
    bar: {
      columnWidth: "50%",
    },
  },
  stroke: {
    width: [4, 4],
  },
  dataLabels: {
    enabled: false,
  },
  markers: {
    size: 5,
    colors: undefined,
    strokeColors: "#fff",
    strokeWidth: 1,
    shape: "circle",
    hover: {
      sizeOffset: 2,
    },
  },
  labels: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
  xaxis: {
    type: "month",
  },
  yaxis: [
    {
      labels: {
        style: {
          colors: "#373737",
        },
      },
    },
  ],
  tooltip: {
    fixed: {
      enabled: true,
      position: "topLeft",
      offsetY: 30,
      offsetX: 100,
    },
  },
  legend: {
    horizontalAlign: "center",
    offsetY: 15,
    position: "top",
  },
  colors: ["#d8d8d8", "#151c62"],
});

const series = ref([
  {
    name: "월별 탐지건수",
    type: "column",
    data: [3131, 5432, 2145, 4212, 6703, 3300, 3244, 5482, 2234, 6680, 3605, 5210],
  },
  {
    name: "누적 탐지건수",
    type: "line",
    data: [3131, 8563, 10708, 14920, 21623, 24923, 28167, 33679, 35913, 42593, 46198, 51408],
  },
]);
</script>
