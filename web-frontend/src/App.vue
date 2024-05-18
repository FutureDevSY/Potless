<script setup>
import { RouterView, useRoute } from "vue-router";
import Navbar from "./views/common/Navbar2.vue";

const isVisibleNavbar = () => {
  const visibleRoutes = ["Home", "Login"];
  const currentRouteName = useRoute().name;
  return visibleRoutes.includes(currentRouteName);
};
</script>

<template>
  <body v-if="!isVisibleNavbar()">
    <div class="mained-layout">
      <Navbar />
      <RouterView />
    </div>
  </body>
  <body v-else>
    <div class="main-layout2">
      <RouterView />
    </div>
  </body>
</template>

<style scoped>
@import url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css");
body {
  font-family: "Pretendard", sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  margin: 0;
  background-color: #d3d5ed;
  background-repeat: no-repeat;
  background-size: cover;
  box-sizing: border-box;
}

.mained-layout {
  display: grid;
  grid-template-columns: 10% 90%;
  width: calc(100% - 5vh);
  height: 95vh;
  background: #f8f8f8;
  box-shadow: 0 0.5px 0 1px rgba(255, 255, 255, 0.23) inset,
    0 1px 0 0 rgba(255, 255, 255, 0.66) inset, 0 4px 16px rgba(0, 0, 0, 0.12);
  border-radius: 15px;
  z-index: 10;
  min-height: 0;
}
.main-layout2 {
  width: calc(100% - 5vh);
  height: 95vh;
  background: rgb(254, 254, 254);
  box-shadow: 0 0.5px 0 1px rgba(255, 255, 255, 0.23) inset,
    0 1px 0 0 rgba(255, 255, 255, 0.66) inset, 0 4px 16px rgba(0, 0, 0, 0.12);
  border-radius: 15px;
  z-index: 10;
  min-height: 0;
}
</style>
