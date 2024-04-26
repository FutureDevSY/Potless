<template>
  <div>
    <div class="main-navbar">
      <div class="logo text-title" @click="store.moveHome">POTLESS</div>
      <!-- <button class="logout text-title" @click="store.moveLogin">
        <img src="@/assets/icon/enter.png" alt="#" />
        <div>로그인</div>
      </button> -->
      <button class="logout text-title" @click="store.moveLogin">
        <img src="@/assets/icon/out.png" alt="#" />
        <div>로그아웃</div>
      </button>
    </div>

    <div class="sub-navbar">
      <div class="text-dark-title nav-item first-item" @click="store.movePorthole">포트홀 조회</div>
      <div class="text-dark-title nav-item" @click="store.moveTask">작업 정보</div>
      <div class="text-dark-title nav-item" @click="store.moveStatistics">통계 자료</div>
    </div>
  </div>
</template>

<script setup>
import { useMoveStore } from "../../stores/move.js";

const store = useMoveStore();
</script>

<style scoped>
.text-light-title {
  color: #1d1d1d;
}

.text-dark-title {
  color: white;
}

.main-navbar {
  background: #ffffff;
  width: 100%;
  height: 75px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  margin-left: 30px;
  font-size: 30px;
  font-weight: bold;
  cursor: pointer;
}

.logout {
  height: 70%;
  margin-right: 30px;
  padding: 0px 12px 0px 12px;
  font-size: 22px;
  font-weight: bold;
  background-color: white;
  border: none;
  border-radius: 8px;
  display: flex;
  align-items: center;
}

.logout:hover {
  background-color: #151b621c;
}

.sub-navbar {
  background: #151c62;
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
}

.nav-item {
  margin-right: 70px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  position: relative;
  padding-bottom: 3px;
  overflow: hidden;
}

.nav-item::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1.5px;
  background-color: white;
  transform: scaleX(0);
  transform-origin: left;
  transition: transform 0.2s ease-out;
}

.nav-item:hover::after {
  transform: scaleX(1);
}

.first-item {
  margin-left: 32px;
}
</style>
