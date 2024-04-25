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
      <div class="text-info">
        <div class="text-left">
          <p>
            <span class="pothole-info">번호</span>
            <span>{{ pothole_info.pothole_id }}</span>
          </p>
          <p>
            <span class="pothole-info">위험물 정도</span>
            <span
              >{{ pothole_info.type }} / {{ pothole_info.severity }} /
              {{ pothole_info.status }}</span
            >
          </p>
          <p>
            <span class="pothole-info">상세 규모</span>
            <span>너비 {{ pothole_info.width }}mm</span>
          </p>
        </div>
        <div class="text-right">
          <p>
            <span class="pothole-info">상세 주소</span>
            <span>{{ pothole_info.address }}</span>
          </p>
          <p>
            <span class="pothole-info">위경도 좌표</span>
            <span>{{ pothole_info.dirX }} , {{ pothole_info.dirY }} </span>
          </p>
          <p>
            <span class="pothole-info">마지막 탐지 일시</span>
            <span>{{ pothole_info.create_at }}</span>
          </p>
        </div>
      </div>
      <!-- 버튼 -->
      <div class="button-container">
        <button class="button-style" @click="store.moveBack">뒤로가기</button>
      </div>
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
  address: "대전 광역시 동구 계족로 282",
  dirX: 36.3549777,
  dirY: 127.2983403,
  create_at: "2023-01-04 14:22:33",
});

const store = useMoveStore();
</script>

<style scoped>
.detail-container {
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.img-box {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  width: 100%;
}

.map {
  display: flex;
  justify-content: center;
  height: 100%;
  width: 100%;
  padding-top: 30px;
  align-self: stretch;
}

.carusel-container {
  display: flex;
  justify-content: center;
  max-height: 100%;
  max-width: 100%;
  padding-top: 30px;
  align-self: stretch;
}

.text-info-container {
  display: flex;
  flex-direction: column;
  width: 80.5%;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  border-radius: 4px;
}

.text-info {
  display: flex;
  margin-bottom: 10px;
}

.text-info > div {
  display: flex;
  flex-direction: column;
  padding: 10px 0;
}

.text-info p {
  font-size: 0.875rem;
  line-height: 1.6;
  color: #2c3e50;
  margin: 0;
}

.text-left .pothole-info,
.text-right .pothole-info {
  font-weight: bold;
  color: #757575;
}

.text-left,
.text-right {
  width: 600px;
}

span {
  font-size: 20px;
  font-weight: bold;
}

.pothole-info {
  display: inline-block;
  width: 300px;
}

.text-right {
  margin-left: 350px;
}
.button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.button-style {
  background-color: #95a5a6;
  color: #ffffff;
  border: none;
  padding: 10px 20px;
  margin: 0 5px;
  border-radius: 4px;
  cursor: pointer;
  text-transform: uppercase;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.button-style:hover {
  background-color: #7f8c8d;
}
</style>
