<template>
  <div class="map-container">
    <div class="status-overlay">지역별 도로파손 현황</div>
    <div v-html="svgContent" class="map"></div>
    <div
      v-for="item in roadIncidentData"
      :key="item.dong"
      class="label"
      :style="positionStyle(item.dong)"
    >
      <div class="numbers" @mouseover="showTooltip = true" @mouseleave="showTooltip = false">
        {{ item.potholes }}
        <span v-if="showTooltip" class="tooltip">{{ `${item.dong}` }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getAreaDetails, getDongList } from "../../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

const svgContent = ref("");
const showTooltip = ref(false);

onMounted(async () => {
  try {
    const response = await fetch("/location4.svg");
    if (!response.ok) {
      throw new Error(`네트워크 에러: ${response.status}`);
    }
    const svgText = await response.text();
    svgContent.value = svgText;
  } catch (error) {
    console.error("SVG 로드 실패:", error);
  }
});

// ** 로그인 한 지역의 areaGu를 호출
const areaDetails = ref(null); // 지역 이름 저장할

const fetchAreaDetails = async () => {
  const response = await getAreaDetails(accessToken.value, areaId.value);
  if (response && response.status === "SUCCESS") {
    areaDetails.value = response.data;
  } else {
    console.error("Failed to fetch area details");
  }
};

const roadIncidentData = ref([]);

const apiData = async () => {
  try {
    const response = await getDongList(accessToken.value, areaId.value);
    if (response && response.data && response.data.data && response.data.data.list) {
      const { list } = response.data.data;
      roadIncidentData.value = list.map((dong) => ({
        dong: dong.locationName,
        potholes: dong.countDamageBefore + dong.countDamageDuring,
      }));

      console.log("도로별 포트홀 현황:", roadIncidentData.value);
    } else {
      console.error("Invalid or empty data received from the API");
    }
  } catch (error) {
    console.error("Error fetching Dong statistics:", error);
  }
};
onMounted(() => {
  fetchAreaDetails().then(() => {
    apiData();
  });
});

const positions = {
  가정동: { x: "63.2%", y: "44.5%" },
  계산동: { x: "27.8%", y: "65.5%" },
  구성동: { x: "64.4%", y: "49%" },
  관평동: { x: "71.4%", y: "28.5%" },
  교촌동: { x: "37.5%", y: "76%" },
  구룡동: { x: "54%", y: "22%" },
  구암동: { x: "40.5%", y: "57.2%" },
  금고동: { x: "72%", y: "12%" },
  금탄동: { x: "64.5%", y: "1.5%" },
  노은동: { x: "37.5%", y: "52%" },
  대정동: { x: "45%", y: "72%" },
  덕명동: { x: "23%", y: "59.5%" },
  덕진동: { x: "60%", y: "30%" },
  도룡동: { x: "70.8%", y: "43.5%" },
  둔곡동: { x: "56.5%", y: "14.7%" },
  반석동: { x: "23%", y: "43%" },
  문지동: { x: "77%", y: "40.5%" },
  방현동: { x: "59%", y: "35%" },
  복룡동: { x: "38.5%", y: "61.5%" },
  봉명동: { x: "52.5%", y: "56.5%" },
  봉산동: { x: "69%", y: "19.5%" },
  상대동: { x: "47%", y: "61%" },
  성북동: { x: "26%", y: "76%" },
  송강동: { x: "67%", y: "25%" },
  송정동: { x: "10%", y: "85%" },
  수남동: { x: "34.5%", y: "33%" },
  신봉동: { x: "43%", y: "35.5%" },
  신성동: { x: "54%", y: "44%" },
  안산동: { x: "20%", y: "36%" },
  어은동: { x: "58%", y: "50.4%" },
  외삼동: { x: "33%", y: "38%" },
  용산동: { x: "78%", y: "28.5%" },
  원내동: { x: "42.5%", y: "79%" },
  원촌동: { x: "84%", y: "43%" },
  자운동: { x: "50%", y: "38%" },
  장대동: { x: "46%", y: "53.5%" },
  전민동: { x: "82%", y: "35.5%" },
  지족동: { x: "24%", y: "47%" },
  추목동: { x: "49%", y: "30%" },
  탑립동: { x: "85%", y: "30%" },
  하기동: { x: "42%", y: "43.5%" },
  학하동: { x: "37%", y: "65.5%" },
  화암동: { x: "65%", y: "38%" },
  원신흥동: { x: "56%", y: "61.5%" },
  갑동: { x: "22%", y: "53.5%" },
  궁동: { x: "52%", y: "49.5%" },
  방동: { x: "32%", y: "87%" },
  세동: { x: "17%", y: "78%" },
  신동: { x: "57%", y: "7.5%" },
  죽동: { x: "45.5%", y: "48%" },
};

const positionStyle = (dong) => {
  if (positions[dong]) {
    return {
      top: positions[dong].y,
      left: positions[dong].x,
      position: "absolute",
      "z-index": 1,
    };
  }
  return {};
};
</script>

<style scoped>
.map-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 105%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.map {
  width: 47vh;
  height: 100%;
}

.status-overlay {
  position: absolute;
  top: 3%;
  left: 5%;
  color: #373737;
  font-size: 2vh;
  font-weight: bold;
  z-index: 1;
}

.numbers {
  height: 2.4vh;
  width: 2.4vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgb(147, 134, 223);
  border-radius: 100%;
  color: white;
  font-size: 1.6vh;
  font-weight: bold;
  cursor: pointer;
  position: relative;
  z-index: 0;
}
.tooltip {
  position: absolute;
  bottom: -80%;
  left: 50%;
  font-size: 1.4vh;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.421);
  color: white;
  padding: 5px 10px;
  border-radius: 6px;
  white-space: nowrap;
  opacity: 0; /* 초기 상태에서 툴팁을 투명하게 설정 */
  visibility: hidden; /* 초기 상태에서 툴팁을 숨김 */
  transition: opacity 1s ease, visibility 1s ease; /* opacity와 visibility 속성에 대해 트랜지션 적용 */
  z-index: 30;
}

.numbers:hover .tooltip {
  opacity: 1; /* 호버 시 툴팁을 완전 불투명하게 설정 */
  visibility: visible; /* 호버 시 툴팁을 보이게 설정 */
}
</style>
