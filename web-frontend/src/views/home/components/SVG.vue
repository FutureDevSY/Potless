<template>
  <div class="map-container">
    <div v-html="svgContent" class="map"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const svgContent = ref("");

onMounted(async () => {
  try {
    const response = await fetch("/daejeon.svg");
    if (!response.ok) {
      throw new Error(`네트워크 에러: ${response.status}`);
    }
    const svgText = await response.text();
    svgContent.value = svgText;
  } catch (error) {
    console.error("SVG 로드 실패:", error);
  }
});
</script>

<style>
.map-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  overflow: hidden; /* 화면 페이지에 맞추기 */
}

.map {
  width: 180vh;
  height: auto;
  margin-top: 1vh;
}

.map svg g g g {
  /* transition: transform 0.5s ease; */
  transform-origin: center;
  fill: #f1f1f9;
}

.map svg g g g .cls-6 {
  animation: moving-gu 15s ease infinite;
}
.map svg g g g .cls-4 {
  animation: moving-gu 15s ease 3s infinite;
}
.map svg g g g .cls-3 {
  animation: moving-gu 15s ease 6s infinite;
}
.map svg g g g .cls-2 {
  animation: moving-gu 15s ease 9s infinite;
}
.map svg g g g .cls-5 {
  animation: moving-gu 15s ease 12s infinite;
}

@-webkit-keyframes moving-gu {
  0% {
    fill: #f1f1f9;
  }
  10% {
    fill: #d3d5ed;
    transform: scale(1.05);
    filter: drop-shadow(2px 4px rgb(116, 116, 116));
  }
  20% {
    fill: #f1f1f9;
    transform: scale(1);
  }
}
</style>
