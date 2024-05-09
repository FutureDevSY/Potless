<template>
  <div>
    <apexchart
      ref="apexChartRef"
      type="bar"
      width="98%"
      height="372"
      :options="chartOptions"
      :series="series"
    ></apexchart>
  </div>
</template>

<script setup>
import { ref, computed, watchEffect, onMounted, nextTick } from "vue";

const props = defineProps({
  searchTerm: String,
  roadData: Array
});

const searchTerm = ref("");
const apexChartRef = ref(null);

// 필터링된 동 데이터와 포트홀 수 계산
const filteredDongData = computed(() => {
  return props.roadData
    .filter(road => road.dong.toLowerCase().includes(props.searchTerm.toLowerCase()))
    .sort((a, b) => b.potholes - a.potholes) // 포트홀 수에 따라 정렬
    .slice(0, 10); // 상위 10개만 선택
});

const series = computed(() => [
  {
    name: "포트홀 수",
    data: filteredDongData.value.map(dong => dong.potholes),
  },
]);

const chartOptions = computed(() => ({
  chart: {
    type: "bar",
    toolbar: {
      show: true,
    },
  },
  xaxis: {
    categories: filteredDongData.value.map(dong => dong.dong),
  },
  plotOptions: {
    bar: {
      horizontal: true,
      distributed: false,
      colors: {
        ranges: [
          {
            from: 0,
            to: 100,
            color: "#D3D5ED",
          },
        ],
        backgroundBarOpacity: 1,
      },
    },
  },
  states: {
    hover: {
      filter: {
        type: "darken",
        value: 0.3,
      },
    },
  },
  dataLabels: {
    enabled: false,
  },
}));

onMounted(() => {
  nextTick(() => {
    if (apexChartRef.value) {
      apexChartRef.value.updateOptions({
        xaxis: {
          categories: filteredDongData.value.map(dong => dong.dong),
        }
      }, true, true);
    }
  });
});

watchEffect(() => {
  if (apexChartRef.value) {
    const newOptions = {
      xaxis: {
        categories: filteredDongData.value.map(dong => dong.dong),
      }
    };
    nextTick(() => {
      apexChartRef.value.updateOptions(newOptions, true, true);
    });
  }
});
</script>

<style scoped></style>
