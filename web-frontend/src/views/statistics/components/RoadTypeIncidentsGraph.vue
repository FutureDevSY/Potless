<template>
  <div id="map" class="map"></div>
</template>

<script setup>
import { onMounted } from "vue";
const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;

onMounted(() => {
  const script = document.createElement("script");
  script.onload = () => initMap();
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false`;
  document.head.appendChild(script);
});

function initMap() {
  window.kakao.maps.load(() => {
    const mapContainer = document.getElementById("map");
    mapContainer.style.width = "100%";
    mapContainer.style.height = "610px";
    const mapOption = {
      center: new window.kakao.maps.LatLng(36.3549777, 127.2983403),
      level: 3,
      draggable: false,
      zoomable: false,
    };
    const map = new window.kakao.maps.Map(mapContainer, mapOption);
  });
}
</script>

<style scoped>
.map {
  width: 100%;
  height: auto;
}
</style>
