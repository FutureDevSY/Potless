<template>
  <body>
    <div class="main-layout">
      <Navbar2 />
      <div class="statics-container">
        <div class="left-box">
          <div class="incident-box">
            <div class="title-and-lottie">
              <LottieRadar class="lottie" />
              <p class="incident-title">위험물 발생 현황</p>
            </div>
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
              <LottieDanger class="lottie lottie-danger" />
              <p class="road-title">지역별 위험물 건수</p>
              <input
                type="text"
                v-model="searchTerm"
                placeholder="동을 입력해주세요."
                @input="updateChart"
              />
            </div>
            <RoadTypeIncidentsGraphVue :search-term="searchTerm" :road-data="roadIncidentData" />
          </div>
        </div>
        <div class="right-box">
          <div class="total-box">
            <div class="title-and-lottie">
              <LottieGraph class="lottie" />
              <p class="totalincident-title">위험물 누적 탐지 건수</p>
            </div>
            <div class="incident-graph">
              <IncidentGraph />
            </div>
          </div>

          <div class="work-box">
            <div class="title-and-lottie">
              <LottieConstruction class="lottie" />
              <p class="work-title">보수 공사 현황</p>
            </div>
            <div class="chart-and-data">
              <WorkChart class="work-chart" :data="workChartData" />
              <div class="data-list">
                <transition-group name="data-list" tag="div" class="data-entries">
                  <Inquire
                    v-for="(item, index) in workChartData"
                    :key="index"
                    :title="item.title"
                    :number="item.number"
                    class="infos"
                  />
                </transition-group>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Navbar2 from "../common/Navbar2.vue";
import IncidentGraph from "../statistics/components/IncidentGraph.vue";
import IncidentReport from "../statistics/components/IncidentReport.vue";
import RoadTypeIncidentsGraphVue from "../statistics/components/RoadTypeIncidentsGraph.vue";
import Inquire from "../statistics/components/Inquire.vue";
import WorkChart from "../statistics/components/WorkChart.vue";
import LottieRadar from "../statistics/components/LottieRadar.vue";
import LottieDanger from "../statistics/components/LottieDanger.vue";
import LottieGraph from "../statistics/components/LottieGraph.vue";
import LottieConstruction from "../statistics/components/LottieConstruction.vue";
import { format, subDays } from "date-fns";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getAreaDetails, getDongList, getDongPerDate } from "../../api/statistics/statistics";

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

// ** 지역별 발생 건수: 포트홀 구에 속한 동에 대한 통계 조회
// ** 보수 공사 현황: 포트홀 구에 속한 동에 대한 통계 조회
const workChartData = ref([]);
const roadIncidentData = ref([]);

const secondfourthData = async () => {
  try {
    const response = await getDongList(accessToken.value, areaId.value);
    if (response && response.data && response.data.data && response.data.data.list) {
      const { list } = response.data.data;
      let countDone = 0,
        countDuring = 0,
        countBefore = 0;

      roadIncidentData.value = list.map((dong) => ({
        dong: dong.locationName,
        potholes: dong.countDamageBefore,
        severity: dong.severityCount,
      }));

      list.forEach((item) => {
        countDone += item.countDamageDone;
        countDuring += item.countDamageDuring;
        countBefore += item.countDamageBefore;
      });

      workChartData.value = [
        { title: "미완료", number: countBefore.toString() },
        { title: "보수중", number: countDuring.toString() },
        { title: "완료", number: countDone.toString() },
      ];
      console.log("도로별 포트홀 현황:", roadIncidentData.value);
      // console.log("보수 공사 현황:", workChartData.value);
    } else {
      console.error("Invalid or empty data received from the API");
    }
  } catch (error) {
    console.error("Error fetching Dong statistics:", error);
  }
};

onMounted(() => {
  fetchAreaDetails().then(() => {
    firstData();
    secondfourthData();
  });
});
</script>

<style scoped>
body {
  font-family: "Nunito", sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #d3d5ed;
  background-repeat: no-repeat;
  background-size: cover;
  /* background: #212140; */
}

.main-layout {
  display: grid;
  grid-template-columns: 12% 88%;
  width: 100%;
  margin: 25px;
  background: rgb(254, 254, 254);
  box-shadow: 0 0.5px 0 1px rgba(255, 255, 255, 0.23) inset,
    0 1px 0 0 rgba(255, 255, 255, 0.66) inset, 0 4px 16px rgba(0, 0, 0, 0.12);
  border-radius: 15px;
  z-index: 10;
  display: grid;
}

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

.title-and-lottie {
  display: flex;
  align-items: center;
}
.lottie {
  margin-left: 10px;
}
.lottie-danger {
  transform: translateY(-5px);
}

/* ***** */
/* 위험물 발생 현황 */
.incident-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.2vh 5px 8px 5px;
  height: 177px;
  margin-bottom: 3.4vh;
}
.incident-title {
  margin: 1vh 0px 1.8vh 0px;
}
.incident-report {
  display: grid;
  margin-bottom: 20px;
  grid-template-columns: 1fr 1fr 1fr;
}
/* slide animation 효과 -> 왼쪽에서 오른쪽으로 이동 */
@keyframes slideIn {
  from {
    transform: translateX(-40px);
    opacity: 0;
  }
  to {
    transform: translateX(0px);
    opacity: 1;
  }
}
.incident-report-enter-active {
  animation: slideIn 1s ease-out forwards;
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
  margin: 0px 0px 0px 0px;
  padding-bottom: 15px;
}
input {
  width: 25%;
  padding: 9px;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #a1a1a1;
  transition: border 0.4s ease;
  font-size: 14px;
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
  margin: 1vh 0px 0px 0px;
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
  margin: 1vh 0px 1vh 0px;
}
.chart-and-data {
  display: grid;
  grid-template-columns: 5fr 7fr;
  height: 90%;
}
.work-chart {
  height: 100%;
}
.data-list {
  border-left: 1px solid #ccc;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
@keyframes slideInRightToLeft {
  from {
    transform: translateX(40px);
    opacity: 0;
  }
  to {
    transform: translateX(0px);
    opacity: 1;
  }
}
.data-list-enter-active {
  animation: slideInRightToLeft 1s ease-out forwards;
}
.data-list-enter {
  opacity: 0;
}
</style>
