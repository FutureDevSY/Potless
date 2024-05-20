<template>
  <div class="main-container">
    <div class="info-container">
      <div class="title">오늘 현황</div>

      <div class="danger">
        <div>
          <div class="circle severity">심각</div>
          <div class="info-text end">{{ dangerCount }} 건</div>
        </div>
        <div>
          <div class="circle caution">주의</div>
          <div class="info-text mid">{{ cautiousCount }} 건</div>
        </div>
        <div>
          <div class="circle good">양호</div>
          <div class="info-text start">{{ safeCount }}건</div>
        </div>
      </div>
    </div>
    <div class="type">
      <div class="infos">
        <img
          class="icons"
          src="../../../assets/icon/pothole-icon.png"
          alt="pothole"
        />
        <div class="name">포트홀</div>
        <div class="number">{{ potholeNum }}건</div>
      </div>
      <div class="infos">
        <img
          class="icons"
          src="../../../assets/icon/road-icon.png"
          alt="road"
        />
        <div class="name">도로 파손</div>
        <div class="number">0건</div>
      </div>
      <div class="infos">
        <img
          class="icons"
          src="../../../assets/icon/line-icon.png"
          alt="line"
        />
        <div class="name">도로 마모</div>
        <div class="number">0건</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import {
  getDongPerDate,
  getSeverityPerDate,
} from "../../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

const currentData = ref([]);
const potholeNum = ref(0);
const now = new Date();
const year = now.getFullYear();
const month = ("0" + (now.getMonth() + 1)).slice(-2);
const day = ("0" + now.getDate()).slice(-2);
const today_start = year + "-" + month + "-" + (day - 1);
const today_end = year + "-" + month + "-" + day;

// 데이터 통계 불러오기 함수
const takeData = () => {
  const DateParams = ref({
    start: today_start,
    end: today_end,
  });

  getDongPerDate(
    accessToken.value,
    DateParams.value,
    (res) => {
      if (res.data.status === "SUCCESS") {
        currentData.value = res.data.data.list[areaId.value - 1];
        potholeNum.value = currentData.value.list[0].count;
      } else {
        // console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

const dangerCount = ref(0);
const cautiousCount = ref(0);
const safeCount = ref(0);

// 심각도 불러오기 함수
const severityData = () => {
  getSeverityPerDate(
    accessToken.value,
    areaId.value,
    (res) => {
      if (res.data.status === "SUCCESS") {
        // console.log(res.data.message);
        dangerCount.value = res.data.data.severityThree;
        cautiousCount.value = res.data.data.severityTwo;
        safeCount.value = res.data.data.severityOne;
      } else {
        // console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
};

onMounted(() => {
  severityData();
  takeData();
});
</script>

<style scoped>
.main-container {
  background-color: #ffffff;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
  padding: 1.5vh 0vw 0vh 0vw;
  display: grid;
  grid-template-columns: 55% 30%;
  gap: 8%;
}
.title {
  color: #373737;
  font-size: 2vh;
  font-weight: bold;
  padding-left: 1vw;
}
.info-container {
  display: grid;
  grid-template-rows: 12% 82%;
  gap: 6%;
}
.danger {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  padding-left: 20px;
  margin-top: 1.5vh;
}
.circle {
  height: 7.5vh;
  width: 7.5vh;
  border-radius: 100%;
  margin-left: 8px;
  color: white;
  font-size: 2.2vh;
  font-weight: bold;
  display: flex;
  justify-content: center;
  align-items: center;
}
.severity {
  background-color: #ff4e4e;
}
.caution {
  background-color: #ffb931;
}
.good {
  background-color: #8dc63f;
}
.info-text {
  font-size: 2vh;
  text-align: center;
  width: 9vh;
  color: #373737;
  font-weight: bold;
  margin-top: 1.4vh;
}
.end,
.mid,
.start {
  color: #595959;
}

.type {
  padding-top: 1vh;
  display: grid;
  grid-template-rows: 33% 33% 33%;
}
.icons {
  height: 4vh;
}
.infos {
  display: flex;
  padding-bottom: 1px;
}
.name {
  color: #373737;
  padding: 1vh;
  font-size: 1.8vh;
}
.number {
  color: #373737;
  padding: 1vh;
  font-size: 2vh;
  margin-left: auto;
  font-weight: bold;
}
</style>
