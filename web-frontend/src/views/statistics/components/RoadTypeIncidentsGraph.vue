<template>
  <div>
    <apexchart
      ref="apexChartRef"
      type="bar"
      width="98%"
      height="95%"
      :options="chartOptions"
      :series="series"
    ></apexchart>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from "vue";

const props = defineProps({
  searchTerm: String,
  roadData: Array,
});

const searchTerm = ref("");
const apexChartRef = ref(null);

const filteredDongData = computed(() => {
  return props.roadData
    .filter((road) => road.dong.toLowerCase().includes(props.searchTerm.toLowerCase()))
    .sort((a, b) => b.potholes - a.potholes)
    .slice(0, 10);
});

const series = computed(() => [
  {
    name: "전체 위험물 수",
    data: filteredDongData.value.map((dong) => dong.potholes),
  },
  {
    name: "심각도-상 위험물 수",
    data: filteredDongData.value.map((dong) => dong.severity),
  },
]);

const chartOptions = computed(() => ({
  chart: {
    type: "bar",
    toolbar: {
      show: true,
    },
    stacked: false,
    animations: {
      enabled: true,
      easing: "easeinout",
      speed: 1000,
      dynamicAnimation: {
        enabled: true,
        speed: 600,
      },
    },
  },
  colors: ["#4F58B5", "#F23B3B"],
  xaxis: {
    categories: filteredDongData.value.map((dong) => dong.dong),
  },
  plotOptions: {
    bar: {
      horizontal: true,
    },
  },
  dataLabels: {
    enabled: true,
    formatter: function (val) {
      if (isNaN(val) || val === 0) {
        return "";
      }
      return val;
    },
  },
  tooltip: {
    y: {
      formatter: function (val) {
        return val + " 개";
      },
    },
  },
}));

onMounted(() => {
  nextTick(() => {
    if (apexChartRef.value) {
      apexChartRef.value.updateOptions(
        {
          xaxis: {
            categories: filteredDongData.value.map((dong) => dong.dong),
          },
        },
        true,
        true
      );
    }
  });
});
</script>

<style scoped></style>
