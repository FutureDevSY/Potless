<template>
  <main>
    <nav class="main-menu">
      <div class="logo">
        <img src="../../assets/icon/weblogo.png" alt="#" />
      </div>
      <ul>
        <li
          v-for="(item, index) in navItems"
          :key="index"
          :class="{ 'nav-item': true, active: activeNavItem === index }"
          @click="() => handleClick(index)"
        >
          <b></b>
          <b></b>
          <a href="#">
            <!-- <p>-</p> 여기에 아이콘-->
            <span class="nav-text">{{ item.name }}</span>
          </a>
        </li>
      </ul>
    </nav>
  </main>
</template>

<script setup>
import { ref } from "vue";
import { useMoveStore } from "../../stores/move.js";

const store = useMoveStore();
const activeNavItem = ref(0);

const navItems = [
  { name: "홈", icon: "fa fa-house", action: 0 },
  { name: "포트홀 조회", icon: "fa fa-user", action: store.movePorthole },
  { name: "작업 정보", icon: "fa fa-calendar-check", action: store.moveTask },
  { name: "통계 자료", icon: "fa fa-person-running", action: store.moveStatistics },
];

function setActiveNavItem(index) {
  activeNavItem.value = index;
}

// 두 함수를 실행하는 래퍼 함수
function handleClick(index) {
  setActiveNavItem(index);
  navItems[index].action();
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Nunito:wght@200;300;400;500;600;700;800;900;1000&family=Roboto:wght@300;400;500;700&display=swap");

*,
*::before,
*::after {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}

nav {
  user-select: none;
  flex-direction: column;
  align-items: center;
  font-family: "Roboto", sans-serif;
  z-index: 10;
}

.main-menu {
  height: 100%;
  background: #151c62;
  padding-top: 10px;
  border-radius: 15px 0 0 15px;
  font-family: "Roboto", sans-serif;
}

nav ul,
nav ul li {
  outline: 0;
}

nav ul li a {
  text-decoration: none;
}

.logo {
  display: flex;
  justify-content: center;
  width: 100%;
  margin: 4vh auto 4vh auto;
}

img {
  width: 90%;
}

.nav-item {
  position: relative;
  display: block;
}

.nav-item a {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 2.2vh 0;
  margin-left: 20px;
  border-top-left-radius: 40px;
  border-bottom-left-radius: 40px;
  z-index: 10;
}

.nav-item b:nth-child(1) {
  position: absolute;
  top: -1.4vh;
  height: 1.4vh;
  width: 100%;
  background: #fff;
  display: none;
}

.nav-item b:nth-child(1)::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-bottom-right-radius: 20px;
  background: #151c62;
}

.nav-item b:nth-child(2) {
  position: absolute;
  bottom: -1.4vh;
  height: 1.4vh;
  width: 100%;
  background: #fff;
  display: none;
}

.nav-item b:nth-child(2)::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-top-right-radius: 20px;
  background: #151c62;
}

.nav-item.active b:nth-child(1),
.nav-item.active b:nth-child(2) {
  display: block;
}

.nav-item.active a {
  background-color: #ffffff;
  text-decoration: none;
  color: #555555;
}

.nav-item.active a::before,
.nav-item.active a::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 15px;
}

.nav-text {
  display: block;
  width: 120px;
}
</style>
