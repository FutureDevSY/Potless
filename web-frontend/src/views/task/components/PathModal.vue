<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div id="map" style="width: 100%; height: 400px"></div>
      <button @click="closeModal">닫기</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";

const store = useAuthStore();
const { coordinates } = storeToRefs(store);
const KAKAO_APP_KEY = import.meta.env.VITE_KAKAO_APP_KEY;

const props = defineProps({
  pathData: Array,
  wayPoint: Array,
});

const emit = defineEmits(["close"]);

const closeModal = () => {
  emit("close");
};

onMounted(() => {
  loadKakaoMapsAPI()
    .then(() => {
      drawMap();
    })
    .catch((error) => {
      console.error("Failed to load Kakao Maps:", error);
    });
});

async function loadKakaoMapsAPI() {
  if (window.kakao && window.kakao.maps) {
    return Promise.resolve();
  }

  return new Promise((resolve, reject) => {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&libraries=services,clusterer,drawing&autoload=false`;
    document.head.appendChild(script);
    script.onload = () => kakao.maps.load(resolve);
    script.onerror = () => reject(new Error("Kakao Maps script load error"));
  });
}

function drawMap() {
  if (!props.pathData) {
    console.error("Invalid path data structure");
    return;
  }

  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(coordinates.value.y, coordinates.value.x),
    level: 3,
  };
  const map = new kakao.maps.Map(container, options);

  const bounds = new kakao.maps.LatLngBounds();
  const colors = ["#FF0000", "#0000FF", "#FF00FF", "#00FFFF"];

  props.pathData.forEach((path, index) => {
    const color = colors[index % colors.length];

    path.roads.forEach((road) => {
      const linePath = road.vertexes
        .map((vertex, index, array) => {
          if (index % 2 === 0 && index < array.length - 1) {
            const latLng = new kakao.maps.LatLng(array[index + 1], vertex);
            bounds.extend(latLng);
            return latLng;
          }
        })
        .filter((v) => v);

      const polyline = new kakao.maps.Polyline({
        path: linePath,
        strokeWeight: 3,
        strokeColor: color, // 색상 적용
        strokeOpacity: 0.7,
        strokeStyle: "solid",
      });
      polyline.setMap(map);
    });
  });

  // waypoint에 마커 추가
  props.wayPoint.forEach((point) => {
    const markerPosition = new kakao.maps.LatLng(point.y, point.x);
    const marker = new kakao.maps.Marker({
      position: markerPosition,
      map: map,
      image: new window.kakao.maps.MarkerImage(
        markerImageSrc,
        new window.kakao.maps.Size(40, 40),
        { offset: new window.kakao.maps.Point(20, 40) }
      ),
    });

    bounds.extend(markerPosition);
  });

  map.setBounds(bounds);
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 50%;
  max-width: 600px;
  min-width: 300px;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}
</style>
