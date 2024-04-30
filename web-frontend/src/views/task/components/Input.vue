<template>
  <div class="input-container">
    <input type="text" placeholder="작업 보고서명 혹은 작업 관리자 이름" />
    <img src="../../../assets/icon//search.png" alt="#" class="search-icon" />
  </div>
</template>

<script setup></script>

<style scoped>
.input-container {
  position: relative;
  display: flex;
  align-items: center;
  margin-right: 8px;
}

input[type="text"] {
  padding: 11px 32px 11px 8px;
  border: 1px solid #bcbcbc;
  width: 250px;
  font-size: 15px;
  height: 20px;
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
  height: 20px;
}
</style>
