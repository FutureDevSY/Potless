import { defineStore } from "pinia";
import { ref } from "vue";

export const useDistrictStore = defineStore("districtStore", () => {
  const myValue = ref("");

  function setMyValue(newValue) {
    myValue.value = newValue;
  }

  return { myValue, setMyValue };
});
