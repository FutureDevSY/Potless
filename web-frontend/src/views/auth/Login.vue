<template>
  <div class="main">
    <div :class="{ 'left-div': true, hidden: loginSuccess }" :style="{ flex: leftDivFlex }">
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
    <div class="right-div" :style="{ flex: rightDivFlex }"></div>
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
const authIdError = ref(false);
const authPasswordError = ref(false);
const showError = ref(false);
const errorMsg = ref("아이디와 비밀번호를 다시 입력해주세요.");
const loginSuccess = ref(false);
const leftDivFlex = ref("1");
const rightDivFlex = ref("1");

const doLogin = () => {
  console.log("doLogin 함수 호출됨"); // 디버깅을 위해 추가
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
        store2.moveMain();
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
  height: 95vh;
  display: flex;
  transition: flex 0.4s ease;
}
.left-div {
  flex: 1;
  display: grid;
  transition: opacity 0.4s ease, max-width 0.4s ease;
  grid-template-rows: 32% 38% 30%;
}
.left-div.hidden {
  opacity: 0;
  max-width: 10%;
  flex-grow: 1;
}
.login-title {
  margin-top: 3vh;
  font-size: 4.5vh;
  font-weight: bold;
  color: #373737;
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
}
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 60vh;
}
.input-group {
  width: 50%;
  margin-bottom: 4vh;
}
.form-control {
  width: 94%;
  height: 5.5vh;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #717171;
  transition: border 0.4s ease;
  font-size: 2vh;
  padding-left: 0.5vw;
  color: #373737;
  margin-bottom: 1vh;
}
.form-control:focus {
  outline: 0;
  border-color: #151c62;
}

.form-control::placeholder {
  color: gray;
  font-size: 1.8vh;
  transition: all 0.4s ease;
}

.form-control:focus::placeholder {
  opacity: 0;
}
.login-button {
  background-color: #151c62;
  margin: 0vh 12vw;
  width: 48.7%;
  height: 6.5vh;
  padding: 0vh 3vw;
  cursor: pointer;
  border-radius: 8px;
  font-size: 2.1vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  font-weight: bold;
  transition: all 0.3s;
  margin-top: 4vh;
}
.login-button:hover {
  background-color: #0e1241;
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
  transition: flex-grow 0.4s ease;

  background-color: rgb(254, 255, 240);
}
</style>
