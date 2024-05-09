<script setup>
import { RouterView, useRoute } from "vue-router";
import Navbar from "./views/common/Navbar.vue";

const isVisibleNavbar = () => {
  const visibleRoutes = ["Home", "Test"];
  const currentRouteName = useRoute().name;
  return visibleRoutes.includes(currentRouteName);
};
</script>

<template>
  <Navbar v-if="!isVisibleNavbar()" />
  <RouterView />
</template>

<style scoped></style>
