<template>
  <div class="statics-container">
    <div class="left-box">
      <p class="incident-title">위험물 발생 현황</p>
      <div class="incident-report">
        <IncidentReport
          v-for="(item, index) in dataItems"
          :key="index"
          :title="item.title"
          :number="item.number"
          :subtitle="item.subtitle"
          :percent="item.percent"
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
import { ref, onMounted } from "vue";
import IncidentGraph from "./components/IncidentGraph.vue";
import IncidentReport from "./components/IncidentReport.vue";
import RoadTypeIncidentsGraphVue from "./components/RoadTypeIncidentsGraph.vue";
import TownTypeIncidentsGraph from "./components/TownTypeIncidentsGraph.vue";
import WorkChart from "./components/WorkChart.vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import {
  getGuList,
  getDongList,
  getTotalDongList,
  getDongDetail,
} from "../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

// 대전 시 전체 구 데이터
const GuData = ref(null);

// 포트홀 구에 속한 동에 대한 통계 조회 (구 1개, 동 여러개)
const DongListData = ref(null);

// 구 별로 상관없이 모든 동의 전체 통계 출력
const TotalDongListData = ref(null);

// 동 이름 검색하면 해당 동의 통계 출력
const DongDetailData = ref(null);

const TakeStatistics = () => {
  getGuList(
    accessToken.value,
    (res) => {
      console.log("구 전체 리스트 요청", res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        GuData.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );

  getDongList(
    accessToken.value,
    areaId.value,
    (res) => {
      console.log("동 리스트 요청", res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        DongListData.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );

  getTotalDongList(
    accessToken.value,
    (res) => {
      console.log("동 전체 정보", res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        TotalDongListData.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );

  // 동 쿼리 검색인데 쿼리 검색 뭐로 해야할지 안정해져있음
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
};

const dataItems = [
  { title: "오늘", number: "250", subtitle: "전날 대비", percent: "+30%" },
  { title: "1주일", number: "1,500", subtitle: "전주 대비", percent: "-3%" },
  { title: "1개월", number: "7,420", subtitle: "전달 대비", percent: "+10%" },
];

const searchTerm = ref("");
onMounted(() => {
  TakeStatistics();
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
