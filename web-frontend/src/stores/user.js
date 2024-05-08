import { ref } from "vue";
import { defineStore } from "pinia";
import locations from "../views/porthole/components/location.json";

export const useAuthStore = defineStore(
  "authStore",
  () => {
    const isLoggedIn = ref(false);
    const accessToken = ref("");
    const username = ref("");
    const areaId = ref(null);
    const areaName = ref("");
    const userId = ref(null);
    const coordinates = ref({ x: null, y: null });

    const areaIdToName = {
      1: "대덕구",
      2: "동구",
      3: "중구",
      4: "유성구",
      5: "서구",
    };

    const login = (userData, userToken) => {
      isLoggedIn.value = true;
      accessToken.value = userToken;
      username.value = userData.data.memberInfo.memberName;
      areaId.value = userData.data.memberInfo.region;
      userId.value = userData.data.memberInfo.id;
      areaName.value = areaIdToName[areaId.value];

      // 좌표 설정
      const location = locations.find((loc) => loc.areaId === areaId.value);
      if (location) {
        coordinates.value = { x: location.x, y: location.y };
      }
    };

    const logoutfc = () => {
      isLoggedIn.value = false;
      accessToken.value = null;
      areaId.value = null;
      userId.value = null;
      username.value = "";
      areaName.value = "";
      coordinates.value = { x: null, y: null };
    };

    return {
      isLoggedIn,
      accessToken,
      username,
      areaId,
      areaName,
      userId,
      coordinates,
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
            "coordinates",
          ],
        },
      ],
    },
  }
);
