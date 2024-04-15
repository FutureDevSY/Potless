<template>
  <div class="navbar-container">
    <div class="logo">서비스 로고</div>
    <div class="move-page">
      <div class="porthole" @click="movePorthole">포트홀 조회</div>
      <div class="path" @click="movePath">공사 추천 경로</div>
      <div class="task" @click="moveTask">작업 정보</div>
      <button class="logout" @click="moveLogin">로그아웃</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();

const movePorthole = () => {
  router.push("/porthole");
};

const movePath = () => {
  router.push("/path");
};

const moveTask = () => {
  router.push("/taskinfo");
};

const moveLogin = () => {
  router.push("/auth/login");
};
</script>

<style scoped>
.navbar-container {
  background: #cdc9c9;
  width: 100%;
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.logo {
  margin: 0 20px;
}
.move-page {
  display: flex;
}
.move-page > div {
  margin: 0 20px;
}
.logout {
  height: 50%;
  margin: 0 20px;
}
</style>
