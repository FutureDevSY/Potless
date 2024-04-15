<template>
  <div class="container">
    <div class="left-box">
      <div>서비스 명</div>
      <div>서비스 로고</div>
      <div>서비스 설명/ 슬로건</div>
    </div>
    <div class="right-box">
      <strong>로그인</strong>
      <input type="text" v-model="auth_id" placeholder="아이디" />
      <input type="password" v-model="auth_password" placeholder="비밀번호" />
      <button @click="moveHome">로그인</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const auth_id = ref("");
const auth_password = ref("");
const moveHome = () => {
  router.push("/");
};
</script>

<style scoped>
.container {
  display: flex;
  margin: 10%;
  padding: 5px;
  grid-row: 1fr 1fr;
  gap: 10px;
  height: 500px;
}
input {
  text-align: center;
  margin: 10px;
}
.left-box {
  border: 1px solid black;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.left-box > div {
  margin: 10px;
}
.right-box {
  border: 1px solid black;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
