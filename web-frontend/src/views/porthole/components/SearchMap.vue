<template>
  <div class="maps-container">
    <div class="inputs-group">
      <div class="search-container">
        <input
          type="text"
          v-model="query"
          placeholder="위험물 주소(위치)를 입력해 주세요."
          @input="handleInput"
        />
        <!-- <button type="button" @click="searchAddress">위치 찾기</button> -->
        <ul v-if="results.length > 0">
          <li v-for="item in results" :key="item.id" @click="selectLocation(item)">
            {{ item.place_name }} - {{ item.address_name }}
          </li>
        </ul>
      </div>
    </div>

    <div id="map" class="map-style"></div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import markerImageSrc from "../../../assets/icon/marker.png";

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
      image: new window.kakao.maps.MarkerImage(markerImageSrc, new window.kakao.maps.Size(40, 40), {
        offset: new window.kakao.maps.Point(20, 40),
      }),
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

const handleInput = async (e) => {
  query.value = e.target.value;
  if (query.value.trim() !== "") {
    await searchAddress();
  } else {
    results.value = [];
  }
};

function selectLocation(item) {
  const newPos = new kakao.maps.LatLng(item.y, item.x);
  map.value.setCenter(newPos);
  results.value = [];
}
</script>

<style scoped>
.maps-container {
  width: 100%;
  height: 100%;
  position: relative;
  border-radius: 6px;
}

input {
  position: absolute;
  top: 1.2vh;
  left: 8%;
  width: 97%;
  height: 4.5vh;
  border-radius: 6px;
  font-size: 1.4vh;
  border: 1px solid rgb(223, 223, 223);
  padding: 0vh 0vw 0 15px;
  background-color: rgba(255, 255, 255, 0.788);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.211);
}

input:focus {
  border: 2px solid #0070e88b;
  outline: none;
}

.search-container {
  z-index: 10;
  width: 100%;
}

.search-container ul {
  max-height: 40vh;
  position: absolute;
  top: 5vh;
  left: 8.2%;
  width: 100%;
  overflow-y: auto;
  padding-left: 0;
  border: 2px solid #0070e88b;
  border-radius: 6px;
}

.search-container li {
  background-color: #ffffff;
  padding: 2.8vh 0.5vw;
  display: flex;
  justify-content: center;
  font-size: 1.6vh;
  color: #2f2f2f;
  text-align: center;
  transition: background-color 0.3s;
}

.search-container li:hover {
  background-color: #ebeaea;
  cursor: pointer;
}

.inputs-group {
  position: absolute;
  display: flex;
  align-items: center;
  width: 86.7%;
}

.inputs-title {
  font-size: 2vh;
  font-weight: 500;
  color: #565656;
}
.map-style {
  width: 100%;
  height: 100%;
  border-radius: 6px;
  border: 2px solid #dedede;
}
</style>
