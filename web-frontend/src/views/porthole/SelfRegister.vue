<template>
  <div class="form-container">
    <div class="header-box">
      <h1>포트홀 신고</h1>
      <div class="form-box">
        <form @submit.prevent="submitForm" class="form-content">
          <div class="input-group">
            <select id="potholeType" v-model="potholeType" required>
              <option disabled value="">타입 선택</option>
              <option>POTHOLE</option>
              <option>CRACK</option>
            </select>
          </div>
          <div class="input-group">
            <select id="severity" v-model="severity" required>
              <option disabled value="">심각도 선택</option>
              <option value="3">심각</option>
              <option value="2">주의</option>
              <option value="1">양호</option>
            </select>
          </div>
          <button type="submit">신고하기</button>
        </form>
      </div>
    </div>
    <div class="map-box">
      <SearchMap @updateCenter="handleCenterUpdate" />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { postPotholeAdd } from "../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { useMoveStore } from "@/stores/move";
import SearchMap from "./components/SearchMap.vue";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const potholeType = ref("");
const severity = ref("");
const location_x = ref("");
const location_y = ref("");

const submitForm = () => {
  const potholeInfo = {
    x: location_x.value,
    y: location_y.value,
    severity: Number(severity.value),
    type: potholeType.value,
  };

  postPotholeAdd(
    accessToken.value,
    potholeInfo,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        alert("포트홀 신고가 성공적으로 완료되었습니다.");
        store.movePorthole();
      } else {
        alert("포트홀 신고에 실패하였습니다. 다시 시도해주세요.");
      }
    },
    (error) => {
      console.log(error);
      console.log(error.message);
      alert(`신고 처리 중 오류가 발생하였습니다: ${error.message}`);
    }
  );
};

const handleCenterUpdate = (coords) => {
  location_x.value = coords.x;
  location_y.value = coords.y;
  console.log("Map center updated to:", coords);
};
</script>

<style scoped>
.form-container {
  max-width: 900px;
  margin: auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
.header-box {
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
}
.form-box {
  width: 500px;
  margin-left: 20px;
}
.form-content {
  display: flex;
  align-items: center;
}

.input-group {
  min-width: 150px;
  height: 50px;
}

select,
input,
button {
  margin: 10px;
  width: 150px;
  height: 100%; 
}

label {
  display: block;
  margin-bottom: 5px;
}
button {
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}
button:hover {
  background-color: #0056b3;
}
.map-box {
  margin-top: 20px;
}
</style>
