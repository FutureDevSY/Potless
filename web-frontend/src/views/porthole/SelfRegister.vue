<template>
  <div class="form-container">
    <span @click="store.moveBack()" class="back">&larr;</span>
    <div class="main-title">도로 파손 수동 신고</div>
    <div class="inputs">
      <form @submit.prevent="submitForm" class="form-content">
        <div class="input-div">
          <div class="input-group">
            <SelfSelect
              :options="['포트홀', '도로균열', '도로마모']"
              defaultText="위험물 종류"
              @update:selected="updatePotholeType"
            />
          </div>
          <div class="input-group">
            <SelfSelect
              :options="['심각', '주의', '양호']"
              defaultText="위험물 심각도"
              @update:selected="updateSeverity"
            />
          </div>
        </div>
        <div class="file-upload">
          <input
            class="form__input--file"
            id="upload1"
            type="file"
            @change="handleFileChange"
            accept="image/*"
          />
          <label class="form__label--file" for="upload1">파일 선택</label>
          <span :class="['form__span--file', { 'file-selected': fileName }]">{{
            fileName || "포트홀 사진을 선택해 주세요. (선택)"
          }}</span>
        </div>
        <SearchMap @updateCenter="handleCenterUpdate" />
        <button class="report-btn" type="submit">신고하기</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { postPotholeAdd } from "../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { useMoveStore } from "@/stores/move";
import SelfSelect from "./components/SelfSelect.vue";
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
const fileName = ref("");

const handleFileChange = (event) => {
  file.value = event.target.files.length > 0 ? event.target.files[0] : null;
  fileName.value = file.value ? file.value.name : "";
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
  console.log("submitForm 호출됨"); // 추가된 로그
  console.log("potholeType:", potholeType.value); // 추가된 로그
  console.log("severity:", severity.value); // 추가된 로그
  console.log("location_x:", location_x.value); // 추가된 로그
  console.log("location_y:", location_y.value); // 추가된 로그

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
        // console.log(res.data.message);
        showAlert();
        store.movePorthole();
      } else {
        showAlert2();
      }
    },
    (error) => {
      console.log(error.message);
      alert(`신고 처리 중 오류가 발생하였습니다: ${error.message}`);
    }
  );
};

const handleCenterUpdate = (coords) => {
  location_x.value = coords.x;
  location_y.value = coords.y;
};

const updatePotholeType = (selected) => {
  console.log("updatePotholeType 호출됨:", selected); // 추가된 로그
  switch (selected) {
    case "포트홀":
      potholeType.value = "POTHOLE";
      break;
    case "도로균열":
      potholeType.value = "CRACK";
      break;
    case "도로마모":
      potholeType.value = "WORNOUT";
      break;
    default:
      potholeType.value = "";
  }
};

const updateSeverity = (selected) => {
  console.log("updateSeverity 호출됨:", selected); // 추가된 로그
  switch (selected) {
    case "심각":
      severity.value = "3";
      break;
    case "주의":
      severity.value = "2";
      break;
    case "양호":
      severity.value = "1";
      break;
    default:
      severity.value = "";
  }
};
</script>

<style scoped>
.form-container {
  margin: auto;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
  height: 85vh;
  width: 35vw;
  display: grid;
  grid-template-rows: 13% 76% 11%;
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
  grid-template-rows: 8% 7.5% 76.5%;
  gap: 4%;
}
.input-div {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
.input-group {
  height: 100%;
  width: 100%;
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
  padding: 0vh 3vw;
}

.report-btn {
  height: 40px;
  width: 100%;
  margin-bottom: 1vh;
  background-color: #1e476d;
  cursor: pointer;
  border-radius: 6px;
  font-size: 2vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
}

.report-btn:hover {
  background-color: #17344f;
}

select:focus option[value=""] {
  display: none;
}
.file-upload {
  display: flex;
  width: 101%;
}
input[type="file"] {
  display: block;
  width: 0;
  height: 0;
  overflow: hidden;
}
.form__label--file {
  width: 20%;
  height: 100%;
  background: #9f9f9f;
  border-radius: 6px;
  color: #fff;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.6vh;
  transition: all 0.5s ease;
}
.form__label--file:hover {
  background: rgb(136, 136, 136);
}
.form__span--file {
  padding: 0 0px 0 15px;
  margin-left: 15px;
  display: block;
  width: 80%;
  min-height: 30px;
  border: 2px solid #d6d6d6;
  border-radius: 6px;
  display: flex;
  align-items: center;
  color: #ababab;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  box-sizing: border-box;
}
.form__span--file.file-selected {
  border-color: #a2a2a2;
  background-color: #efefef6f;
  color: #666666;
}
.back {
  position: absolute;
  top: 7%;
  left: 32%;
  color: #aaa;
  font-size: 5vh;
  font-weight: bold;
  cursor: pointer;
}
.back:hover {
  color: #7d7d7d;
  text-decoration: none;
  cursor: pointer;
}
</style>
