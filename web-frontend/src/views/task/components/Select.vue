<template>
  <div class="dropdown-container" @click="toggleDropdown">
    <div class="dropdown-display">{{ selectedStatus || "작업 상태" }}</div>
    <img src="../../../assets/icon/select.png" alt="#" />
    <transition name="fade">
      <ul v-show="showDropdown" class="dropdown-list">
        <li @click="selectOption('작업 미완료', $event)">작업 미완료</li>
        <li @click="selectOption('작업 완료', $event)">작업 완료</li>
      </ul>
    </transition>
  </div>
</template>

<script setup>
import { ref } from "vue";

const showDropdown = ref(false);
const selectedStatus = ref("");

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const selectOption = (status, event) => {
  event.stopPropagation();
  selectedStatus.value = status;
  showDropdown.value = false;
};
</script>

<style scoped>
.dropdown-container {
  position: relative;
  border: 1px solid #ccc;
  display: flex;
  width: 100px;
  align-items: center;
  padding: 8px;
  background-color: white;
  cursor: pointer;
  justify-content: space-between;
  margin-right: 8px;
}

.dropdown-display {
  margin: 0 5px;
  font-size: 12px;
}

.dropdown-icon {
  margin-left: 8px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  border: 1px solid #ccc;
  list-style-type: none;
  padding: 0;
  margin: 0;
  background-color: white;
  z-index: 1000;
}

.dropdown-list.active {
  opacity: 1;
  transform: translateY(0);
  display: block;
}

.dropdown-list li {
  padding: 8px;
  border-bottom: 1px solid #eee;
  font-size: 12px;
}

.dropdown-list li:hover {
  background-color: #f6f6f6;
}

.dropdown-list li:last-child {
  border-bottom: none;
}

img {
  margin-left: 10px;
  height: 20px;
}
</style>
