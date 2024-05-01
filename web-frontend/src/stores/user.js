import { ref } from "vue";
import { defineStore } from "pinia";

export const useAuthStore = defineStore(
  "authStore",
  () => {
    const isLoggedIn = ref(false);
    const accessToken = ref("");
    const username = ref("");
    const areaId = ref(null);

    const login = (userData, userToken) => {
      isLoggedIn.value = true;
      accessToken.value = userToken;
      username.value = userData.data.memberInfo.memberName;
      areaId.value = userData.data.memberInfo.region;
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
