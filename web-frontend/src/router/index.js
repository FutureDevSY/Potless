import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/home/Home.vue";
import Login from "../views/auth/Login.vue";

import PortholeList from "../views/porthole/PortholeList.vue";
import PortholeDetail from "../views/porthole/PortholeDetail.vue";
import SelfRegister from "../views/porthole/SelfRegister.vue";

import Statistics from "../views/statistics/Statistics.vue";

import TaskInfo from "../views/task/TaskInfo.vue";
import TaskInfoDetail from "../views/task/TaskInfoDetail.vue";

import Test from "../views/home/Test.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "Home",
      component: Home,
      props: true,
    },
    {
      path: "/auth/login",
      name: "Login",
      component: Login,
      props: true,
    },
    {
      path: "/porthole",
      name: "PortholeList",
      component: PortholeList,
      props: true,
    },
    {
      path: "/porthole/:id",
      name: "PortholeDetail",
      component: PortholeDetail,
      props: true,
    },
    {
      path: "/porthole/register",
      name: "SelfRegister",
      component: SelfRegister,
      props: true,
    },
    {
      path: "/statistics",
      name: "Statistics",
      component: Statistics,
      props: true,
    },
    {
      path: "/taskinfo",
      name: "TaskInfo",
      component: TaskInfo,
      props: true,
    },
    {
      path: "/taskinfo/:id",
      name: "TaskInfoDetail",
      component: TaskInfoDetail,
      props: true,
    },
    {
      path: "/test",
      name: "Test",
      component: Test,
      props: true,
    },
  ],
});

import { useAuthStore } from "../stores/user";

router.beforeEach((to, from) => {
  const store = useAuthStore();
  if (to.name === "PortholeList" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "PortholeDetail" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "SelfRegister" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "Statistics" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "TaskInfo" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "TaskInfoDetail" && !store.isLoggedIn) {
    window.alert("로그인이 필요합니다.");
    return { name: "Login" };
  }
  if (to.name === "Login" && store.isLoggedIn) {
    window.alert("이미 로그인 했습니다.");
    return { name: "Home" };
  }
});

export default router;
