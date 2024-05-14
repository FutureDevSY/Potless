<template>
  <div class="pdf-container">
    <div class="header">
      <div class="pothole-info">
        <div class="title">도로파손 정보</div>
        <div class="info-i">
          도로파손 No. <span class="numebr">{{ props.pothole.damageId }}</span>
        </div>
        <div class="info">
          <div>위험물 유형:</div>
          <div>
            {{ dtypeDisplay }}
          </div>
        </div>
        <div class="info">
          <div>심각도:</div>
          <div :style="severityStyle">
            {{
              props.pothole.severity === 1
                ? "양호"
                : props.pothole.severity === 2
                ? "주의"
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
          {{ props.pothole.address }}
        </div>
      </div>
      <div class="info">
        <div>탐지 일시:</div>
        <div>
          {{ formattedDateTime }}
        </div>
      </div>
      <div class="info-img">
        <div class="image-box">
          <img :src="base64Image" alt="" class="pothole-img" />

          <!-- <img
            src="../../../assets/image/default.PNG"
            alt=""
            class="pothole-img"
          /> -->
          <div><포트홀 사진></div>
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
const base64Image = ref(""); // Base64 이미지 데이터를 저장할 ref
const allImagesLoaded = ref(false);
const imageLoadCount = ref(0);
const totalImages = ref(1);

const props = defineProps({
  pothole: Object,
});

const checkAllImagesLoaded = () => {
  if (imageLoadCount.value >= totalImages.value) {
    allImagesLoaded.value = true;
    console.log("All images loaded, ready for PDF conversion.");
  }
};

const dtypeDisplay = computed(() => {
  const display =
    props.pothole.dtype === "POTHOLE"
      ? "포트홀"
      : props.pothole.dtype === "CRACK"
      ? "도로균열"
      : props.pothole.dtype === "WORNOUT"
      ? "도로마모"
      : "알 수 없는 유형";
  return display;
});

const formattedDateTime = computed(() => {
  const date = new Date(props.pothole.createdDateTime);
  return date.toLocaleString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
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

async function convertToBase64(url) {
  const response = await fetch(url);
  const blob = await response.blob();
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.readAsDataURL(blob);
  });
}

onMounted(async () => {
  base64Image.value = await convertToBase64(
    props.pothole.imagesResponseDTOS[0].url
  );

  const map = L.map(mapContainer.value, {
    zoomControl: false,
  }).setView([props.pothole.dirY, props.pothole.dirX], 16);
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors",
    maxZoom: 19,
  }).addTo(map);

  const icon = L.icon({
    iconUrl: marker,
    iconSize: [32, 32],
    iconAnchor: [16, 16],
  });

  L.marker([props.pothole.dirY, props.pothole.dirX], { icon }).addTo(map);

  map.whenReady(() => {
    leafletImage(map, function (err, canvas) {
      if (err) {
        console.error("Failed to generate image from map:", err);
        return;
      }
      imageUrl.value = canvas.toDataURL();
    });
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
  font-size: 40px;
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

.image-box {
  width: 50%;
  height: auto;
}
.pothole-img {
  width: 100%;
  height: auto;
}

.info-img {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  font-size: 20px;
}
</style>
