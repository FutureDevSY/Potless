<template>
  <div class="map-container">
    <div class="status-overlay">지역별 도로파손 현황</div>
    <div class="average">* 평균 : {{ averagePotholes }} 건</div>
    <div class="input-container">
      <input type="text" placeholder="지역(동) 입력" @input="handleInput" :value="inputValue" />
      <img src="../../../assets/icon//search.png" alt="#" class="search-icon" />
    </div>
    <div class="dong-list">
      <div
        class="dong"
        v-for="item in filteredDongList"
        :key="item.dong"
        @mouseover="hoveredDong = item.dong"
        @mouseleave="hoveredDong = ''"
      >
        {{ item.dong }}
      </div>
    </div>
    <div v-html="svgContent" class="map"></div>
    <div
      v-for="item in roadIncidentData"
      :key="item.dong"
      class="label"
      v-show="item.dong === hoveredDong"
    >
      <LottieLocation :style="positionStyle(item.dong)" />
      <div class="infos">
        <div class="dong-label">{{ item.dong }}</div>
        <div class="num-label">
          포트홀의 개수 :
          <span class="num-detail" :style="{ color: getPotholeColor(item.potholes) }">{{
            item.potholes
          }}</span>
          <span :style="{ color: getPotholeColor(item.potholes) }" class="gun">건</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import LottieLocation from "./LottieLocation.vue";
import { getAreaDetails, getDongList } from "../../../api/statistics/statistics";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);

const svgContent = ref("");
const inputValue = ref("");
const hoveredDong = ref("");

const handleInput = (e) => {
  inputValue.value = e.target.value;
};

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
  가정동: { x: "58.5%", y: "47%" },
  계산동: { x: "31.2%", y: "65.5%" },
  구성동: { x: "59%", y: "51%" },
  관평동: { x: "65%", y: "31.5%" },
  교촌동: { x: "38.5%", y: "75%" },
  구룡동: { x: "51%", y: "26%" },
  구암동: { x: "40.5%", y: "57.8%" },
  금고동: { x: "64%", y: "17%" },
  금탄동: { x: "59.5%", y: "7.5%" },
  노은동: { x: "39%", y: "53.5%" },
  대정동: { x: "42%", y: "72%" },
  덕명동: { x: "27%", y: "54%" },
  덕진동: { x: "57%", y: "32%" },
  도룡동: { x: "63.8%", y: "46%" },
  둔곡동: { x: "53.5%", y: "19%" },
  반석동: { x: "28%", y: "44%" },
  문지동: { x: "71.5%", y: "42.5%" },
  방현동: { x: "56%", y: "37%" },
  복룡동: { x: "39.5%", y: "61.5%" },
  봉명동: { x: "50%", y: "57.5%" },
  봉산동: { x: "62.5%", y: "23.5%" },
  상대동: { x: "46%", y: "62%" },
  성북동: { x: "30%", y: "75.5%" },
  송강동: { x: "61.5%", y: "29%" },
  송정동: { x: "18%", y: "83%" },
  수남동: { x: "36%", y: "36.5%" },
  신봉동: { x: "43%", y: "38.5%" },
  신성동: { x: "51%", y: "47%" },
  안산동: { x: "26%", y: "39%" },
  어은동: { x: "54%", y: "52%" },
  외삼동: { x: "35.5%", y: "41%" },
  용산동: { x: "69%", y: "32.5%" },
  원내동: { x: "41.5%", y: "79%" },
  원촌동: { x: "72%", y: "45.5%" },
  자운동: { x: "48.5%", y: "41%" },
  장대동: { x: "44.5%", y: "55%" },
  전민동: { x: "72%", y: "38.5%" },
  지족동: { x: "30%", y: "48%" },
  추목동: { x: "47.5%", y: "33%" },
  탑립동: { x: "75%", y: "33%" },
  하기동: { x: "43%", y: "44.8%" },
  학하동: { x: "38%", y: "64.5%" },
  화암동: { x: "62.2%", y: "38.5%" },
  원신흥동: { x: "52%", y: "62.5%" },
  갑동: { x: "27%", y: "54%" },
  궁동: { x: "49.5%", y: "53%" },
  방동: { x: "33%", y: "86%" },
  세동: { x: "22.5%", y: "78%" },
  신동: { x: "54.3%", y: "13%" },
  죽동: { x: "44.5%", y: "49%" },
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

const extractInitialConsonants = (str) => {
  const initialConsonants = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
  const cho = initialConsonants.split("");
  const ga = 44032;
  return Array.from(str)
    .map((ch) => {
      if (ch.charCodeAt(0) < ga || ch.charCodeAt(0) > 55203) {
        return ch;
      }
      const uni = ch.charCodeAt(0) - ga;
      return cho[Math.floor(uni / 588)];
    })
    .join("");
};

const filteredDongList = computed(() => {
  const searchValue = inputValue.value.trim();
  if (searchValue === "") {
    return roadIncidentData.value;
  }
  const initialConsonantSearch = extractInitialConsonants(searchValue);
  return roadIncidentData.value.filter((item) => {
    const itemInitialConsonants = extractInitialConsonants(item.dong);
    return itemInitialConsonants.startsWith(initialConsonantSearch);
  });
});

const averagePotholes = computed(() => {
  const validEntries = roadIncidentData.value.filter((item) => item.potholes > 0);
  if (validEntries.length === 0) return 0;
  const totalPotholes = validEntries.reduce((total, item) => total + item.potholes, 0);
  return (totalPotholes / validEntries.length).toFixed(1);
});

const minPotholes = computed(() => {
  return Math.min(...roadIncidentData.value.map((item) => item.potholes));
});

const maxPotholes = computed(() => {
  return Math.max(...roadIncidentData.value.map((item) => item.potholes));
});

const getPotholeColor = (potholes) => {
  const range = maxPotholes.value - minPotholes.value;
  const percentage = ((potholes - minPotholes.value) / range) * 100;

  if (percentage < 30) {
    return "#8dc63f";
  } else if (percentage < 70) {
    return "#ffb931";
  } else {
    return "#ff4e4e";
  }
};
</script>

<style scoped>
.map-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
  background-color: #ffffff;
}

.map {
  width: 43vh;
  height: 82%;
}

.status-overlay {
  position: absolute;
  top: 4%;
  left: 30%;
  color: #373737;
  font-size: 2.2vh;
  font-weight: bold;
  z-index: 1;
}
.average {
  position: absolute;
  top: 5.5%;
  left: 73%;
  color: #242557;
  font-size: 1.6vh;

  z-index: 1;
}

.infos {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgb(241, 241, 241);
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.255);
  border-radius: 10px;
  z-index: 0;
  position: absolute;
  top: 15%;
  left: 10%;
  padding: 2vh 1vw;
}
.dong-label {
  color: #636363;
  font-size: 1.8vh;
  margin-bottom: 1vh;
  font-weight: bold;
}
.num-label {
  color: #606060;
  font-size: 1.6vh;
}
.num-detail {
  font-size: 2.2vh;
  font-weight: bold;
}
.gun {
  font-size: 1.8vh;
  font-weight: bold;
}

.dong-list {
  position: absolute;
  top: 80%;
  left: 65%;
  width: 9.5vw;
  height: 12vh;
  overflow: auto;
  justify-content: center;
  align-items: center;
  border: 1px solid #bcbcbc;
  border-radius: 5px;
}
.dong {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0.5vh 0vw;
  cursor: pointer;
}
.dong:hover {
  background-color: rgb(241, 241, 241);
}
.input-container {
  position: absolute;
  top: 74%;
  left: 65%;
  display: flex;
  align-items: center;
}

input[type="text"] {
  padding: 1vh 0px 1vh 5px;
  border: 1px solid #bcbcbc;
  width: 175px;
  border-radius: 5px;
  font-size: 1.4vh;
}

input[type="text"]:focus {
  outline: 0;
  border: 1px solid #6d6d6d;
}

.search-icon {
  position: absolute;
  right: 5px;
  cursor: pointer;
  height: 2.5vh;
}
</style>
