<template>
  <div class="detail-container">
    <div class="img-box">
      <div class="carusel-container">
        <Carusel :image="caruselImage" />
      </div>
      <div class="map">
        <RoadTypeIncidentsGraph
          v-if="pothole_info.dirX && pothole_info.dirY"
          :pothole-dirx="pothole_info.dirY"
          :pothole-diry="pothole_info.dirX"
        />
      </div>
    </div>
    <div class="text-info-container">
      <div class="text-left">
        <p>
          <span class="info-title">위험물 번호</span>
          <span class="infos">{{ pothole_info.id }}</span>
        </p>
        <p>
          <span class="info-title">위험물 종류</span>
          <span class="infos">{{ pothole_info.dtype }}</span>
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
        <!-- 버튼 -->
        <div class="button-container">
          <button class="back-btn" @click="store.moveBack">뒤로가기</button>
          <button class="delete-btn" @click="deleteData(pothole_info.id)">
            삭제하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import Carusel from "./components/Carusel.vue";
import RoadTypeIncidentsGraph from "./components/PotholeLocationMap.vue";
import { useMoveStore } from "@/stores/move";
import { useAuthStore } from "@/stores/user";
import { getPotholeDetail, deletePothole } from "../../api/pothole/pothole.js";
import { storeToRefs } from "pinia";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const route = useRoute();
const pothole_info = ref({});

const takeData = (potholeId) => {
  getPotholeDetail(
    accessToken.value,
    potholeId,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        pothole_info.value = res.data.data;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const deleteData = (potholeId) => {
  deletePothole(
    accessToken.value,
    potholeId,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        store.moveBack();
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const caruselImage = computed(() => {
  if (
    pothole_info.value.imagesResponseDTOS &&
    pothole_info.value.imagesResponseDTOS.length > 0
  ) {
    return pothole_info.value.imagesResponseDTOS[0].url;
  } else {
    return "../../assets/image/default.PNG";
  }
});

onMounted(() => {
  takeData(route.params.id);
});
</script>

<style scoped>
.detail-container {
  margin: 1vh 10.5vw 0vh 10.5vw;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.img-box {
  display: grid;
  grid-template-columns: 1fr 1fr;
  width: 100%;
  height: 52.03vh;
}

.map {
  display: flex;
  justify-content: center;
  width: 100%;
  align-self: stretch;
  margin-top: 0px;
  height: 55.03vh;
}

.carusel-container {
  display: flex;
  justify-content: center;
  align-self: stretch;
}

.text-info-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 57vw;
  border-radius: 4px;
  margin-top: 2.5vh;
}

.image {
  height: 55.03vh;
  object-fit: cover;
}

p {
  margin: 1.9vh 0px;
}

.info-title {
  color: #959595;
  font-size: 1.95vh;
  font-weight: bold;
}

.infos {
  color: #373737;
  font-size: 2vh;
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
  justify-content: right;
  width: 100%;
  margin-top: 6vh;
  margin-left: 170px;
}

.back-btn {
  font-size: 1.55vh;
  padding: 1.5vh 1.5vw;
  cursor: pointer;
  border: none;
  background-color: #f8f8f8;
  border-radius: 8px;
  color: #373737;
  border: 1px solid #acacac;
  transition: background-color 0.3s;
  margin-right: 8px;
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
