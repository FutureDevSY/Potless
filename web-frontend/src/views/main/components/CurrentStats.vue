<template>
  <div class="main-container">
    <div class="info-container">
      <div class="title">오늘 현황</div>

      <div class="danger">
        <div>
          <img class="bad-img" src="../../../assets/icon/number-three.png" alt="bad" />
          <div class="info-text end">심각 - 4건</div>
        </div>
        <div>
          <img class="soso-img" src="../../../assets/icon/number-two.png" alt="soso" />
          <div class="info-text mid">보통 - 4건</div>
        </div>
        <div>
          <img class="good-img" src="../../../assets/icon/number-one.png" alt="good" />
          <div class="info-text start">양호 - 4건</div>
        </div>
      </div>
    </div>
    <div class="type">
      <div class="infos">
        <img class="icons" src="../../../assets/icon/pothole-icon.png" alt="pothole" />
        <div class="name">포트홀</div>
        <div class="number">{{ potholeNum }}건</div>
      </div>
      <div class="infos">
        <img class="icons" src="../../../assets/icon/road-icon.png" alt="road" />
        <div class="name">도로 파손</div>
        <div class="number">0건</div>
      </div>
      <div class="infos">
        <img class="icons" src="../../../assets/icon/line-icon.png" alt="line" />
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
import { getDongPerDate } from "../../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

const currentData = ref([]);
const potholeNum = ref(0);
const now = new Date();
const year = now.getFullYear()
const month = ('0' + (now.getMonth() + 1)).slice(-2)
const day = ('0' + now.getDate()).slice(-2)
const today = year + '-' + month  + '-' + day
const takeData = () => {
  getDongPerDate(
    accessToken.value,
    today,
    today,
    (res) => {
        if (res.data.status === "SUCCESS") {
            currentData.value = res.data.data.list[areaId.value - 1];
            potholeNum.value = currentData.value.list[0].count
        } else {
            console.log(res.data.message);
        }
    },
    (error) => {
        console.log(error.response.data.message);
    }
  );
}

onMounted(() => {
    takeData();
})
</script>

<style scoped>
.main-container {
  background-color: rgb(241, 241, 241);
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
.bad-img,
.soso-img,
.good-img {
  height: 8vh;
  padding-left: 5px;
}
.info-text {
  font-size: 1.9vh;
  text-align: center;
  width: 9vh;
  color: #373737;
  font-weight: bold;
  margin-top: 1vh;
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
