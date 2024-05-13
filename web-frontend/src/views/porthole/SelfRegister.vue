<template>
  <div class="form-container">
    <div class="main-title">포트홀 수동 신고</div>
    <div class="inputs">
      <form @submit.prevent="submitForm" class="form-content">
        <div class="input-group">
          <div class="input-title">위험물의 종류를 입력해 주세요 :</div>
          <select id="potholeType" v-model="potholeType" required>
            <option class="" disabled value="">타입</option>
            <option>POTHOLE</option>
            <option>CRACK</option>
          </select>
        </div>
        <div class="input-group">
          <div class="input-title">위험물의 심각도를 선택해 주세요 :</div>
          <select id="severity" v-model="severity" required>
            <option disabled value="">심각도</option>
            <option value="3">심각</option>
            <option value="2">주의</option>
            <option value="1">양호</option>
          </select>
        </div>
      </form>
      <SearchMap @updateCenter="handleCenterUpdate" />
    </div>
    <div class="button-div"><button class="report-btn" type="submit">신고하기</button></div>
    <!-- 
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
        </form>
      </div>
    </div>
    <div class="map-box">
      <SearchMap @updateCenter="handleCenterUpdate" />
    <button type="submit">신고하기</button> -->
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
  margin: auto;
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
  height: 85vh;
  width: 38vw;
  display: grid;
  grid-template-rows: 15% 70% 15%;
}
.main-title {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3.5vh;
  font-weight: bold;
  color: #373737;
}
.inputs {
  padding: 0vh 2vw;
}
.input-group {
  display: flex;
  align-items: center;
}
select,
input {
  margin: 10px;
  width: 150px;
  height: 4.5vh;
  border-radius: 5px;
  font-size: 1.8vh;
}
select {
  margin-left: auto;
  padding-left: 20px;
}
.input-title {
  font-size: 2vh;
  font-weight: 500;
  color: #373737;
}
.button-div {
  display: flex;
  justify-content: center;
}

.report-btn {
  background-color: #151c62;
  width: 80%;
  height: 50%;
  cursor: pointer;
  border-radius: 8px;
  font-size: 2vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 2.5vh;
}

.report-btn:hover {
  background-color: #0e1241;
}

/* .form-box {
  width: 500px;
}
.form-content {
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
} */
</style>
