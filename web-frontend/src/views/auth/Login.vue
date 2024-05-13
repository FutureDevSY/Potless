<template>
  <div class="container">
    <img class="logo" src="../../assets/icon/weblogo.png" alt="#" />
    <div class="main-div">
      <div class="image-box"></div>
      <div class="login-box">
        <div class="login-title">관리자 로그인</div>
        <form class="login-form" @submit.prevent="moveHome">
          <div class="input-group">
            <div class="input-title">아이디</div>
            <input
              class="form-control"
              type="text"
              v-model="auth_id"
              placeholder="아이디를 입력해 주세요."
            />
          </div>
          <div class="input-group">
            <div class="input-title">비밀번호</div>
            <input
              class="form-control"
              type="password"
              v-model="auth_password"
              placeholder="비밀번호를 입력해 주세요."
              @input="showError = false"
            />
            <div v-if="showError" class="error-message">
              {{ errorMsg }}
            </div>
          </div>
          <button class="login-button" type="submit" @click="doLogin">
            <span class="button-text">로그인</span>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../../stores/user.js";
import { login } from "../../api/auth/auth";
import { useMoveStore } from "@/stores/move";

const store = useAuthStore();
const store2 = useMoveStore();
const auth_id = ref("");
const auth_password = ref("");
const showError = ref(false);
const errorMsg = ref("");

const doLogin = () => {
  const loginData = ref({
    email: auth_id.value,
    password: auth_password.value,
  });

  login(
    loginData,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        store.login(res.data, res.data.data.token);
        store2.moveMain();
      }
    },
    (error) => {
      console.log(error);
      showError.value = true;
      errorMsg.value = error.message;
    }
  );
};
</script>

<style scoped>
.container {
  display: flex;
  align-content: center;
  height: 100vh;
  background-color: #f8f8f8;
  background-image: url("../../assets/icon/login-background7.jpg");
  background-color: rgb(255, 255, 255);
  background-size: cover;
  background-position: center;
}

.logo {
  position: absolute;
  top: 5%;
  left: 4%;

  width: 12.5vw;
}

.image-box {
  margin: 25vh 0 0 13vw;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #ffffff;
  height: 45vh;
  width: 550px;
  color: #373737;
  box-shadow: 0 4px 9px rgba(0, 0, 0, 0.4);
  background-image: url("../../assets/icon/detect.jpg");
  background-color: rgb(255, 255, 255);
  background-size: cover;
  background-position: center;
}

.login-box {
  margin: 25vh 0 0 11vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #ffffff;
  height: 58vh;
  width: 690px;
  border-radius: 10px;
  color: #373737;
  box-shadow: 0 4px 9px rgba(0, 0, 0, 0.4);
}
.main-div {
  display: flex;
}

.login-title {
  margin-left: 4vw;
  padding-bottom: 3vh;
  font-size: 3.6vh;
  margin-bottom: 3vh;
  font-weight: bold;
  color: #373737;
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.input-title {
  margin-left: 0.7vw;
  font-size: 1.8vh;
}

input {
  margin: 1.3vh;
}

.form-control {
  width: 500px;
  height: 5.5vh;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #717171;
  transition: border 0.4s ease;
  padding-left: 15px;
  font-size: 2vh;
  color: #373737;
  margin-bottom: 3vh;
}

.form-control:focus {
  outline: 0;
  border-color: #151c62;
}

.form-control::placeholder {
  color: gray;
  font-size: 1.6vh;
  transition: all 0.4s ease;
}

.form-control:focus::placeholder {
  opacity: 0;
}

.login-button {
  background-color: #151c62;
  width: 520px;
  height: 6vh;
  cursor: pointer;
  border-radius: 8px;
  font-size: 2.1vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 3vh;
}

.login-button:hover {
  background-color: #0e1241;
}

.input-error {
  border: 2px solid #ff4343;
}

.error-message {
  color: #ff4343;
}
</style>
../../api/auth.js ../../api/auth/auth.js
