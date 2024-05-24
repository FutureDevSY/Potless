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
      <LottieLocation class="marked" :style="positionStyle(item.dong)" />
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
    const response = await fetch("/location7.svg");
    if (!response.ok) {
      throw new Error(`네트워크 에러: ${response.status}`);
    }
    const svgText = await response.text();
    svgContent.value = svgText;
  } catch (error) {
    console.error("SVG 로드 실패:", error);
  }
});

const areaDetails = ref(null);

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
  압구정동: { x: "12.5%", y: "12%" },
  신사동: { x: "13.5%", y: "19.5%" },
  청담동: { x: "33%", y: "19%" },
  논현동: { x: "15.5%", y: "27.5%" },
  역삼동: { x: "20.5%", y: "38.5%" },
  대치동: { x: "42%", y: "39%" },
  도곡동: { x: "26%", y: "47.5%" },
  개포동: { x: "41%", y: "53.5%" },
  일원동: { x: "58%", y: "47.5%" },
  수서동: { x: "69%", y: "50%" },
  자곡동: { x: "73%", y: "57%" },
  율현동: { x: "82.5%", y: "61%" },
  세곡동: { x: "78%", y: "66.5%" },
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
  width: 52vh;
  height: 75%;
}

.status-overlay {
  position: absolute;
  top: 4%;
  left: 32%;
  color: #1e476d;
  font-size: 2.4vh;
  font-weight: bold;
  z-index: 1;
}
.average {
  position: absolute;
  top: 5.5%;
  left: 73%;
  color: #1e476d;
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
  top: 16%;
  left: 56%;
  padding: 2.8vh 1.7vw;
}
.dong-label {
  color: #636363;
  font-size: 2.1vh;
  margin-bottom: 1.2vh;
  font-weight: bold;
}
.num-label {
  color: #606060;
  font-size: 1.9vh;
}
.num-detail {
  font-size: 2.5vh;
  font-weight: bold;
}
.gun {
  font-size: 1.8vh;
  font-weight: bold;
}

.dong-list {
  position: absolute;
  top: 80%;
  left: 3.4%;
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
  left: 3.5%;
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

.marked {
  position: absolute;
}
</style>
