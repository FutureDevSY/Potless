<template>
  <div
    :class="[
      'dropdown-container',
      { 'dropdown-open': showDropdown, 'dropdown-selected': selected },
      customClass,
    ]"
    :style="customStyle"
    @click="toggleDropdown"
  >
    <div :class="['dropdown-display', { 'dropdown-selected': selected }, customClass]">
      {{ selected || defaultText }}
    </div>
    <img src="../../../assets/icon/select.png" alt="#" />
    <transition name="fade">
      <ul
        v-show="showDropdown"
        :class="['dropdown-list', { 'dropdown-open': showDropdown }]"
        @click.stop
      >
        <li v-for="option in options" :key="option" @click="selectOption(option, $event)">
          {{ option }}
        </li>
      </ul>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const props = defineProps({
  options: Array,
  defaultText: String,
  customClass: String,
  customStyle: Object,
});

const emit = defineEmits(["update:selected"]);
const showDropdown = ref(false);
const selected = ref("");
const selectedText = ref("");

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const selectOption = (option, event) => {
  event.stopPropagation();
  if (selected.value !== option) {
    selected.value = option;
    selectedText.value = option;
  } else {
    selected.value = "";
    selectedText.value = "";
  }
  showDropdown.value = false;
  emit("update:selected", selected.value);
};

const handleGlobalClick = (event) => {
  if (!event.target.closest(".dropdown-container")) {
    showDropdown.value = false;
  }
};

onMounted(() => {
  document.addEventListener("click", handleGlobalClick);
});

onUnmounted(() => {
  document.removeEventListener("click", handleGlobalClick);
});
</script>

<style scoped>
.dropdown-container {
  position: relative;
  border: 2px solid #d6d6d6;
  width: 100%;
  height: 100%;
  border-radius: 6px;
  background-color: white;
  cursor: pointer;
  align-items: center;
  display: flex;
  justify-content: center;
}

.dropdown-container.dropdown-open {
  border: 2px solid #0070e88b;
}

.dropdown-container.dropdown-selected {
  border-color: #a2a2a2;
  background-color: #efefef6f;
}

.dropdown-display {
  margin: 0 5px;
  font-size: 1.7vh;
  font-weight: bold;
  color: #b5b5b5;
  margin-right: 20px;
}

.dropdown-display.dropdown-selected {
  color: #666666;
  font-size: 1.7vh;
  font-weight: bold;
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
  top: 110%;
  left: 0;
  right: 0;
  border: 2px solid #cacaca;
  list-style-type: none;
  padding: 0;
  margin: 0;
  background-color: white;
  border-radius: 6px;
  z-index: 1000;
}

.dropdown-list.dropdown-open {
  border-color: #0070e88b;
}

.dropdown-list.active {
  opacity: 1;
  transform: translateY(0);
  display: block;
}

.dropdown-list li {
  padding: 1.8vh;
  font-size: 1.7vh;
  font-weight: bold;
  color: #2f2f2f;
  text-align: center;
}

.dropdown-list li:hover {
  background-color: #f6f6f6;
}

.dropdown-list li:last-child {
  border-bottom: none;
}

img {
  position: absolute;
  right: 5%;
  height: 2.4vh;
}
</style>
