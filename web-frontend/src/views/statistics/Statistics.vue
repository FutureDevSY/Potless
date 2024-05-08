<template>
  <div class="statics-container">
    <div class="left-box">
      <p class="incident-title">위험물 발생 현황</p>
      <div class="incident-report">
        <IncidentReport
          v-for="(item, index) in reportData"
          :key="index"
          :title="item.title"
          :number="item.number"
          :subtitle="item.subtitle"
          :percent="item.percent"
          :diff="item.diff"
        />
      </div>
      <p class="totalincident-title">위험물 누적 탐지 건수</p>
      <div class="incident-graph">
        <IncidentGraph />
      </div>
      <div class="title-input">
        <p class="road-title">도로별 km당 발생 건수</p>
        <input
          type="text"
          v-model="searchTerm"
          placeholder="도로명을 입력해주세요."
          @input="updateChart"
        />
      </div>
      <div class="road-graph">
        <RoadTypeIncidentsGraphVue :search-term="searchTerm" />
      </div>
    </div>
    <div class="right-box">
      <p class="town-title">지역별 발생 현황</p>
      <TownTypeIncidentsGraph />
      <p class="work-title">보수 공사 현황</p>
      <WorkChart class="work-chart" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import IncidentGraph from "./components/IncidentGraph.vue";
import IncidentReport from "./components/IncidentReport.vue";
import RoadTypeIncidentsGraphVue from "./components/RoadTypeIncidentsGraph.vue";
import TownTypeIncidentsGraph from "./components/TownTypeIncidentsGraph.vue";
import WorkChart from "./components/WorkChart.vue";
import { format, subDays } from 'date-fns';
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import {
  getGuList,
  getDongList,
  getTotalDongList,
  getDongDetail,
  getDongPerDate
} from "../../api/statistics/statistics";


const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);
const searchTerm = ref("");

// 대전 시 전체 구 데이터
// const GuData = ref(null);

// 포트홀 구에 속한 동에 대한 통계 조회 (구 1개, 동 여러개)
// const DongListData = ref(null);

// 구 별로 상관없이 모든 동의 전체 통계 출력
// const TotalDongListData = ref(null);

// 동 이름 검색하면 해당 동의 통계 출력
// const DongDetailData = ref(null);

// 구 별로 지정한 월별 발생한 통계 출력
const reportData = ref([]) // 데이터set을 담을 변수
const today = new Date(); // 오늘 날짜
const yesterday = subDays(today, 1); // 어제 날짜
const thisWeekStart = subDays(today, 6); // 이번주 시작 날짜
const lastWeekEnd = subDays(thisWeekStart, 1); // 저번주 마지막 날짜
const lastWeekStart = subDays(lastWeekEnd, 6); // 저번주 시작 날짜
const thisMonthStart = subDays(today, 29); // 이번달 시작 날짜
const lastMonthEnd = subDays(thisMonthStart, 1); // 저번달 마지막 날짜
const lastMonthStart = subDays(lastMonthEnd, 29); // 저번달 시작 날짜

const formatDate = (date) => format(date, "yyyy-MM-dd"); // 날짜 형식 변경

function calculatePercentChange(current, previous) { // 증감률 계산 함수
  if (previous === 0) return "N/A";  // 0으로 나누면 "N/A"을 반환하고 IncidentReport에서 메세지 처리
  return ((current - previous) / previous * 100).toFixed(1) + "%";
}


// 해당 함수를 호출하는 예시 코드
// const startDate = "2024-01-14";
// const endDate = "2024-03-14";

const TakeStatistics = async () => {
  // getGuList(
  //   accessToken.value,
  //   (res) => {
  //     console.log("구 전체 리스트 요청", res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       GuData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  // getDongList(
  //   accessToken.value,
  //   areaId.value,
  //   (res) => {
  //     console.log("동 리스트 요청", res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       DongListData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  // getTotalDongList(
  //   accessToken.value,
  //   (res) => {
  //     console.log("동 전체 정보", res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       TotalDongListData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  // getTotalDongList(
  //   accessToken.value,
  //   (res) => {
  //     console.log("동 전체 정보", res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       TotalDongListData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  // 동 이름 검색하면 해당 동의 통계 출력
  // getDongDetail(
  //   accessToken.value,
  //   (res) => {
  //     console.log(res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       DongDetailData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

  
  // 구 별로 지정한 월별 발생한 통계 출력
  // getDongMonthly(
  //   accessToken.value,
  //   (res) => {
  //     console.log(res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       getDongMonthlyData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );

 // 구 별로 지정한 날짜별 발생한 통계 출력
  // getDongPerDate(
  //   accessToken.value,
  //   startDate,
  //   endDate,
  //   (res) => {
  //     console.log("동 날짜별 정보", res);
  //     if (res.data.status == "SUCCESS") {
  //       console.log(res.data.message);
  //       getDongPerDateData.value = res.data.data;
  //     }
  //   },
  //   (error) => {
  //     console.log(error);
  //     console.log(error.response.data.message);
  //   }
  // );
};

// API 호출을 위한 일반화된 함수
async function fetchDataForPeriod(start, end, title) {
  return new Promise((resolve, reject) => {
    getDongPerDate(
      accessToken.value,
      formatDate(start),
      formatDate(end),
      (res) => {
        if (res.data && res.data.status === "SUCCESS") {
          console.log(res.data.message);
          const data = res.data.data.list;
          const yuseongData = data.find(d => d.areaGu === "유성구");
          const counts = yuseongData.list.map(item => item.count);
          const total = counts.reduce((acc, current) => acc + current, 0);
          resolve({ title: title, number: total });
        }
      },
      (error) => {
        console.error(`Error fetching data for ${title}:`, error);
        reject(error);
      }
    );
  });
}

const calculateData = async () => {
  try {
    // 데이터 가져오기
    const todayData = await fetchDataForPeriod(today, today, "오늘");
    const yesterdayData = await fetchDataForPeriod(yesterday, yesterday, "어제");
    const thisWeekData = await fetchDataForPeriod(thisWeekStart, today, "최근 1주일");
    const lastWeekData = await fetchDataForPeriod(lastWeekStart, lastWeekEnd, "지난 1주일");
    const thisMonthData = await fetchDataForPeriod(thisMonthStart, today, "최근 1달");
    const lastMonthData = await fetchDataForPeriod(lastMonthStart, lastMonthEnd, "지난 1달");

    // 증감률 계산
    const dailyChangePercent = calculatePercentChange(todayData.number, yesterdayData.number);
    const weeklyChangePercent = calculatePercentChange(thisWeekData.number, lastWeekData.number);
    const monthlyChangePercent = calculatePercentChange(thisMonthData.number, lastMonthData.number);

    // 데이터셋 저장
    reportData.value = [
      { title: "오늘", number: todayData.number, subtitle: "전날 대비",  diff: yesterdayData.number, percent: dailyChangePercent },
      { title: "최근 1주일", number: thisWeekData.number, subtitle: "전주 대비", diff: lastWeekData.number, percent: weeklyChangePercent },
      { title: "최근 1달", number: thisMonthData.number, subtitle: "전달 대비", diff: lastMonthData.number, percent: monthlyChangePercent }
    ];
    console.log("데이터:", reportData.value)
  } catch (error) {
    console.error("Failed to fetch data:", error);
  }
};

onMounted(() => {
  // TakeStatistics();
  calculateData();
});
</script>

<style scoped>
.statics-container {
  max-width: 1920px;
  max-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.left-box,
.right-box {
  display: flex;
  flex-direction: column;
  padding: 20px 15px;
}
.incident-report {
  display: grid;
  margin-bottom: 20px;
  grid-template-columns: 1fr 1fr 1fr;
}

.totalincident-title,
.incident-title,
.town-title,
.work-title,
.road-title {
  font-size: 18px;
  font-weight: 600;
  color: #373737;
}

.incident-title {
  margin: 10px 0px 10px 10px;
}

.totalincident-title {
  margin: 20px 0px 0px 10px;
}

.town-title {
  margin: 10px 0px 15px 0px;
}

.work-title {
  margin: 75px 0px 15px 0px;
}

.road-title {
  margin: 25px 0px 0px 10px;
  padding-bottom: 15px;
}

.incident-graph {
  width: 100%;
}
.work-chart {
  width: 100%;
}

.title-input {
  display: flex;
  align-items: center;
  margin: 40px 0px 0px 20px;
}

input {
  width: 30%;
  padding: 10px;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #a1a1a1;
  transition: border 0.4s ease;
  font-size: 16px;
  color: #373737;
  margin-left: 20px;
}

input:focus {
  outline: 0;
  border-color: #1f2993;
}

.road-title {
  margin: 0;
}
</style>
