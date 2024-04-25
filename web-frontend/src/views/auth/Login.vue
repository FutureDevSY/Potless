<template>
  <div class="container">
    <div class="left-box">
      <div class="logo">서비스 로고</div>
      <div class="servicename">POTLESS</div>
      <img class="image" src="@/assets/image/road.png" alt="서비스 이미지" />
      <div class="slogan1">포트홀 없는 길,</div>
      <div class="slogan2">우리가 만들어 갑니다.</div>
    </div>
    <div class="right-box">
      <div
        class="login-title"
        :style="{ 'padding-bottom': showIdError || showPasswordError ? '6.5vh' : '9vh' }"
      >
        관리자 로그인
      </div>
      <div v-if="showIdError" class="error-message">아이디를 입력해주세요.</div>
      <div v-if="showPasswordError" class="error-message">비밀번호를 입력해주세요.</div>
      <form class="login-form" @submit.prevent="moveHome">
        <input
          class="form-control"
          :class="{ 'input-error': showIdError }"
          type="text"
          v-model="auth_id"
          placeholder="아이디"
          @input="showIdError = false"
        />
        <input
          class="form-control"
          :class="{ 'input-error': showPasswordError }"
          type="password"
          v-model="auth_password"
          placeholder="비밀번호"
          @input="showPasswordError = false"
        />
        <button class="login-button" type="submit">
          <span class="button-text">로그인</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const auth_id = ref("");
const auth_password = ref("");
const showIdError = ref(false);
const showPasswordError = ref(false);

const moveHome = () => {
  if (!auth_id.value) {
    showIdError.value = true;
  }
  if (!auth_password.value) {
    showPasswordError.value = true;
  }
  if (auth_id.value && auth_password.value) {
    router.push("/");
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15vw 0 15vw;
  height: 100vh;
  background-color: #f3f3f3;
}

.left-box,
.right-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffffff;
  height: 75vh;
}

.left-box {
  flex: 4;
  background-color: #d7d7d7;
  color: white;
}

.right-box {
  flex: 3;
}

.logo {
  padding-top: 12vh;
}

.servicename {
  font-size: 6vh;
  font-weight: bold;
  padding-top: 0.3vh;
}

.image {
  max-width: 100%;
  height: 45%;
  width: auto;
  padding-top: 1vh;
}

.slogan1,
.slogan2 {
  font-size: 3vh;
}

.slogan1 {
  padding-top: 1vh;
}

.slogan2 {
  margin-top: 1.5vh;
}

.login-title {
  font-size: 3.6vh;
  font-weight: bold;
  padding-bottom: 9vh;
  padding-top: 16.5vh;
  color: #373737;
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

input {
  margin: 1.3vh;
}

.form-control {
  width: 18vw;
  height: 48px;
  position: relative;
  overflow: hidden;
  border-radius: 20px;
  background: none;
  border: 0;
  border: 2px solid #7a7979;
  transition: border 0.4s ease;
  padding-left: 15px;
  font-size: 18px;
  color: #373737;
}

.form-control:focus {
  outline: 0;
  border-color: #a9a9a9;
}

.form-control::placeholder {
  color: gray;
  font-size: 16px;
  transition: all 0.4s ease;
}

.form-control:focus::placeholder {
  opacity: 0;
}

.login-button {
  background-color: #474747;
  margin-top: 5vh;
  width: 13vw;
  height: 50px;
  cursor: pointer;
  border-radius: 40px;
  font-size: 20px;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: border 0.5s, color 0.5s;
}

.input-error {
  border: 2px solid #ff4343;
}

.error-message {
  color: #ff4343;
}
</style>
