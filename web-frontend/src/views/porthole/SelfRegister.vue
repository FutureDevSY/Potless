<template>
  <div class="form-container">
    <h1>포트홀 신고</h1>
    <form @submit.prevent="submitForm">
      <div class="input-group">
        <label for="potholeType">포트홀 타입:</label>
        <select id="potholeType" v-model="potholeType" required>
          <option disabled value="">타입 선택</option>
          <option>POTHOLE</option>
          <option>CRACK</option>
        </select>
      </div>
      <div class="input-group">
        <label for="district">구:</label>
        <input type="text" id="district" v-model="district" required />
      </div>
      <div class="input-group">
        <label for="subDistrict">동:</label>
        <input type="text" id="subDistrict" v-model="subDistrict" required />
      </div>
      <div class="input-group">
        <label for="plotNumber">지번:</label>
        <input type="text" id="plotNumber" v-model="plotNumber" required />
      </div>
      <button type="submit">신고하기</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { postPotholeAdd } from "../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { useMoveStore } from "@/stores/move";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const potholeType = ref("");
const district = ref("");
const subDistrict = ref("");
const plotNumber = ref("");

const submitForm = () => {
  const address = `${district.value} ${subDistrict.value} ${plotNumber.value}`;
  const potholeInfo = {
    type: potholeType.value,
    address: address,
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
      console.log(error.response.data.message);
      alert(
        `신고 처리 중 오류가 발생하였습니다: ${error.response.data.message}`
      );
    }
  );
};
</script>

<style scoped>
.form-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input,
select,
button {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
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
</style>
