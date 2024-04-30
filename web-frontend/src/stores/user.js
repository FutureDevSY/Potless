import { ref } from "vue";
import { defineStore } from "pinia";

export const useAuthStore = defineStore(
  "authStore",
  () => {
    const isLoggedIn = ref(false);
    const accessToken = ref("");
    const username = ref("");

    const login = (userData) => {
      isLoggedIn.value = true;
      accessToken.value = userData.token;
      username.value = userData.memberName;
    };

    const logout = () => {
      isLoggedIn.value = false;
      accessToken.value = null;
      username.value = "";
    };

    return {
      isLoggedIn,
      accessToken,
      username,
      login,
      logout,
    };
  },
  {
    persist: {
      storage: sessionStorage,
    },
  }
);
