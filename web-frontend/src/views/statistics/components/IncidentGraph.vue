<template>
  <div>
    <apexchart type="line" :options="chartOptions" :series="series" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { getAreaDetails, getDongMonthly } from "../../../api/statistics/statistics";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { format, addMonths, subYears } from "date-fns";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

// ** 로그인 한 지역의 areaGu를 호출
const areaDetails = ref(null); // 지역 이름 저장할

const fetchAreaDetails = async () => {
  const response = await getAreaDetails(accessToken.value, areaId.value);
  if (response && response.status === "SUCCESS") {
    areaDetails.value = response.data;
  } else {
    console.error("Failed to fetch area details");
  }
};

// ** 위험물 발생 현황: 동 이름 검색하면 해당 동의 통계 출력
const cumulateData = ref([]);

const startYear = format(addMonths(subYears(new Date(), 1), 1), "yyyy-MM"); // 작년 2023-06
const currentMonth = format(new Date(), "yyyy-MM"); // 올해 2024-05

const thirdData = async () => {
  const response = await getDongMonthly(accessToken.value, startYear, currentMonth);
  if (response && response.status === "SUCCESS") {
    const data = response.data.list;
    const areaData = data.find((d) => d.areaGu === areaDetails.value.areaGu); // areaGu 동적 사용
    cumulateData.value = areaData.list.map((monthData) => ({
      month: monthData.month,
      count: monthData.count,
    }));
    // console.log("위험물 누적 탐지 건수:", cumulateData.value);
  } else {
    console.error("Failed to fetch data");
  }
};

onMounted(() => {
  fetchAreaDetails().then(() => {
    thirdData();
  });
});

const chartOptions = computed(() => ({
  chart: {
    height: "320px",
    type: "line",
    stacked: false,
    animations: {
      enabled: true,
      easing: "easeinout",
      speed: 500,
      dynamicAnimation: {
        enabled: true,
        speed: 900,
      },
    },
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
    width: [3, 3],
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
  xaxis: {
    categories: cumulateData.value.map((item) => item.month), // X축 레이블 업데이트
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
  colors: ["#BCF95D", "#151c62"],
  states: {
    hover: {
      filter: {
        type: "darken",
        value: 0.85,
      },
    },
  },
}));

const series = computed(() => [
  {
    name: "월별 탐지건수",
    type: "column",
    data: cumulateData.value.map((item) => item.count),
  },
  {
    name: "누적 탐지건수",
    type: "line",
    data: cumulateData.value.reduce(
      (acc, cur) => [...acc, acc.length ? acc[acc.length - 1] + cur.count : cur.count],
      []
    ),
  },
]);
</script>
