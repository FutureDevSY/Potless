<template>
  <div class="form-container">
    <div class="main-title">도로 파손 수동 신고</div>
    <div class="inputs">
      <form @submit.prevent="submitForm" class="form-content">
        <div class="input-div">
          <div class="input-group">
            <select id="potholeType" v-model="potholeType" required>
              <option disabled value="">위험물 종류</option>
              <option value="POTHOLE">포트홀</option>
              <option value="CRACK">도로균열</option>
              <option value="WORNOUT">도로마모</option>
            </select>
          </div>
          <div class="input-group">
            <select id="severity" v-model="severity" required>
              <option disabled value="">도로파손 심각도</option>
              <option value="3">심각</option>
              <option value="2">주의</option>
              <option value="1">양호</option>
            </select>
          </div>
        </div>
        <div class="file-upload">
          <input type="file" @change="handleFileChange" accept="image/*" />
        </div>
        <!-- <SearchMap @updateCenter="handleCenterUpdate" /> -->
      </form>
    </div>
    <div class="button-div">
      <button class="report-btn" type="submit">신고하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { postPotholeAdd } from "../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { useMoveStore } from "@/stores/move";
import Select from "./components/Select.vue";
import SearchMap from "./components/SearchMap.vue";
import { useSwal } from "../../composables/useSwal";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const potholeType = ref("");
const severity = ref("");
const location_x = ref("");
const location_y = ref("");
const file = ref(null);
const swal = useSwal();

const handleFileChange = (event) => {
  file.value = event.target.files.length > 0 ? event.target.files[0] : null;
};

const showAlert = () => {
  swal({
    title: "도로 파손 신고가 성공적으로 완료되었습니다.",
    icon: "success",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const showAlert2 = () => {
  swal({
    title: "도로 파손 신고에 실패하였습니다. 다시 시도해주세요.",
    icon: "error",
    confirmButtonText: "확인",
    width: "700px",
  });
};

const submitForm = () => {
  const formData = new FormData();
  formData.append("dtype", potholeType.value);
  formData.append("severity", severity.value);
  formData.append("x", location_x.value);
  formData.append("y", location_y.value);
  if (file.value) {
    formData.append("files", file.value);
  }

  postPotholeAdd(
    accessToken.value,
    formData,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        showAlert();
        store.movePorthole();
      } else {
        showAlert2();
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
};
</script>

<style scoped>
.form-container {
  margin: auto;
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
  height: 85vh;
  width: 35vw;
  display: grid;
  grid-template-rows: 14% 73% 13%;
}
.main-title {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3.4vh;
  font-weight: bold;
  color: #373737;
}
.inputs {
  padding: 0vh 3vw;
}
.form-content {
  height: 100%;
  display: grid;
  grid-template-rows: 9% 9% 77%;
  gap: 2.5%;
}
.input-div {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}
.input-group {
  display: flex;
  align-items: center;
}
select,
input {
  width: 100%;
  height: 100%;
  border-radius: 5px;
  font-size: 1.8vh;
  font-weight: bold;
  color: #939393;
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
  align-items: center;
  height: 100%;
  width: 100%;
}

.report-btn {
  height: 50%;
  width: 70%;
  background-color: #151c62;
  cursor: pointer;
  border-radius: 8px;
  font-size: 2vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
}

.report-btn:hover {
  background-color: #0e1241;
}

select:focus option[value=""] {
  display: none;
}
</style>
