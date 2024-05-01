import { ref } from "vue";
import { defineStore } from "pinia";

export const useAuthStore = defineStore(
  "authStore",
  () => {
    const isLoggedIn = ref(false);
    const accessToken = ref("");
    const username = ref("");
    const areaId = ref(null);
    const areaName = ref("");

    const login = (userData, userToken) => {
      isLoggedIn.value = true;
      accessToken.value = userToken;
      username.value = userData.data.memberInfo.memberName;
      areaId.value = userData.data.memberInfo.region;

      const areaIdToName = {
        1: "대덕구",
        2: "동구",
        3: "중구",
        4: "유성구",
        5: "서구",
      };

      areaName.value = areaIdToName[areaId.value];
    };

    const logoutfc = () => {
      isLoggedIn.value = false;
      accessToken.value = null;
      username.value = "";
    };

    return {
      isLoggedIn,
      accessToken,
      username,
      areaId,
      areaName,
      login,
      logoutfc,
    };
  },
  {
    persist: {
      storage: sessionStorage,
    },
  }
);
