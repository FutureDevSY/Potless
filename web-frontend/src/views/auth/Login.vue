<template>
  <div class="container">
    <div class="login-box">
      <div class="login-title">관리자 로그인</div>
      <form class="login-form" @submit.prevent="moveHome">
        <div class="input-group">
          <div class="input-title">아이디</div>
          <input
            class="form-control"
            :class="{ 'input-error': showIdError }"
            type="text"
            v-model="auth_id"
            placeholder="아이디를 입력해 주세요."
            @input="showIdError = false"
          />
        </div>
        <div class="input-group">
          <div class="input-title">비밀번호</div>
          <input
            class="form-control"
            :class="{ 'input-error': showPasswordError }"
            type="password"
            v-model="auth_password"
            placeholder="비밀번호를 입력해 주세요."
            @input="showError = false"
          />
          <div v-if="showPasswordError" class="error-message">
            {{ errorMsg }}
          </div>
        </div>
        <button class="login-button" type="submit" @click="doLogin">
          <span class="button-text">로그인</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../../stores/user.js";
import { login } from "../../api/auth/auth";

const store = useAuthStore();
const router = useRouter();
const auth_id = ref("");
const auth_password = ref("");
const showError = ref(false);
const errorMsg = ref("");

const doLogin = () => {
  const loginData = ref({});

  loginData.append("email", auth_id.value);
  loginData.append("password", auth_password.value);

  login(
    loginData,
    ({ data }) => {
      if (data.status == "success") {
        store.login();
      }
    },
    ({ error }) => {
      showError.value = true;
      errorMsg.value = error.message;
    }
  );
};

const moveHome = () => {
  if (!auth_password.value) {
    showError.value = true;
  }
  if (auth_id.value && auth_password.value) {
    router.push("/");
  }
};
</script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  flex-direction: column;
  height: calc(100vh - 125px);
  background-color: #f8f8f8;
}

.login-box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #ffffff;
  height: 57vh;
  width: 480px;
  margin-top: 10vh;
  color: #373737;
  box-shadow: 0 4px 9px rgba(0, 0, 0, 0.4);
}

.login-title {
  margin-left: 3.6vw;
  padding-bottom: 3vh;
  font-size: 3.6vh;
  margin-bottom: 4vh;
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
  font-size: 14px;
}

input {
  margin: 1.3vh;
}

.form-control {
  width: 350px;
  height: 40px;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: none;
  border: 2px solid #717171;
  transition: border 0.4s ease;
  padding-left: 15px;
  font-size: 18px;
  color: #373737;
  margin-bottom: 3vh;
}

.form-control:focus {
  outline: 0;
  border-color: #151c62;
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
  background-color: #151c62;
  width: 370px;
  height: 45px;
  cursor: pointer;
  border-radius: 8px;
  font-size: 20px;
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
