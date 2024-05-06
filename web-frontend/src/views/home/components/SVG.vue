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
  width: 175vh;
  height: auto;
  margin-top: 2vh;
}

.map svg g g g {
  transition: transform 0.5s ease;
  transform-origin: center;
  fill: #fcfcfc;
}

.map svg g g g:hover {
  fill: #f1f1f9;
  /* transform: scale(1.04) translateZ(30px); */
  transform: scale(1.04);
  filter: drop-shadow(2px 4px rgb(116, 116, 116));
  color: white;
}
</style>
