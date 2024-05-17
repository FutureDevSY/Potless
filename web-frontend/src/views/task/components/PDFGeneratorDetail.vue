<template>
  <div class="pdf-container">
    <div class="title">도로파손 정보</div>
    <div class="header">
      <div class="pothole-info">
        <div class="info">
          파손 번호: <span>{{ props.pothole.damageId }}</span>
        </div>
        <div class="info">
          <div>파손 유형:</div>
          <div>
            {{ dtypeDisplay }}
          </div>
        </div>
        <div class="info">
          <div>심 각 도 :</div>
          <div>
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
          <div style="letter-spacing: 20px">너비:</div>
          <div>{{ props.pothole.width }} cm</div>
        </div>
        <div class="info">
          <div>탐지 일시:</div>
          <div>
            {{ formattedDateTime }}
          </div>
        </div>
        <div class="info">
          <div>상세 주소:</div>
          <div>
            {{ props.pothole.address }}
          </div>
        </div>
      </div>
      <div ref="mapContainer" class="img-container">
        <img :src="imageUrl" alt="" />
      </div>
    </div>
    <hr class="half-line" />
    <div class="middle">
      <div class="info-img">
        <div class="image-box">
          <img :src="base64Image" alt="" class="pothole-img" />
          <div class="img-title"><작업 전 사진></div>
        </div>
      </div>
      <div class="info-img">
        <div class="image-box">
          <div class="border">
            <img
              v-if="base64Image1"
              :src="base64Image1"
              alt=""
              class="pothole-img"
            />
          </div>
          <div class="img-title"><작업 중 사진></div>
        </div>
      </div>
      <div class="info-img">
        <div class="image-box">
          <div class="border">
            <img
              v-if="base64Image2"
              :src="base64Image2"
              alt=""
              class="pothole-img"
            />
          </div>
          <div class="img-title"><작업 후 사진></div>
        </div>
      </div>
    </div>
    <div class="page-number">- {{ props.index + 1 }} -</div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import leafletImage from "leaflet-image";
import marker from "../../../assets/icon/marker.png";
import defaultImage from "../../../assets/image/traffic3.webp";

const mapContainer = ref(null);
const imageUrl = ref("");
const base64Image = ref("");
const base64Image1 = ref("");
const base64Image2 = ref("");
const allImagesLoaded = ref(false);
const imageLoadCount = ref(0);
const totalImages = ref(1);

const props = defineProps({
  pothole: Object,
  index: Number,
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
  if (props.pothole.imagesResponseDTOS[0]) {
    base64Image.value = await convertToBase64(
      props.pothole.imagesResponseDTOS[0].url
    );
  }
  if (props.pothole.imagesResponseDTOS[1]) {
    base64Image1.value = await convertToBase64(
      props.pothole.imagesResponseDTOS[1].url
    );
  }
  if (props.pothole.imagesResponseDTOS[2]) {
    base64Image2.value = await convertToBase64(
      props.pothole.imagesResponseDTOS[2].url
    );
  }

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
  margin-bottom: 50px;
}
.info {
  width: 90%;
  font-size: 15px;
  margin-bottom: 30px;
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
  display: grid;
  margin-top: 30px;
  grid-template-columns: 1fr 1fr 1fr;
}
.map {
  width: 100%;
  height: 100%;
}
.half-line {
  color: black;
}
.image-box {
  width: 95%;
  height: 309.52px;
}

.border {
  border: 1px solid black;
  width: 100%;
  height: 312.52px;
}
.pothole-img {
  width: 100%;
  height: 309.52px;
}

.info-img {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  font-size: 20px;
  width: 100%;
  height: 309.52px;
}

.img-title {
  margin-top: 30px;
}
.page-number {
  margin-top: 180px;
  text-align: center;
}
</style>
