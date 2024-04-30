<template>
  <div class="detail-container">
    <div class="img-box">
      <div class="carusel-container">
        <Carusel />
      </div>
      <div class="map">
        <RoadTypeIncidentsGraph
          :pothole-dirx="pothole_info.dirX"
          :pothole-diry="pothole_info.dirY"
        />
      </div>
    </div>
    <div class="text-info-container">
      <div class="text-left">
        <p>
          <span class="info-title">위험물 번호</span>
          <span class="infos">{{ pothole_info.pothole_id }}</span>
        </p>
        <p>
          <span class="info-title">위험물 종류</span>
          <span class="infos">{{ pothole_info.type }}</span>
        </p>
        <p>
          <span class="info-title">위험성 정도</span>
          <span class="infos">{{ pothole_info.severity }}</span>
        </p>
        <p>
          <span class="info-title">작업 상태</span>
          <span class="infos">{{ pothole_info.status }}</span>
        </p>
        <p>
          <span class="info-title">상세 규모</span>
          <span class="infos">너비 {{ pothole_info.width }}mm</span>
        </p>
      </div>
      <div class="text-right">
        <p>
          <span class="info-title">상세 주소</span>
          <span class="infos">{{ pothole_info.address }}</span>
        </p>
        <p>
          <span class="info-title">위도 좌표</span>
          <span class="infos">{{ pothole_info.dirX }}</span>
        </p>
        <p>
          <span class="info-title">경도 좌표</span>
          <span class="infos">{{ pothole_info.dirY }}</span>
        </p>
        <p>
          <span class="info-title">마지막 탐지 일시</span>
          <span class="infos">{{ pothole_info.create_at }}</span>
        </p>
      </div>
    </div>
    <!-- 버튼 -->
    <div class="button-container">
      <button class="back-btn" @click="store.moveBack">뒤로가기</button>
      <button class="delete-btn">삭제하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Carusel from "./components/Carusel.vue";
import RoadTypeIncidentsGraph from "./components/PotholeLocationMap.vue";
import { useMoveStore } from "@/stores/move";

const pothole_info = ref({
  pothole_id: 1,
  severity: 2,
  type: "포트홀",
  status: 1,
  width: 305,
  address: "대전광역시 동구 계족로 282",
  dirX: 36.3549777,
  dirY: 127.2983403,
  create_at: "2023-01-04 14:22:33",
});

const store = useMoveStore();
</script>

<style scoped>
.detail-container {
  margin: 2vh 10.5vw 0vh 10.5vw;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.img-box {
  display: grid;
  grid-template-columns: 1fr 1fr;
  width: 100%;
}

.map {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-top: 0.6vh;
  align-self: stretch;
}

.carusel-container {
  display: flex;
  justify-content: center;
  padding-top: 0.6vh;
  align-self: stretch;
}

.text-info-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 78vw;
  border-radius: 4px;
  margin-top: 1.5vh;
}

.info-title {
  color: #959595;
  font-size: 2vh;
  font-weight: bold;
}

.infos {
  color: #373737;
  font-size: 2.3vh;
}

.text-left {
  flex: 1;
}

.text-right {
  flex: 1;
}

.info-title {
  display: inline-block;
  width: 10.5vw;
}

.button-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 2vh;
}

.back-btn {
  font-size: 1.55vh;
  padding: 1.5vh 1.5vw;
  cursor: pointer;
  border: none;
  background-color: #f8f8f8;
  border-radius: 8px;
  color: #373737;
  border: 1px solid #373737;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #d8d8d8;
}

.delete-btn {
  font-size: 1.55vh;
  padding: 1.5vh 1.5vw;
  cursor: pointer;
  border: none;
  background-color: #fef1f1;
  border-radius: 8px;
  color: #cd0404;
  border: 1px solid #cd0404;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #fccccc;
}
</style>
