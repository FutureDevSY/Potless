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
    const userId = ref(null);

    const login = (userData, userToken) => {
      isLoggedIn.value = true;
      accessToken.value = userToken;
      username.value = userData.data.memberInfo.memberName;
      areaId.value = userData.data.memberInfo.region;
      userId.value = userData.data.memberInfo.id;

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
      areaId.value = null;
      userId.value = null;
      username.value = "";
      areaName.value = "";
    };

    return {
      isLoggedIn,
      accessToken,
      username,
      areaId,
      areaName,
      userId,
      login,
      logoutfc,
    };
  },
  {
    persist: {
      enabled: true,
      strategies: [
        {
          storage: sessionStorage,
          paths: [
            "isLoggedIn",
            "accessToken",
            "username",
            "areaId",
            "areaName",
            "userId",
          ],
        },
      ],
    },
  }
);
