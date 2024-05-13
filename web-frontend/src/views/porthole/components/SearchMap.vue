<template>
  <div class="maps-container">
    <div class="inputs-group">
      <div class="inputs-title">위험물의 주소/위치를 입력해 주세요 :</div>
      <div class="search-container">
        <input type="text" v-model="query" @keyup.enter="searchAddress" placeholder="주소/위치" />
        <ul v-if="results.length > 0">
          <li v-for="item in results" :key="item.id" @click="findLocation(item)">
            {{ item.place_name }} - {{ item.address_name }}
          </li>
        </ul>
      </div>
    </div>

    <div id="map" style="width: 98%; height: 400px"></div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted, watch } from "vue";

const emit = defineEmits(["updateCenter"]);
const query = ref("");
const results = ref([]);
const map = ref(null);
const marker = ref(null);
const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;
const KAKAO_REST_API_KEY = import.meta.env.VITE_KAKAO_REST_API_KEY;

const loadKakaoMapsApi = () => {
  return new Promise((resolve, reject) => {
    const script = document.createElement("script");
    script.onload = () => resolve(window.kakao);
    script.onerror = () => reject(new Error("Failed to load Kakao Maps API"));
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false&libraries=services`;
    document.head.appendChild(script);
  });
};

const initMap = () => {
  window.kakao.maps.load(() => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(36.3556142, 127.2985695),
      level: 3,
    };
    map.value = new kakao.maps.Map(container, options);
    marker.value = new kakao.maps.Marker({
      position: map.value.getCenter(),
      map: map.value,
    });

    // 맵의 중심이 변경될 때마다 마커를 중심으로 이동
    kakao.maps.event.addListener(map.value, "center_changed", () => {
      const newCenter = map.value.getCenter();
      marker.value.setPosition(newCenter);
      emit("updateCenter", { x: newCenter.getLng(), y: newCenter.getLat() });
    });
  });
};

onMounted(async () => {
  try {
    await loadKakaoMapsApi();
    initMap();
  } catch (error) {
    console.error("Kakao Maps API loading failed:", error);
  }
});

const searchAddress = async () => {
  const apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json";
  try {
    const response = await axios.get(apiUrl, {
      headers: {
        Authorization: `KakaoAK ${KAKAO_REST_API_KEY}`,
      },
      params: {
        query: query.value,
      },
    });
    results.value = response.data.documents;
  } catch (error) {
    console.error("Error:", error);
  }
};

const findLocation = (item) => {
  const newPos = new kakao.maps.LatLng(item.y, item.x);
  map.value.setCenter(newPos);
};
</script>

<style scoped>
.maps-container {
  width: 100%;
  height: 100%;
  position: relative;
  /* display: flex;
  max-width: 100%;
  margin: auto;
  max-height: 600px;
  align-items: start; */
}

input {
  margin: 10px;
  width: 150px;
  height: 4.5vh;
  border-radius: 5px;
  font-size: 1.8vh;
  border: 1px solid rgb(91, 91, 91);
  padding: 0vh 0vw 0 3vw;
}

.search-container {
  z-index: 10;
}

.search-container ul {
  height: 40vh;
  position: absolute;
  top: 7%;
  left: 130px;
  width: 80%;
  overflow-y: auto;
  padding-left: 0;
}

.search-container li {
  background-color: #f9f9f9;
  padding: 2.5vh 2vw;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s;
  border: 1px solid rgb(131, 131, 131);
}

.search-container li:hover {
  background-color: #f1f1f1;
  cursor: pointer;
}

.search-container li a {
  text-decoration: none;
  color: #333;
  display: block;
}

.inputs-group {
  display: flex;
  align-items: center;
}
.inputs-title {
  width: 100%;
  font-size: 2vh;
  font-weight: 500;
  color: #373737;
}
</style>
