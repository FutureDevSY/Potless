<template>
  <div class="chart">
    <apexchart :key="chartKey" type="donut" :options="chartOptions" :series="chartSeries" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, reactive } from 'vue';
import { defineProps } from 'vue';

const props = defineProps({
  data: Array
});

const chartKey = ref(0); // 차트 강제 리렌더링을 위한 키
const chartOptions = reactive({
  labels: [],
  legend: { position: "bottom" },
  dataLabels: {
    enabled: true,
    formatter: val => `${val.toFixed(0)}%`
  },
  plotOptions: {
    pie: {
      donut: {
        labels: {
          show: true,
          total: {
            show: true,
            label: "총계",
            formatter: w => w.globals.seriesTotals.reduce((a, b) => a + b, 0).toLocaleString()
          }
        }
      }
    }
  },
  colors: ["#7B82C8", "#232EA3", "#151c62"],
  states: { active: { filter: { type: "none" } } },
  tooltip: {
    y: { formatter: value => `${value}%` }
  }
});

const chartSeries = ref([]);

function updateChartOptions() {
  if (props.data && props.data.length > 0) {
    chartOptions.labels = props.data.map(item => item.title);
    chartSeries.value = props.data.map(item => parseInt(item.number));
    chartKey.value++; // 키 값을 변경하여 차트를 강제로 리렌더링
  }
}

watch(() => props.data, updateChartOptions, { immediate: true });
onMounted(updateChartOptions);
</script>

<style scoped>
.chart {
  width: 90%;
}
</style>
