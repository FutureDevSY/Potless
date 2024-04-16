import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/home/Home.vue";
import Login from "../views/auth/Login.vue";

import PortholeList from "../views/porthole/PortholeList.vue";
import PortholeDetail from "../views/porthole/PortholeDetail.vue";

import PathLine from "../views/path/PathLine.vue";
import Statistics from "../views/statistics/Statistics.vue";

import TaskInfo from "../views/task/TaskInfo.vue";
import TaskInfoDetail from "../views/task/TaskInfoDetail.vue";

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
      children: [
        {
          path: "detail/:id",
          name: "PortholeDetail",
          component: PortholeDetail,
          props: true,
        },
      ],
    },
    {
      path: "/path",
      name: "PathLine",
      component: PathLine,
      props: true,
    },
    {
      path: "/path",
      name: "PathLine",
      component: PathLine,
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
      children: [
        {
          path: "detail/:id",
          name: "TaskInfoDetail",
          component: TaskInfoDetail,
          props: true,
        },
      ],
    },
  ],
});

export default router;
