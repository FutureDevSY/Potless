<template>
  <div class="chart-container">
    <div class="chart">
      <apexchart type="donut" :options="chartOptions" :series="series" />
    </div>
    <div class="data-display">
      <Inquire
        v-for="(item, index) in dataItems"
        :key="index"
        :title="item.title"
        :number="item.number"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Inquire from "./Inquire.vue";

const chartOptions = ref({
  labels: ["미완료", "보수중", "완료"],
  legend: {
    position: "bottom",
  },
  dataLabels: {
    enabled: true,
    formatter: function (val) {
      return `${val.toFixed(0)}%`;
    },
  },
  plotOptions: {
    pie: {
      donut: {
        labels: {
          show: true,
          total: {
            show: true,
            label: "총계",
            formatter: function (w) {
              return w.globals.seriesTotals.reduce((a, b) => {
                return a + b;
              }, 0);
            },
          },
        },
      },
    },
  },
  colors: ["#BC7FCD", "#FB9AD1", "#FFCDEA"],
  states: {
    active: {
      filter: {
        type: "none",
      },
    },
  },
  tooltip: {
    y: {
      formatter: function (value) {
        return `${value}%`;
      },
    },
  },
});

const series = [50, 20, 30];

const dataItems = [
  { title: "완료", number: "300" },
  { title: "보수중", number: "200" },
  { title: "미완료", number: "500" },
];
</script>

<style scoped>
.data-display {
  width: 100%;
  border-left: 1px solid #ccc;
}
.chart {
  width: 418px;
}
.chart-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
.data-total {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  margin: 20px;
}
</style>
