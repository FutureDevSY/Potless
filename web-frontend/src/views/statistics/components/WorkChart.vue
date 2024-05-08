<template>
  <div class="chart-container">
    <div class="chart">
      <apexchart type="donut" :options="chartOptions" :series="series" />
    </div>
    <div class="data-display">
      <transition-group name="incident-report" tag="div" class="data-list">
        <Inquire
          v-for="(item, index) in dataItems"
          :key="index"
          :title="item.title"
          :number="item.number"
          class="infos"
        />
      </transition-group>
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
  colors: ["#7B82C8", "#232EA3", "#151c62"],
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
  border-left: 1px solid #ccc;
  height: 200px;
}
/* slide animation 효과 -> 오른쪽에서 왼쪽으로 이동 */
@keyframes slideIn {
  from {
    transform: translateX(-30px);
    opacity: 0;
  }
  to {
    transform: translateX(0px);
    opacity: 1;
  }
}
.incident-report-enter-active {
  animation: slideIn 0.6s ease-out forwards;
}
.incident-report-enter {
  opacity: 0;
}
.chart {
  width: 100%;
}
.chart-container {
  display: grid;
  grid-template-columns: 5fr 7fr;
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
