<template>
  <div class="map-container">
    <div class="header">
      <img class="logo" src="../../assets/icon/weblogo.png" alt="#" />
      <button class="login-btn" @click="store.moveLogin">로그인</button>
    </div>
    <SVG></SVG>
  </div>
</template>

<script setup>
import SVG from "./components/SVG.vue";
import { ref } from "vue";
import { useMoveStore } from "@/stores/move";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getWeatherInfo } from "../../api/weather/weather";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
// 여기에 통계 정보 들어감
const currentWeather = ref(null);
const hourlyWeather = ref([]);

const weatherData = () => {};
</script>

<style scoped>
.header {
  display: flex;
  margin-left: auto;
  align-items: center;
}

.map-container {
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: #f3f3f3;
}

.logo {
  margin-top: 3.5vh;
  width: 22.5vw;
  margin-right: 25.73vw;
}

.login-btn {
  background-color: #151c62;
  width: 9vw;
  height: 5.5vh;
  cursor: pointer;
  border-radius: 8px;
  font-size: 18px;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 3vh;
  margin-right: 4vw;
}

.login-btn:hover {
  background-color: #0e1241;
}
</style>
