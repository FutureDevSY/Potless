<template>
  <div>
    <div id="map" class="map"></div>
    <button class="btn-style" @click="toggleIsCount">
      {{ isCount ? "위험도 확인" : "신고중복 확인" }}
    </button>
  </div>
</template>

<script setup>
import { onMounted, watch, toRefs, nextTick, ref } from "vue";
import markerImageSrc from "../../../assets/icon/marker.png";
import markerImageSrc1 from "../../../assets/icon/marker_cautious.png";
import markerImageSrc2 from "../../../assets/icon/marker_safe.png";

import markerImageSrc3 from "../../../assets/icon/count_danger.png";
import markerImageSrc4 from "../../../assets/icon/count_cautious.png";
import markerImageSrc5 from "../../../assets/icon/count_safe.png";

const isCount = ref(false);

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

function toggleIsCount() {
  isCount.value = !isCount.value;
  updateMarkers();
}

function updateMarkerImage(data) {
  let imageSrc;
  if (isCount.value) {
    if (data.count === 0) {
      imageSrc = markerImageSrc5;
    } else if (data.count >= 1 && data.count <= 4) {
      imageSrc = markerImageSrc4;
    } else if (data.count >= 5) {
      imageSrc = markerImageSrc3;
    }
  } else {
    switch (data.severity) {
      case 1:
        imageSrc = markerImageSrc2;
        break;
      case 2:
        imageSrc = markerImageSrc1;
        break;
      case 3:
        imageSrc = markerImageSrc;
        break;
      default:
        imageSrc = markerImageSrc;
    }
  }
  return new window.kakao.maps.MarkerImage(imageSrc, new window.kakao.maps.Size(40, 40), {
    offset: new window.kakao.maps.Point(20, 40),
  });
}

function updateMarkers() {
  markers.forEach((marker) => marker.setMap(null));
  markers = [];

  if (currentData.value && currentData.value.length) {
    let avgX = 0;
    let avgY = 0;

    currentData.value.forEach((data) => {
      avgX += data.dirX;
      avgY += data.dirY;
      const markerPosition = new window.kakao.maps.LatLng(data.dirY, data.dirX);
      const markerImage = updateMarkerImage(data);

      const marker = new window.kakao.maps.Marker({
        position: markerPosition,
        map: map,
        image: markerImage,
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
  width: 37.3vw;
  height: 70vh;
  margin-top: 0px;
  border: 2px solid rgb(223, 223, 223);
}

.btn-style {
  padding: 1vh 0px;
  width: 7.6vw;
  background-color: #56acff;
  border: none;
  color: #ffffff;
  font-size: 1.6vh;
  border-radius: 4px;
  cursor: pointer;
  position: absolute;
  bottom: 15.7%;
  right: 4%;
  z-index: 10;
}

.btn-style:hover {
  background-color: #1e90ff;
}
</style>
