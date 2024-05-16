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
    const response = await fetch("/location5.svg");
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

<style scoped>
.map-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 94%;
  height: 100%;
  overflow: hidden;
}

.map {
  width: 43vh;
  height: 100%;
}
</style>
