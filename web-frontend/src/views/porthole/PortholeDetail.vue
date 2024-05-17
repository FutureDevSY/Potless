<template>
  <div class="detail-container">
    <div class="img-box">
      <div class="carusel-container">
        <Carusel
          :images="images"
          :damage-id="pothole_info.id"
          @uploadComplete="handleUploadComplete"
        />
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
          <span class="infos">{{ dtypeDisplay(pothole_info.dtype) }}</span>
        </p>
        <p>
          <span class="info-title">위험성 정도</span>
          <span class="infos">{{ dangerClass2(pothole_info.severity) }}</span>
        </p>
        <p>
          <span class="info-title">작업 상태</span>
          <span class="infos">{{ pothole_info.status }}</span>
        </p>
        <p>
          <span class="info-title">상세 규모</span>
          <span class="infos">너비 {{ roundedWidth }}mm</span>
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
          <button
            class="delete-btn"
            @click="deleteData(pothole_info.id)"
            v-if="canDelete"
          >
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
import RoadTypeIncidentsGraph from "./components/PotholeDetailMap.vue";
import { useMoveStore } from "@/stores/move";
import { useAuthStore } from "@/stores/user";
import { getPotholeDetail, deletePothole } from "../../api/pothole/pothole.js";
import { storeToRefs } from "pinia";
import { useSwal } from "../../composables/useSwal";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const route = useRoute();
const pothole_info = ref({});
const swal = useSwal();
const canDelete = computed(() => {
  return pothole_info.value.status !== "작업중";
});

const showAlert = () => {
  swal({
    title: "도로 파손 데이터가 성공적으로 삭제되었습니다.",
    icon: "success",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const dtypeDisplay = (dtype) => {
  switch (dtype) {
    case "POTHOLE":
      return "포트홀";
    case "CRACK":
      return "도로균열";
    case "WORNOUT":
      return "도로마모";
    default:
      return "알 수 없는 유형";
  }
};

const dangerClass2 = (danger) => {
  switch (danger) {
    case 3:
      return "심각";
    case 2:
      return "주의";
    case 1:
      return "양호";
    default:
      return "";
  }
};

const roundedWidth = computed(() => {
  return round(pothole_info.value.width, 2);
});

function round(value, decimals) {
  return Number(Math.round(value + "e" + decimals) + "e-" + decimals);
}

const images = ref([]);

const takeData = (potholeId) => {
  getPotholeDetail(
    accessToken.value,
    potholeId,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        pothole_info.value = res.data.data;
        images.value = res.data.data.imagesResponseDTOS;
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const deleteData = (potholeId) => {
  const pothole_info2 = {
    damageIds: [potholeId],
  };
  deletePothole(
    accessToken.value,
    pothole_info2,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        store.moveBack();
        showAlert();
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const handleUploadComplete = (data) => {
  console.log("업로드 완료: ", data);
  takeData(route.params.id);
};

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
