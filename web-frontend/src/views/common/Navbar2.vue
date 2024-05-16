<template>
  <main>
    <nav class="main-menu">
      <div @click="handleLogoClick" class="logo">
        <img src="../../assets/icon/weblogo-b.png" alt="#" />
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
      <div>
        <button id="logout-btn" @click="clickLogout">로그아웃</button>
      </div>
    </nav>
  </main>
</template>

<script setup>
import { ref } from "vue";
import { useMoveStore } from "../../stores/move.js";
import { useAuthStore } from "../../stores/user.js";
import { storeToRefs } from "pinia";
import { logout } from "../../api/auth/auth.js";
import router from "@/router";
import { useSwal } from "@/composables/useSwal";

const store = useMoveStore();
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const activeNavItem = ref(0);
const swal = useSwal();

const navItems = [
  { name: "홈", icon: "fa fa-house", action: store.moveMain },
  { name: "포트홀 조회", icon: "fa fa-user", action: store.movePorthole },
  { name: "작업 정보", icon: "fa fa-calendar-check", action: store.moveTask },
  {
    name: "통계 자료",
    icon: "fa fa-person-running",
    action: store.moveStatistics,
  },
];

const showAlert = () => {
  swal({
    title: "로그아웃이 완료되었습니다.",
    icon: "info",
    confirmButtonText: "확인",
    width: "700px",
  });
};

function setActiveNavItem(index) {
  activeNavItem.value = index;
}

// 두 함수를 실행하는 래퍼 함수
function handleClick(index) {
  setActiveNavItem(index);
  navItems[index].action();
}

function handleLogoClick() {
  setActiveNavItem(0);
  store.moveMain();
}

const clickLogout = () => {
  logout(
    accessToken.value,
    (res) => {
      if (res.data.status == "SUCCESS") {
        console.log(res.data.message);
        store2.logoutfc();
        store.moveLogin();
        showAlert();
      } else {
        store2.logoutfc();
        store.moveLogin();
        console.log(res);
      }
    },
    (error) => {
      store2.logoutfc();
      store.moveLogin();
      console.log(error.response.data.message);
    }
  );
};

router.beforeEach((to, from) => {
  const store = useAuthStore();
  if (to.name === "Main") {
    setActiveNavItem(0);
  }
  if (to.name === "PortholeList") {
    setActiveNavItem(1);
  }
  if (to.name === "Statistics") {
    setActiveNavItem(3);
  }
  if (to.name === "TaskInfo") {
    setActiveNavItem(2);
  }
});
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
  cursor: pointer;
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
  font-size: 1.9vh;
  font-weight: bold;
  padding: 2.2vh 12px;
  margin-left: 20px;
  border-top-left-radius: 40px;
  border-bottom-left-radius: 40px;
  z-index: 10;
  transition: all 0.3s ease-out;
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
  background: linear-gradient(to right, #151c62 50%, white 50%);
  background-size: 200% 120%;
  background-position: right bottom;
  transition: all 0.3s ease-out;
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

.nav-item a:click {
  background-color: #ffffff;
  color: black;
}

#logout-btn {
  position: absolute;
  top: 88%;
  left: 2.45%;
  width: 7.5%;
  padding: 1.5vh 0vw;
  font-size: 1.9vh;
  font-weight: bold;
  text-decoration: none;
  border-radius: 5px;
  border: 1px solid #ffffff;
  color: #ffffff;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-block;
}

#logout-btn:hover {
  color: #373737;
  font-weight: bold;
  background-color: #ffffff;
}
</style>
