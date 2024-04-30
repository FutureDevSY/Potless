<template>
  <div class="input-container">
    <input type="text" placeholder="지역(동) 또는 도로명" />
    <img
      src="../../../assets/icon//search.png"
      alt="#"
      class="search-icon"
      :value="inputValue"
      @input="updateInput"
    />
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
  padding: 8px 32px 8px 8px;
  border: 1px solid #ccc;
  width: 200px;
  font-size: 12px;
  height: 20px;
}

.search-icon {
  position: absolute;
  right: 10px;
  cursor: pointer;
  margin-left: 10px;
  height: 20px;
}
</style>
