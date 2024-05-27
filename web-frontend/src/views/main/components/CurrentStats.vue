<template>
  <div class="main-container">
    <div class="title">오늘 현황</div>

    <div class="main-box">
      <div class="danger">
        <div class="danger-box">
          <div class="circle severity">
            <div class="inner-circle">심각</div>
          </div>
          <div class="info-text end">{{ dangerCount }} 건</div>
        </div>
        <div class="danger-box">
          <div class="circle caution"><div class="inner-circle">주의</div></div>
          <div class="info-text mid">{{ cautiousCount }} 건</div>
        </div>
        <div class="danger-box">
          <div class="circle good"><div class="inner-circle">양호</div></div>
          <div class="info-text start">{{ safeCount }}건</div>
        </div>
      </div>

      <div class="type">
        <div class="infos borderb">
          <div class="name">포트홀</div>
          <div class="number">{{ potholeNum }}건</div>
        </div>
        <div class="infos borderb">
          <div class="name">도로 파손</div>
          <div class="number">0건</div>
        </div>
        <div class="infos">
          <div class="name">도로 마모</div>
          <div class="number">0건</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getDongPerDate, getSeverityPerDate } from "../../../api/statistics/statistics";

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
        potholeNum.value = currentData.value.list[0].count + currentData.value.list[1].count;
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
  padding: 2vh 0vw 0vh 0vw;
}

.title {
  color: #1e476d;
  font-size: 2.4vh;
  font-weight: bold;
  text-align: center;
}

.main-box {
  display: grid;
  margin-top: 0.7vh;
  grid-template-columns: 65% 35%;
  border: 1px solid #bcbcbc;
  border-left: none;
  border-right: none;
  border-bottom: none;
  height: 81%;
}

.danger {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  border-right: 1px solid #bcbcbc;
}

.danger-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.circle {
  height: 8vh;
  width: 8vh;
  border-radius: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}

.inner-circle {
  height: 5vh;
  width: 5vh;
  border-radius: 100%;
  background-color: white;
  color: #1e476d;
  font-size: 2vh;
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
  font-size: 2.4vh;
  text-align: center;
  width: 9vh;
  color: #1e476d;
  font-weight: bold;
  margin-top: 1vh;
}
.end,
.mid,
.start {
  color: #1e476d;
}

.type {
  display: grid;
  grid-template-rows: 33.5% 33.5% 32.9%;
}

.infos {
  display: flex;
  padding: 0vh 1.5vw 0vh 1vw;
}
.borderb {
  border-bottom: 1px solid #bcbcbc;
}
.name {
  color: #1e476d;
  padding: 1vh;
  font-size: 2vh;
}
.number {
  color: #1e476d;
  padding: 1vh;
  font-size: 2.1vh;
  margin-left: auto;
  font-weight: bold;
}
</style>
