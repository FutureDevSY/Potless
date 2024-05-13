<template>
  <div id="map" class="map"></div>
</template>

<script setup>
import { onMounted, watch, toRefs, ref } from "vue";
import markerImageSrc from "../../../assets/icon/marker.png"; // 임포트된 이미지 경로 변수명 변경

const props = defineProps({
  potholeDirx: Number,
  potholeDiry: Number,
});

const { potholeDirx, potholeDiry } = toRefs(props);

const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;

let map = null;
let marker = null;

onMounted(() => {
  const script = document.createElement("script");
  script.onload = () => {
    window.kakao.maps.load(initializeMap); // initializeMap 호출 시점이 올바르게 조정
  };
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false`;
  document.head.appendChild(script);
});

function initializeMap() {
  const mapContainer = document.getElementById("map");
  const mapOption = {
    center: new window.kakao.maps.LatLng(
      potholeDirx.value || 33.450701,
      potholeDiry.value || 126.570667
    ),
    level: 3,
  };
  map = new window.kakao.maps.Map(mapContainer, mapOption);

  const imageSize = new window.kakao.maps.Size(40, 40);
  const imageOption = { offset: new window.kakao.maps.Point(20, 40) };
  const markerImage = new window.kakao.maps.MarkerImage(
    markerImageSrc,
    imageSize,
    imageOption
  ); // 변경된 변수명 사용

  marker = new window.kakao.maps.Marker({
    position: mapOption.center,
    image: markerImage,
  });

  marker.setMap(map);
  watch([potholeDirx, potholeDiry], updateMapLocation, {
    immediate: true,
  });
}

function updateMapLocation() {
  if (
    !map ||
    potholeDirx.value === undefined ||
    potholeDiry.value === undefined
  ) {
    console.log("Map or coordinates not ready");
    return;
  }
  const newCenterPoint = new window.kakao.maps.LatLng(
    potholeDirx.value,
    potholeDiry.value
  );
  map.setCenter(newCenterPoint);
  marker.setPosition(newCenterPoint);
}
</script>

<style scoped>
.map {
  width: 40.3vw;
  height: 70.5vh;
  margin-top: 0px;
  border: 2px solid rgb(223, 223, 223);
}
</style>
