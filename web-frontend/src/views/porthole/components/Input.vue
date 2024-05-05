<template>
  <div class="input-container">
    <input
      type="text"
      placeholder="지역(동) 또는 도로명"
      :value="inputValue"
      @input="updateInput"
    />
    <img src="../../../assets/icon//search.png" alt="#" class="search-icon" />
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["update:value"]);
const inputValue = ref("");

const updateInput = (event) => {
  inputValue.value = event.target.value;
  emit("update:value", inputValue.value);
};
</script>

<style scoped>
.input-container {
  position: relative;
  display: flex;
  align-items: center;
  margin-right: 8px;
}

input[type="text"] {
  padding: 1.3vh 2px 1.3vh 8px;
  border: 1px solid #bcbcbc;
  width: 250px;
  font-size: 15px;
}

input[type="text"]:focus {
  outline: 0;
  border: 1px solid #6d6d6d;
}

.search-icon {
  position: absolute;
  right: 10px;
  cursor: pointer;
  margin-left: 10px;
  height: 3vh;
}
</style>
