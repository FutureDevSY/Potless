<template>
  <div>
    <apexchart
      ref="apexChartRef"
      type="bar"
      width="98%"
      height="388"
      :options="chartOptions"
      :series="series"
    ></apexchart>
  </div>
</template>

<script setup>
import { ref, computed, watchEffect, onMounted, nextTick } from "vue";

const props = defineProps({
  searchTerm: String,
  roadData: Array,
});

const searchTerm = ref("");
const apexChartRef = ref(null);

// 필터링된 동 데이터와 포트홀 수 계산
const filteredDongData = computed(() => {
  return props.roadData
    .filter((road) => road.dong.toLowerCase().includes(props.searchTerm.toLowerCase()))
    .sort((a, b) => b.potholes - a.potholes) // 포트홀 수에 따라 정렬
    .slice(0, 10); // 상위 10개만 선택
});

// 포트홀 개수, 위험한 포트홀 개수
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
      return val;
    },
  },
  tooltip: {
    y: {
      formatter: function (val) {
        return val + " 개"; // 툴팁에 '개' 단위 추가
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
