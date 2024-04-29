<template>
  <div class="pdf-container">
    <div class="header">
      <div class="pothole-info">
        <div class="title">위험물 정보</div>
        <div class="info-i">
          위험물 No. <span class="numebr">{{ props.pothole.damageId }}</span>
        </div>
        <div class="info">
          <div>위험물 유형:</div>
          <div>
            {{ props.pothole.dtype }}
          </div>
        </div>
        <div class="info">
          <div>심각도:</div>
          <div :style="severityStyle">
            {{
              props.pothole.severity === 1
                ? "안전"
                : props.pothole.severity === 2
                ? "위험"
                : props.pothole.severity === 3
                ? "심각"
                : "Unknown"
            }}
          </div>
        </div>
        <div class="info">
          <div>너비:</div>
          <div>{{ props.pothole.width }} mm</div>
        </div>
      </div>
      <div ref="mapContainer" class="img-container">
        <img :src="imageUrl" alt="" />
      </div>
    </div>
    <div class="middle">
      <div class="info">
        <div>상세 주소:</div>
        <div>
          {{ props.pothole.location }} {{ props.pothole.area }}
          {{ props.pothole.roadName }}
        </div>
      </div>
      <div class="direct info">
        <div>위경도 좌표:</div>
        <div class="coordinates">
          <div>x: {{ props.pothole.dir_x }}</div>
          <div>y: {{ props.pothole.dir_y }}</div>
        </div>
      </div>
      <div class="info">
        <div>마지막 탐지 일시:</div>
        <div>
          {{ props.pothole.createAt }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import leafletImage from "leaflet-image";
import marker from "../../../assets/icon/marker.png";

const mapContainer = ref(null);
const imageUrl = ref("");

const props = defineProps({
  pothole: Object,
});

const severityStyle = computed(() => {
  switch (props.pothole.severity) {
    case 1:
      return { color: "green" };
    case 2:
      return { color: "blue" };
    case 3:
      return { color: "red" };
    default:
      return { color: "black" };
  }
});

function preloadImage(url) {
  let img = new Image();
  img.onload = function () {
    console.log("Image preloaded successfully");
  };
  img.onerror = function () {
    console.error("Failed to load image: Image loading failed");
  };
  img.src = url;
}

onMounted(() => {
  const map = L.map(mapContainer.value).setView([51.505, -0.09], 13);
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors",
  }).addTo(map);

  map.whenReady(() => {
    setTimeout(() => {
      // 지도가 화면에 완전히 렌더링된 후 이미지 생성
      leafletImage(map, function (err, canvas) {
        if (err) {
          console.error("Failed to generate image from map:", err);
          return;
        }
        const imageUrl = canvas.toDataURL();
        console.log("Map image URL:", imageUrl);
      });
    }, 500);
  });
});
</script>

<style scoped>
.pdf-container {
  border: 1px solid black;
  width: 210mm;
  height: 297mm;
  display: flex;
  flex-direction: column;
  padding: 70px;
  box-sizing: border-box;
  font-family: "돋움체";
}

.numebr {
  color: blue;
}
.img-container {
  border: 2px solid red;
}
.header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  height: 300px;
}

.title {
  font-size: 50px;
  font-weight: 800;
  margin-bottom: 20px;
}
.info-i {
  width: 90%;
  font-size: 30px;
  margin-bottom: 5px;
}
.info {
  width: 90%;
  font-size: 30px;
  margin-bottom: 5px;
  display: flex;
  justify-content: space-between;
}

.direct {
  display: flex;
}
.direct .coordinates {
  display: flex;
  flex-direction: column;
}

.direct .coordinates > div {
  margin-bottom: 4px;
}
.middle {
  margin-top: 40px;
}
.map {
  width: 100%;
  height: 100%;
}
</style>
