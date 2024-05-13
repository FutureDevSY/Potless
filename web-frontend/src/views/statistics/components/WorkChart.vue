<template>
  <apexchart
    :key="chartKey"
    width="100%"
    height="112%"
    type="donut"
    :options="chartOptions"
    :series="chartSeries"
  />
</template>

<script setup>
import { ref, watch, onMounted, reactive } from "vue";

const props = defineProps({
  data: Array,
});

const chartKey = ref(0);
const chartOptions = reactive({
  labels: [],
  legend: { position: "bottom" },
  dataLabels: {
    enabled: true,
    formatter: (val) => `${val.toFixed(0)}%`,
  },
  plotOptions: {
    pie: {
      donut: {
        labels: {
          show: true,
          total: {
            show: true,
            label: "총계",
            formatter: (w) => w.globals.seriesTotals.reduce((a, b) => a + b, 0).toLocaleString(),
          },
        },
      },
    },
  },
  colors: ["#A7ABDA", "#4F58B5", "#151C62"],
  states: { active: { filter: { type: "none" } } },
  tooltip: {
    y: { formatter: (value) => `${value}%` },
  },
});

const chartSeries = ref([]);

function updateChartOptions() {
  if (props.data && props.data.length > 0) {
    chartOptions.labels = props.data.map((item) => item.title);
    chartSeries.value = props.data.map((item) => parseInt(item.number));
    chartKey.value++;
  }
}

watch(() => props.data, updateChartOptions, { immediate: true });
onMounted(updateChartOptions);
</script>

<style scoped></style>
