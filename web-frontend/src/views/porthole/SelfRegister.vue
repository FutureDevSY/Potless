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
        <label for="address">주소:</label>
        <input type="text" id="address" v-model="address" required />
        <button>주소 변경</button>
      </div>
      <div>위도: {{ location.x }} / 경도: {{ location.y }}</div>
      <button type="submit">신고하기</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { postPotholeAdd } from "../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";

const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const potholeType = ref("");
const address = ref("");
const location = ref({});

const submitForm = () => {
  const potholeInfo = ref({
    dtype: potholeType.value,
    x: location.value.x,
    y: location.value.y,
  });

  postPotholeAdd(
    accessToken.value,
    potholeInfo.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
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
