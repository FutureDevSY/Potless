<template>
  <div id="map" class="map"></div>
</template>

<script setup>
import { onMounted, watch, toRefs, nextTick } from "vue";
import markerImageSrc from "../../../assets/icon/marker.png";

const props = defineProps({
  potholeDirx: Number,
  potholeDiry: Number,
  currentData: Array,
});

const { potholeDirx, potholeDiry, currentData } = toRefs(props);

const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;
let map = null;
let markers = [];

onMounted(() => {
  loadKakaoMapsAPI();
});

function loadKakaoMapsAPI() {
  if (!window.kakao || !window.kakao.maps) {
    const script = document.createElement("script");
    script.onload = () => {
      window.kakao.maps.load(initializeMap);
    };
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false`;
    document.head.appendChild(script);
  } else {
    initializeMap();
  }
}

function initializeMap() {
  nextTick(() => {
    const mapContainer = document.getElementById("map");
    if (mapContainer) {
      map = new window.kakao.maps.Map(mapContainer, {
        center: new window.kakao.maps.LatLng(33.450701, 126.570667),
        level: 6,
      });
      updateMarkers();
    }
  });
}

function updateMarkers() {
  if (!map || !window.kakao || !window.kakao.maps) {
    console.error("Kakao Maps API is not fully loaded.");
    return;
  }

  markers.forEach((marker) => marker.setMap(null));
  markers = [];

  if (currentData.value && currentData.value.length) {
    let avgX = 0,
      avgY = 0;
    currentData.value.forEach((data) => {
      avgX += data.dirX;
      avgY += data.dirY;
      const markerPosition = new window.kakao.maps.LatLng(data.dirY, data.dirX);
      const marker = new window.kakao.maps.Marker({
        position: markerPosition,
        map: map,
        image: new window.kakao.maps.MarkerImage(
          markerImageSrc,
          new window.kakao.maps.Size(40, 40),
          { offset: new window.kakao.maps.Point(20, 40) }
        ),
      });
      markers.push(marker);
    });
    avgX /= currentData.value.length;
    avgY /= currentData.value.length;
    map.setCenter(new window.kakao.maps.LatLng(avgY, avgX));
  }
}

watch(currentData, updateMarkers, { deep: true, immediate: true });

watch(
  [potholeDirx, potholeDiry],
  () => {
    if (map && potholeDirx.value !== undefined && potholeDiry.value !== undefined) {
      const newCenterPoint = new window.kakao.maps.LatLng(potholeDirx.value, potholeDiry.value);
      map.setCenter(newCenterPoint);
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.map {
  width: 40.3vw;
  height: 70.5vh;
  margin-top: 0px;
  border: 2px solid rgb(223, 223, 223);
}
</style>
