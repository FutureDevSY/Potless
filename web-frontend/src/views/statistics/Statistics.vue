<template>
  <div class="statics-container">
    <div class="left-box">
      <div class="incident-box">
        <p class="incident-title">위험물 발생 현황</p>
        <transition-group name="incident-report" tag="div" class="incident-report">
          <IncidentReport
            v-for="(item, index) in IncidentData"
            :key="index"
            :title="item.title"
            :number="item.number"
            :subtitle="item.subtitle"
            :percent="item.percent"
            :diff="item.diff"
          />
        </transition-group>
      </div>

      <div class="danger-box">
        <div class="title-input">
          <p class="road-title">도로별 km당 발생 건수</p>
          <input
            type="text"
            v-model="searchTerm"
            placeholder="도로명을 입력해주세요."
            @input="updateChart"
          />
        </div>
        <RoadTypeIncidentsGraphVue :search-term="searchTerm" />
      </div>
    </div>
    <div class="right-box">
      <div class="total-box">
        <p class="totalincident-title">위험물 누적 탐지 건수</p>
        <div class="incident-graph">
          <IncidentGraph />
        </div>
      </div>

      <div class="work-box">
        <p class="work-title">보수 공사 현황</p>
        <WorkChart class="work-chart" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import IncidentGraph from "./components/IncidentGraph.vue";
import IncidentReport from "./components/IncidentReport.vue";
import RoadTypeIncidentsGraphVue from "./components/RoadTypeIncidentsGraph.vue";
import TownTypeIncidentsGraph from "./components/TownTypeIncidentsGraph.vue";
import WorkChart from "./components/WorkChart.vue";
import { format, addMonths, subYears, subDays } from "date-fns";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import {
  getAreaDetails,
  getGuList,
  getDongList,
  getTotalDongList,
  getDongDetail,
  getDongMonthly,
  getDongPerDate,
} from "../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);
const searchTerm = ref("");

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

// ** 위험물 발생 현황: 구 별로 지정한 월별 발생한 통계 출력
const IncidentData = ref([]); // 데이터set을 담을 변수
const today = new Date(); // 오늘 날짜
const yesterday = subDays(today, 1); // 어제 날짜
const thisWeekStart = subDays(today, 6); // 이번주 시작 날짜
const lastWeekEnd = subDays(thisWeekStart, 1); // 저번주 마지막 날짜
const lastWeekStart = subDays(lastWeekEnd, 6); // 저번주 시작 날짜
const thisMonthStart = subDays(today, 29); // 이번달 시작 날짜
const lastMonthEnd = subDays(thisMonthStart, 1); // 저번달 마지막 날짜
const lastMonthStart = subDays(lastMonthEnd, 29); // 저번달 시작 날짜

const formatDate = (date) => format(date, "yyyy-MM-dd"); // 날짜 형식 변경

function calculatePercentChange(current, previous) {
  // 증감률 계산 함수
  if (previous === 0) return "N/A"; // 0으로 나누면 "N/A"을 반환하고 IncidentReport에서 메세지 처리
  return (((current - previous) / previous) * 100).toFixed(1) + "%";
}

// API 호출을 위한 일반화된 함수
async function secondDataForPeriod(start, end, title) {
  return new Promise((resolve, reject) => {
    getDongPerDate(
      accessToken.value,
      formatDate(start),
      formatDate(end),
      (res) => {
        if (res.data && res.data.status === "SUCCESS") {
          const data = res.data.data.list;
          const areaData = data.find((d) => d.areaGu === areaDetails.value.areaGu); // areaGu 동적 사용
          const counts = areaData.list.map((item) => item.count);
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

const firstData = async () => {
  try {
    // 데이터 가져오기
    const todayData = await secondDataForPeriod(today, today, "오늘");
    const yesterdayData = await secondDataForPeriod(yesterday, yesterday, "어제");
    const thisWeekData = await secondDataForPeriod(thisWeekStart, today, "최근 1주일");
    const lastWeekData = await secondDataForPeriod(lastWeekStart, lastWeekEnd, "지난 1주일");
    const thisMonthData = await secondDataForPeriod(thisMonthStart, today, "최근 1달");
    const lastMonthData = await secondDataForPeriod(lastMonthStart, lastMonthEnd, "지난 1달");

    // 증감률 계산
    const dailyChangePercent = calculatePercentChange(todayData.number, yesterdayData.number);
    const weeklyChangePercent = calculatePercentChange(thisWeekData.number, lastWeekData.number);
    const monthlyChangePercent = calculatePercentChange(thisMonthData.number, lastMonthData.number);

    // 데이터셋 저장
    IncidentData.value = [
      {
        title: "오늘",
        number: todayData.number,
        subtitle: "전날 대비",
        diff: yesterdayData.number,
        percent: dailyChangePercent,
      },
      {
        title: "최근 1주일",
        number: thisWeekData.number,
        subtitle: "전주 대비",
        diff: lastWeekData.number,
        percent: weeklyChangePercent,
      },
      {
        title: "최근 1달",
        number: thisMonthData.number,
        subtitle: "전달 대비",
        diff: lastMonthData.number,
        percent: monthlyChangePercent,
      },
    ];
    // console.log("위험물 발생 현황:", IncidentData.value);
  } catch (error) {
    console.error("Failed to fetch data:", error);
  }
};

onMounted(() => {
  fetchAreaDetails().then(() => {
    firstData();
  });
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
  padding: 3.1vh 15px;
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

/* ***** */
/* 위험물 발생 현황 */
.incident-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.2vh 5px 20px 5px;
  height: 177px;
  margin-bottom: 3.4vh;
}
.incident-title {
  margin: 1vh 0px 1.8vh 10px;
}
.incident-report {
  display: grid;
  margin-bottom: 20px;
  grid-template-columns: 1fr 1fr 1fr;
}
/* slide animation 효과 -> 왼쪽에서 오른쪽으로 이동 */
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

/* ***** */
/* 동별 포트홀 + 심각한 포트홀 현황 */
.danger-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.2vh 0px 0px 5px;
}
.title-input {
  display: flex;
  align-items: center;
  margin: 1vh 0px 0px 0px;
}
.road-title {
  margin: 0px 0px 0px 10px;
  padding-bottom: 15px;
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
  background-color: white;
  margin-left: 20px;
}
input:focus {
  outline: 0;
  border-color: #696969;
}

/* ***** */
/* 위험물 누적 탐지 건수 */
.total-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.2vh 5px 0px 5px;
  height: 360px;
  margin-bottom: 3.4vh;
}
.totalincident-title {
  margin: 1vh 0px 0px 10px;
}
.incident-graph {
  width: 93%;
}

/* 보수 공사 현황 */
.work-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.2vh 5px 0px 5px;
  height: 273.5px;
}
.work-title {
  margin: 1vh 0px 1vh 10px;
}
.work-chart {
  width: 100%;
}
</style>
