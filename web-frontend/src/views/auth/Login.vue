<template>
  <div class="main">
    <div class="left-div">
      <div class="login-title">관리자 로그인</div>
      <form class="login-form" @submit.prevent="doLogin">
        <div class="input-group">
          <div class="input-title">아이디</div>
          <input
            class="form-control"
            type="text"
            v-model="auth_id"
            placeholder="아이디를 입력해 주세요."
            @keydown.enter="doLogin"
          />
          <div v-if="authIdError" class="error-message">아이디는 필수값입니다.</div>
        </div>
        <div class="input-group">
          <div class="input-title">비밀번호</div>
          <input
            class="form-control"
            type="password"
            v-model="auth_password"
            placeholder="비밀번호를 입력해 주세요."
            @input="showError = false"
            @keydown.enter="doLogin"
          />
          <div v-if="authPasswordError" class="error-message">비밀번호는 필수값입니다.</div>
          <div v-if="showError" class="error-message">
            {{ errorMsg }}
          </div>
        </div>
        <button class="login-button" type="submit">
          <span class="button-text">로그인</span>
        </button>
      </form>
    </div>
    <div class="right-div">
      <img class="logo" src="../../assets/icon/weblogo.png" alt="#" />
      <div class="slogan">
        <!-- <img class="helmet-img" src="../../assets/icon/helmet.png" alt="#" /> -->
        <!-- <img class="work-img" src="../../assets/icon/road-work.png" alt="#" /> -->
        <img class="road-img" src="../../assets/icon/road.png" alt="road" />

        <div class="slogan-left">포트홀 없는 길,</div>
        <div class="slogan-right">신속한 보수 공사,</div>
        <div class="slogan-center">우리가 만들어 갑니다.</div>
      </div>
    </div>
    <div class="invisible-div" :class="{ 'login-success-visible': loginSuccess }"></div>
    <div class="lottie-car-container" :class="{ 'login-success-car': loginSuccessCar }">
      <LottieCar class="lottie-car" />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../../stores/user.js";
import { login } from "../../api/auth/auth";
import { useMoveStore } from "@/stores/move";
import LottieCar from "./components/LottieCar.vue";

const store = useAuthStore();
const store2 = useMoveStore();
const auth_id = ref("");
const auth_password = ref("");
const authIdError = ref(false);
const authPasswordError = ref(false);
const showError = ref(false);
const errorMsg = ref("아이디와 비밀번호를 다시 입력해주세요.");
const loginSuccess = ref(false);
const loginSuccessCar = ref(false);

const doLogin = () => {
  console.log("doLogin 함수 호출됨");
  authIdError.value = !auth_id.value;
  authPasswordError.value = !auth_password.value;

  if (authIdError.value || authPasswordError.value) {
    return;
  }

  const loginData = {
    email: auth_id.value,
    password: auth_password.value,
  };

  login(
    loginData,
    (res) => {
      console.log(res);
      if (res.data.status === "SUCCESS") {
        console.log(res.data.message);
        store.login(res.data, res.data.data.token);
        loginSuccess.value = true;
        setTimeout(() => {
          loginSuccessCar.value = true;
        }, 1040);
        setTimeout(() => {
          store2.moveMain();
        }, 3000);
      } else {
        errorMsg.value = res.data.message;
        showError.value = true;
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
.main {
  height: 79vh;
  display: flex;
  position: relative;
  overflow: hidden;
  border-radius: 15px;
  margin: 8vh 10vw;
  box-shadow: 0 4px 7px rgba(0, 0, 0, 0.255);
}
.left-div {
  flex: 1;
  display: grid;
  grid-template-rows: 32% 38% 30%;
  background-color: #151c62;
  position: relative;
  z-index: 1;
}
.login-title {
  margin-top: 1vh;
  font-size: 4.5vh;
  font-weight: bold;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
input {
  margin: 1.3vh 0vw;
}
.input-title {
  font-size: 2vh;
  font-weight: bold;
  color: #ffffff;
}
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 50vh;
}
.input-group {
  width: 50%;
  margin-bottom: 4vh;
}
.form-control {
  width: 94%;
  height: 5vh;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #ffffff;
  transition: border 0.4s ease;
  font-size: 2vh;
  padding-left: 0.5vw;
  color: #ffffff;
  margin-bottom: 1vh;
}
.form-control:focus {
  outline: 0;
  border-color: #767676;
}

.form-control::placeholder {
  color: #afafaf;
  font-size: 1.8vh;
  transition: all 0.4s ease;
}

.form-control:focus::placeholder {
  opacity: 0;
}
.login-button {
  margin: 0vh 12vw;
  width: 50%;
  height: 7vh;
  padding: 0vh 3vw;
  cursor: pointer;
  border-radius: 8px;
  font-size: 2.2vh;
  position: relative;
  overflow: hidden;
  color: #373737;
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 4vh;
}
.login-button:hover {
  background-color: #c6c6c6;
  color: #101010;
}
.input-error {
  border: 2px solid #ff4343;
}
.error-message {
  color: #ff4343;
  font-size: 1.8vh;
}

.right-div {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f1f1f1;
}

.logo {
  margin-top: 7vh;
  height: 9vh;
}

.slogan {
  width: 65%;
  margin: 12vh 0;
  position: relative;
}
.slogan-left {
  font-size: 3.2vh;
  font-weight: bold;
  color: #565656;
  text-align: center;
  margin-bottom: 5vh;
}

.slogan-right {
  font-size: 3.2vh;
  font-weight: bold;
  color: #565656;
  text-align: center;
  margin-bottom: 6vh;
}

.slogan-center {
  font-size: 3.8vh;
  font-weight: bold;
  color: #313131;
  text-align: center;
  width: 100%;
}

.helmet-img {
  position: absolute;
  top: -4vh;
  right: 18vw;
  height: 9vh;
}

.work-img {
  position: absolute;
  top: 5vh;
  right: -2vw;
  height: 14vh;
}
.road-img {
  position: absolute;
  top: 28vh;
  right: 2vw;
  height: 22vh;
}

.invisible-div {
  position: absolute;
  top: 0;
  right: 0;
  width: 0;
  height: 100%;
  background-color: rgb(255, 255, 255);
  transition: width 3s ease-in-out, background-color 3s ease-in-out;
  z-index: 2;
}

.lottie-car-container {
  position: absolute;
  bottom: -60px;
  right: calc(50% + 40px);
  transform: translateX(50%);
  width: 200px;
  height: 200px;
  pointer-events: none;
  transition: transform 2s ease-in-out;
  z-index: 10;
}

.lottie-car {
  width: 100%;
  transform: scaleX(-1);
  height: 100%;
}

.login-success-visible {
  width: 90%;
}

.login-success-car {
  transform: translateX(-345%);
}
</style>
