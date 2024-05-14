<template>
  <div class="maps-container">
    <div class="inputs-group">
      <div class="inputs-title">위험물의 주소/위치를 입력해 주세요 :</div>
      <div class="search-container">
        <input type="text" v-model="query" placeholder="주소/위치" />
        <button type="button" @click="searchAddress">위치 찾기</button>
        <ul v-if="results.length > 0">
          <li
            v-for="item in results"
            :key="item.id"
            @click="selectLocation(item)"
          >
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
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";

const store2 = useAuthStore();
const { coordinates } = storeToRefs(store2);
const emit = defineEmits(["updateCenter"]);
const query = ref("");
const results = ref([]);
const map = ref(null);
const marker = ref(null);
const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;
const KAKAO_REST_API_KEY = import.meta.env.VITE_KAKAO_REST_API_KEY;

async function loadKakaoMapsApi() {
  const script = document.createElement("script");
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false&libraries=services`;
  document.head.appendChild(script);
  return new Promise((resolve, reject) => {
    script.onload = () => resolve(window.kakao);
    script.onerror = () => reject(new Error("Failed to load Kakao Maps API"));
  });
}

function initMap() {
  window.kakao.maps.load(() => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(coordinates.value.y, coordinates.value.x),
      level: 3,
    };
    map.value = new kakao.maps.Map(container, options);
    marker.value = new kakao.maps.Marker({
      position: map.value.getCenter(),
      map: map.value,
    });

    kakao.maps.event.addListener(map.value, "center_changed", () => {
      const newCenter = map.value.getCenter();
      marker.value.setPosition(newCenter);
      emit("updateCenter", { x: newCenter.getLng(), y: newCenter.getLat() });
    });
  });
}

onMounted(async () => {
  try {
    await loadKakaoMapsApi();
    initMap();
  } catch (error) {
    console.error("Kakao Maps API loading failed:", error);
  }
});

async function searchAddress() {
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
}

function selectLocation(item) {
  const newPos = new kakao.maps.LatLng(item.y, item.x);
  map.value.setCenter(newPos);
  results.value = []; // Clears the results to hide the list
}
</script>

<style scoped>
.maps-container {
  width: 100%;
  height: 100%;
  position: relative;
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

.inputs-group {
  display: flex;
  align-items: center;
}

.inputs-title {
  font-size: 2vh;
  font-weight: 500;
  color: #373737;
}
</style>
