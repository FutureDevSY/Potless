<template>
  <div id="map" class="map"></div>
</template>

<script setup>
import { onMounted, toRefs, watchEffect } from "vue";

const props = defineProps({
  potholeDirx: Number,
  potholeDiry: Number,
});

const { potholeDirx, potholeDiry } = toRefs(props);

const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;

onMounted(() => {
  const script = document.createElement("script");
  script.onload = () => {
    window.kakao.maps.load(() => {
      watchEffect(() => initMap());
    });
  };
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false`;
  document.head.appendChild(script);
});

function initMap() {
  const mapContainer = document.getElementById("map");
  console.log("Coordinates: ", potholeDirx.value, potholeDiry.value);
  if (
    !mapContainer ||
    potholeDirx.value === undefined ||
    potholeDiry.value === undefined
  ) {
    console.log("Map or coordinates not ready");
    return;
  }

  const centerPoint = new window.kakao.maps.LatLng(
    potholeDirx.value,
    potholeDiry.value
  );
  const mapOption = {
    center: new window.kakao.maps.LatLng(potholeDirx.value, potholeDiry.value),
    level: 3,
  };
  const map = new window.kakao.maps.Map(mapContainer, mapOption);
  // 마커 생성 및 지도 중심에 마커 표시
  const marker = new window.kakao.maps.Marker({
    position: centerPoint,
  });
  marker.setMap(map);
}
</script>

<style scoped>
.map {
  width: 100%;
  height: 100%;
}
</style>
