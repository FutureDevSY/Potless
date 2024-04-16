<template>
  <div>
    <apexchart type="line" :options="chartOptions" :series="series" />
  </div>
</template>

<script setup>
import { ref } from "vue";

const chartOptions = ref({
  chart: {
    height: 350,
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
    enabled: true,
    enabledOnSeries: [1],
  },
  labels: [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ],
  xaxis: {
    type: "month",
  },
  yaxis: [
    {
      axisTicks: {
        show: true,
      },
      axisBorder: {
        show: true,
        color: "#FF1654",
      },
      labels: {
        style: {
          colors: "#FF1654",
        },
      },
      title: {
        text: "",
        style: {
          color: "#FF1654",
        },
      },
      tooltip: {
        enabled: true,
      },
    },
  ],
  tooltip: {
    fixed: {
      enabled: true,
      position: "topLeft",
      offsetY: 30,
      offsetX: 60,
    },
  },
  legend: {
    horizontalAlign: "left",
    offsetX: 40,
  },
});

const series = ref([
  {
    name: "막대",
    type: "column",
    data: [
      3131, 5432, 2145, 4212, 6703, 3300, 3244, 5482, 2234, 6680, 3605, 5210,
    ],
  },
  {
    name: "그래프",
    type: "line",
    data: [
      3131, 8563, 10708, 14920, 21623, 24923, 28167, 33679, 35913, 42593, 46198,
      51408,
    ],
  },
]);
</script>
