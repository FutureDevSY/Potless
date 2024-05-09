<template>
  <div class="map-container">
    <div class="search-container">
      <input
        type="text"
        v-model="query"
        @keyup.enter="searchAddress"
        placeholder="검색어 입력"
      />
      <ul v-if="results.length > 0">
        <li v-for="item in results" :key="item.id" @click="findLocation(item)">
          {{ item.place_name }} - {{ item.address_name }}
        </li>
      </ul>
    </div>
    <div id="map" style="width: 100%; height: 700px"></div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted, watch } from "vue";

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
      center: new kakao.maps.LatLng(36.3504, 127.3845),
      level: 8,
    };
    map.value = new kakao.maps.Map(container, options);
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
.map-container {
  display: flex;
  max-width: 100%;
  margin: auto;
  max-height: 900px;
  align-items: start;
  justify-content: space-between;
}
#map {
  max-width: 900px;
}

.search-container ul {
  list-style: none;
  padding-left: 0;
  max-height: 500px;
  overflow-y: auto;
  margin-top: 10px;
}

.search-container li {
  background-color: #f9f9f9;
  border-bottom: 1px solid #ccc;
  padding: 8px;
  transition: background-color 0.3s;
}

.search-container li:hover {
  background-color: #f1f1f1;
  cursor: pointer;
}

.search-container li:last-child {
  border-bottom: none;
}

.search-container li a {
  text-decoration: none;
  color: #333;
  display: block;
}

.search-container li a:hover {
  color: #000;
}
</style>
