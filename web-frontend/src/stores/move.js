import { useRouter } from "vue-router";
import { defineStore } from "pinia";

export const useMoveStore = defineStore("moveStore", () => {
  const router = useRouter();

  const movePorthole = () => {
    router.push("/porthole");
  };

  const movePortholeDetail = (id) => {
    router.push(`/porthole/${id}`);
  };

  const moveSelfRegister = () => {
    router.push("/porthole/register");
  };

  const movePath = () => {
    router.push("/path");
  };

  const moveTask = () => {
    router.push("/taskinfo");
  };

  const moveLogin = () => {
    router.push("/");
  };

  const moveStatistics = () => {
    router.push("/statistics");
  };

  const moveTaskDetail = (id) => {
    router.push(`/taskinfo/${id}`);
  };

  const moveMain = () => {
    router.push("/main");
  };

  const moveBack = () => {
    router.back();
  };

  return {
    movePorthole,
    movePortholeDetail,
    moveSelfRegister,
    movePath,
    moveTask,
    moveLogin,
    moveStatistics,
    moveTaskDetail,
    moveMain,
    moveBack,
  };
});
