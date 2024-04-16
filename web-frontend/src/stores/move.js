import { useRouter } from "vue-router";
import { defineStore } from "pinia";

export const useMoveStore = defineStore("moveStore", () => {
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

  const moveStatistics = () => {
    router.push("/statistics");
  };

  return {
    movePorthole,
    movePath,
    moveTask,
    moveLogin,
    moveStatistics,
  };
});
