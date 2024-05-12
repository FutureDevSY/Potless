<template>
  <div class="main-container" :style="{ backgroundColor: backgroundColor }" v-if="weather">
    <div class="location-box">
      <img class="marker-img" src="../../../assets/icon/location.png" alt="marker" />
      <div class="location">{{ selectedRegionName }}</div>
    </div>
    <div class="info-container">
      <img
        class="weather-img"
        v-if="weatherImage"
        :src="weatherImage"
        alt="Weather condition image"
      />
      <div class="weather-info">
        <div class="temperature">{{ formattedTemperature }}°C</div>
        <div class="cloud">{{ cloudDescription }}</div>
        <div class="wind">풍속: {{ weather.wind.speed }} m/s</div>
      </div>
    </div>
  </div>
  <div v-else>날씨 정보를 불러오는 중입니다...</div>
</template>
<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getWeatherInfo } from "../../../api/weather/weather";

const store = useAuthStore();
const { areaId } = storeToRefs(store);

const weather = ref(null);
const selectedRegionName = ref(""); // 지역 이름 저장
const weatherImage = ref("");

const coordinates = {
  1: { lat: 36.3511, lon: 127.385, name: "대덕구" }, // 대덕구
  2: { lat: 36.3275, lon: 127.4348, name: "동구" }, // 동구
  3: { lat: 36.3514, lon: 127.4197, name: "중구" }, // 중구
  4: { lat: 36.3623, lon: 127.355, name: "유성구" }, // 유성구
  5: { lat: 36.3515, lon: 127.3868, name: "서구" }, // 서구
  6: { lat: 37.5665, lon: 126.978, name: "서울" }, // 서울 (기타)
};

const backgroundColor = computed(() => {
  if (!weather.value) return "#0095FF";
  switch (weather.value.weather[0].main) {
    case "Clear":
      return "#00CFF5";
    case "Clouds":
      return "#079CDF";
    case "Rain":
      return "#0071B1";
    case "Thunderstorm":
      return "#28728F";
    case "Snow":
      return "#75CCDC";
    case "Mist":
      return "#73DADD";
    case "Fog":
      return "#73DADD";
    case "Haze":
      return "#73DADD";
    default:
      return "#0095FF";
  }
});

// 날씨에 따른 이미지 설정
const updateWeatherImage = async () => {
  if (!weather.value) {
    weatherImage.value = "";
    return;
  }
  const main = weather.value.weather[0].main;
  try {
    switch (main) {
      case "Clear":
        weatherImage.value = (await import("../../../assets/weather/sunny.png")).default;
        break;
      case "Clouds":
        weatherImage.value = (await import("../../../assets/weather/clouds.png")).default;
        break;
      case "Rain":
        weatherImage.value = (await import("../../../assets/weather/rain.png")).default;
        break;
      case "Thunderstorm":
        weatherImage.value = (await import("../../../assets/weather/thunderstorm.png")).default;
        break;
      case "Snow":
        weatherImage.value = (await import("../../../assets/weather/snow.png")).default;
        break;
      case "Mist":
        weatherImage.value = (await import("../../../assets/weather/mist-fog-haze.png")).default;
        break;
      case "Fog":
        weatherImage.value = (await import("../../../assets/weather/mist-fog-haze.png")).default;
        break;
      case "Haze":
        weatherImage.value = (await import("../../../assets/weather/mist-fog-haze.png")).default;
        break;
      default:
        weatherImage.value = "";
    }
  } catch (e) {
    console.error("이미지 로드 실패", e);
  }
};

onMounted(() => {
  if (areaId.value && coordinates[areaId.value]) {
    const { lat, lon, name } = coordinates[areaId.value];
    selectedRegionName.value = name;
    getWeatherInfo(
      lat,
      lon,
      (response) => {
        weather.value = response.data;
      },
      (error) => {
        console.error("날씨 정보를 불러오는 데 실패했습니다:", error);
      }
    );
  } else {
    console.log("잘못된 areaId 값입니다.");
    selectedRegionName.value = "잘못된 위치";
  }
});

// 온도 포맷팅
const formattedTemperature = computed(() => {
  if (!weather.value) return "";
  const temp = parseFloat(weather.value.main.temp);
  const roundedTemp = temp.toFixed(1);
  // 소수 첫째 자리가 0일 경우 정수 부분만 반환
  return roundedTemp.endsWith(".0") ? Math.round(temp).toString() : roundedTemp;
});

// 구름 양
const cloudDescription = computed(() => {
  const cloudiness = weather.value ? weather.value.clouds.all : 0;
  if (cloudiness <= 10) return "맑음";
  else if (cloudiness <= 25) return "구름 조금";
  else if (cloudiness <= 50) return "구름 약간";
  else if (cloudiness <= 85) return "구름 많음";
  else return "흐림";
});

// 날씨 데이터 변경 감지
watch(weather, updateWeatherImage);
</script>
<style scoped>
.main-container {
  transition: background-color 0.5s ease;
  border-radius: 15px;
  padding: 1.5vh;
  display: grid;
  grid-template-rows: 15% 82%;
  gap: 3%;
}
.location-box {
  display: flex;
}
.marker-img {
  height: 2.5vh;
}
.location {
  color: white;
  font-size: 1.8vh;
  font-weight: bold;
  margin-left: 5px;
}
.info-container {
  display: grid;
  grid-template-columns: 62% 38%;
}
.weather-img {
  display: flex;
  height: 15vh;
  padding: 0vh 0vh 0vh 2vw;
}
.weather-info {
  color: white;
}
.temperature {
  font-size: 4.8vh;
  font-weight: bold;
  margin-bottom: 1vh;
}
.cloud,
.wind {
  font-size: 1.8vh;
}
</style>
