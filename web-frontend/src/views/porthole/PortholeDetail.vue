<template>
  <div class="wrapper">
    <div class="detail-container">
      <div class="main-title">No. {{ pothole_info.id }} 포트홀 상세 정보</div>
      <div class="info-box">
        <div class="left-div">
          <Carusel
            :images="images"
            :damage-id="pothole_info.id"
            @uploadComplete="handleUploadComplete"
          />
        </div>
        <div class="right-div">
          <div class="map-div">
            <RoadTypeIncidentsGraph
              v-if="pothole_info.dirX && pothole_info.dirY"
              :pothole-dirx="pothole_info.dirY"
              :pothole-diry="pothole_info.dirX"
            />
          </div>
          <div class="info-div">
            <table class="info-table">
              <tr>
                <td class="info-title">위험물 종류</td>
                <td class="infos">{{ dtypeDisplay(pothole_info.dtype) }}</td>
              </tr>
              <tr>
                <td class="info-title">위험성 정도</td>
                <td class="infos">{{ dangerClass2(pothole_info.severity) }}</td>
              </tr>
              <tr>
                <td class="info-title">작업 상태</td>
                <td class="infos">{{ pothole_info.status }}</td>
              </tr>
              <tr>
                <td class="info-title">상세 규모</td>
                <td class="infos">너비 {{ roundedWidth }}cm</td>
              </tr>
              <tr>
                <td class="info-title">상세 주소</td>
                <td class="infos">{{ pothole_info.address }}</td>
              </tr>
              <tr>
                <td class="info-title">위도 좌표</td>
                <td class="infos">{{ pothole_info.dirX }}</td>
              </tr>
              <tr>
                <td class="info-title">경도 좌표</td>
                <td class="infos">{{ pothole_info.dirY }}</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <div class="button-container">
        <button class="back-btn" @click="store.moveBack">뒤로가기</button>
        <button class="delete-btn" @click="deleteData(pothole_info.id)" v-if="canDelete">
          삭제하기
        </button>
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
      if (res.data.status == "SUCCESS") {
        // console.log(res.data.message);
        pothole_info.value = res.data.data;
        images.value = res.data.data.imagesResponseDTOS;
      }
    },
    (error) => {
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
        // console.log(res.data.message);
        store.moveBack();
        showAlert();
      }
    },
    (error) => {
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
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
}

.detail-container {
  display: grid;
  grid-template-rows: 12% 79% 11%;
  height: 90%;
  width: 80%;
  background-color: #ffffff;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
}
.main-title {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3.5vh;
  color: #474747;
  margin-top: 0.5vh;
  font-weight: bold;
}

.info-box {
  display: grid;
  grid-template-columns: 52.5% 46.5%;
  gap: 1%;
  margin: 0vh 2vw 1.5vh 2vw;
}

.left-div {
  display: flex;
  justify-content: center;
}

.right-div {
  display: grid;
  grid-template-rows: 45.5% 52.5%;
  gap: 2%;
}

.map-div {
  height: 100%;
  width: 99.5%;
}

.info-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.info-header td {
  background-color: #f0f0f0;
  font-weight: bold;
}

.info-title {
  padding: 1.23vh 0 1.23vh 1.23vh;
  border: 1px solid #ddd;
  background-color: #ecf1fd;
  font-size: 1.8vh;
  font-weight: bold;
  color: #7c7c7c;
}

.infos {
  padding: 1.23vh;
  border: 1px solid #ddd;
  font-size: 1.9vh;
  color: #606060;
  font-weight: 500;
}

.button-container {
  display: flex;
  justify-content: space-between;
  width: 94.3%;
  padding-left: 2vw;
  margin-top: 1.2vh;
}

.back-btn {
  font-size: 1.55vh;
  padding: 0vh 1.5vw;
  height: 5vh;
  cursor: pointer;
  border: none;
  background-color: #f8f8f8;
  border-radius: 8px;
  color: #373737;
  border: 1px solid #acacac;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #d8d8d8;
}

.delete-btn {
  font-size: 1.55vh;
  padding: 0vh 1.5vw;
  cursor: pointer;
  height: 5vh;
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
