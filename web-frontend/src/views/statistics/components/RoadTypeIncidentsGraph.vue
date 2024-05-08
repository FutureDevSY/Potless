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
import { ref, computed, watch } from "vue";

const props = defineProps({
  searchTerm: String,
});

const searchTerm = ref("");
const apexChartRef = ref(null);

const allRoadsData = ref([
  { road: "대덕대로", potholes: 5, roadlength: 20 },
  { road: "월드컵대로", potholes: 10, roadlength: 20 },
  { road: "유성대로", potholes: 15, roadlength: 20 },
  { road: "현충원로", potholes: 20, roadlength: 20 },
  { road: "교촌대정로", potholes: 25, roadlength: 20 },
  { road: "노은로", potholes: 30, roadlength: 20 },
  { road: "문지로", potholes: 35, roadlength: 20 },
  { road: "반석서로", potholes: 40, roadlength: 20 },
  { road: "신성로", potholes: 45, roadlength: 20 },
  { road: "자운로", potholes: 50, roadlength: 20 },
  { road: "탑립로", potholes: 55, roadlength: 20 },
  { road: "봉우로", potholes: 60, roadlength: 20 },
  { road: "죽동로", potholes: 65, roadlength: 20 },
  { road: "국제과학로", potholes: 70, roadlength: 20 },
  { road: "세점길", potholes: 80, roadlength: 20 },
  { road: "엑스포로", potholes: 85, roadlength: 20 },
]);

const filteredRoadsData = computed(() => {
  return allRoadsData.value
    .filter((road) => road.road.toLowerCase().includes(props.searchTerm.toLowerCase()))
    .sort((a, b) => b.potholes - a.potholes)
    .slice(0, 10);
});

const series = computed(() => [
  {
    name: "1km당 포트홀의 개수",
    data: filteredRoadsData.value.map((data) => data.potholes / data.roadlength),
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
    categories: filteredRoadsData.value.map((data) => data.road),
    labels: {
      formatter: function (val) {
        return val.toFixed(1);
      },
    },
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

watch(searchTerm, () => {
  const newOptions = {
    xaxis: {
      categories: filteredRoadsData.value.map((data) => data.road),
    },
  };
  apexChartRef.value.updateOptions(newOptions, true, true);
});
</script>

<style scoped></style>
