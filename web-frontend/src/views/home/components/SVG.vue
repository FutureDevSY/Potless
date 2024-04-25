<template>
  <div class="map-container">
    <div v-html="svgContent" @click="handleClick" class="map"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useDistrictStore } from "../../../stores/district";

const svgContent = ref("");
const router = useRouter();
const districtStore = useDistrictStore();

const handleClick = (event) => {
  const target = event.target;
  if (target.classList.contains("cls-5")) {
    districtStore.setMyValue("유성구");
  } else if (target.classList.contains("cls-3")) {
    districtStore.setMyValue("중구");
  } else if (target.classList.contains("cls-6")) {
    districtStore.setMyValue("동구");
  } else if (target.classList.contains("cls-4")) {
    districtStore.setMyValue("서구");
  } else if (target.classList.contains("cls-2")) {
    districtStore.setMyValue("대덕구");
  }
  if (event.target.closest("g")) {
    router.push("/porthole");
  }
};

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
  /* margin-top: 15vh; */
}

.map svg g g g {
  /* transition: transform 0.5s ease; */
  cursor: pointer;
  /* transform-origin: center; */
  fill: #f3f3f3;
}

.map svg g g g:hover {
  /* fill: #a5c5b7;
  transform: scale(1.04) translateZ(30px);
  filter: drop-shadow(3px 7px rgb(71, 71, 71));
  color: white; */
}
</style>
