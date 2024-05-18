<template>
  <div class="statics-container">
    <div class="left-box">
      <div class="incident-box">
        <div class="title-and-lottie">
          <LottieRadar class="lottie lottie-radar" />
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
            @input="handleInput"
          />
        </div>
        <RoadTypeIncidentsGraphVue :search-term="searchTerm" :road-data="roadIncidentData" />
      </div>
    </div>
    <div class="right-box">
      <div class="total-box">
        <div class="title-and-lottie-cum">
          <LottieGraph class="lottie lottie-graph" />
          <p class="totalincident-title">위험물 누적 탐지 건수</p>
        </div>
        <div class="incident-graph">
          <IncidentGraph />
        </div>
      </div>

      <div class="work-box">
        <div class="title-and-lottie-cum">
          <LottieConstruction class="lottie lottie-construction" />
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
</template>

<script setup>
import { ref, onMounted } from "vue";
import IncidentGraph from "./components/IncidentGraph.vue";
import IncidentReport from "./components/IncidentReport.vue";
import RoadTypeIncidentsGraphVue from "./components/RoadTypeIncidentsGraph.vue";
import Inquire from "./components/Inquire.vue";
import WorkChart from "./components/WorkChart.vue";
import LottieRadar from "./components/LottieRadar.vue";
import LottieDanger from "./components/LottieDanger.vue";
import LottieGraph from "./components/LottieGraph.vue";
import LottieConstruction from "./components/LottieConstruction.vue";
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
    const DateParams = ref({
      start: formatDate(start),
      end: formatDate(end),
    });

    getDongPerDate(
      accessToken.value,
      DateParams.value,
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
        potholes: dong.countDamageBefore + dong.countDamageDuring,
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
      // console.log("도로별 포트홀 현황:", roadIncidentData.value);
      // console.log("보수 공사 현황:", workChartData.value);
    } else {
      console.error("API 호출 실패");
    }
  } catch (error) {
    console.error("통계 에러", error);
  }
};

const handleInput = (e) => {
  searchTerm.value = e.target.value;
};

onMounted(() => {
  fetchAreaDetails().then(() => {
    firstData();
    secondfourthData();
  });
});
</script>

<style scoped>
.statics-container {
  display: grid;
  padding: 1%;
  grid-template-columns: 49.2% 49.2%;
  gap: 25px;
  animation: fadein 1s;
}

@keyframes fadein {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.left-box {
  display: grid;
  grid-template-rows: 29% 68.4%;
  gap: 3%;
}

.right-box {
  display: grid;
  grid-template-rows: 60% 37%;
  gap: 3%;
}

.totalincident-title,
.incident-title,
.town-title,
.work-title,
.road-title {
  font-size: 2vh;
  font-weight: 600;
  color: #373737;
}

.title-and-lottie {
  display: grid;
  grid-template-columns: 8% 67.5%;
  gap: 10px;
  align-items: center;
}

.title-and-lottie-cum {
  display: grid;
  grid-template-columns: 7% 70.5%;
  gap: 10px;
  align-items: center;
}

.lottie {
  margin-left: 10px;
}
.lottie-danger {
  transform: translateY(-0.8vh);
}
.lottie-radar {
  transform: translateY(-1.5vh);
}

.lottie-graph {
  transform: translateY(-0.8vh);
}

.lottie-construction {
  transform: translateY(-0.8vh);
}

.incident-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 1.5vh 5px 0vh 0px;
  display: grid;
  grid-template-rows: 25% 65%;
  gap: 1vh;
}
.incident-title {
  margin: 1vh 0px 4vh 0px;
}
.incident-report {
  display: grid;
  margin: 0 0 0 10px;
  grid-template-columns: 1fr 1fr 1fr;
}

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

.danger-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 2vh 0px 0px 5px;
  display: grid;
  grid-template-rows: 10% 90%;
}
.title-input {
  display: grid;
  grid-template-columns: 6% 26% 100%;
  gap: 15px;
  align-items: center;
}
.road-title {
  margin: 0px 0px 0px 0px;
  padding-bottom: 15px;
}
input {
  width: 25%;
  padding: 1.8%;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #a1a1a1;
  transition: border 0.4s ease;
  font-size: 1.5vh;
  color: #373737;
  background-color: white;
  transform: translateY(-0.6vh);
}
input:focus {
  outline: 0;
  border-color: #696969;
}

.total-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 0.5vh 0px 0vh 5px;
  display: grid;
  grid-template-rows: 15% 65%;
  gap: 1vh;
}
.totalincident-title {
  margin: 0px 0px 0px 0px;
}

.work-box {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  padding: 2vh 0px 0vh 5px;
  display: grid;
  grid-template-rows: 15% 85%;
  gap: 1vh;
}
.work-title {
  margin: 0px 0px 0px 0px;
  padding-bottom: 15px;
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
  height: 85%;
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
