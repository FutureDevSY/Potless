<template>
  <div class="pagination-container">
    <div
      class="page-item"
      @click="setCurrentPage(currentPage - 5)"
      :class="{ disabled: isPrevGroupDisabled }"
    >
      <img src="../../../assets/icon/previous.png" alt="previous-button" />
    </div>
    <div
      class="page-item"
      v-for="page in pageNumbers"
      :key="page"
      :class="{ active: currentPage === page }"
      @click="setCurrentPage(page)"
    >
      {{ page }}
    </div>
    <div
      class="page-item"
      @click="setCurrentPage(currentPage + 5)"
      :class="{ disabled: isNextGroupDisabled }"
    >
      <img src="../../../assets/icon/next.png" alt="next-button" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, watchEffect } from "vue";

const props = defineProps({
  totalPage: Number,
});

const totalPages = ref(props.totalPage);
watchEffect(() => {
  totalPages.value = props.totalPage;
});
const currentPage = ref(1);
const visiblePages = 5;
const emit = defineEmits(["update:current-page"]);

const pageNumbers = computed(() => {
  let start = Math.floor((currentPage.value - 1) / visiblePages) * visiblePages;
  return Array.from({ length: visiblePages }, (_, i) => start + i + 1).filter(
    (page) => page <= totalPages.value
  );
  return numbers;
});

function setCurrentPage(page) {
  if (page < 1) {
    currentPage.value = 1;
  } else if (page > totalPages.value) {
    currentPage.value = totalPages.value;
  } else {
    currentPage.value = page;
  }
  emit("update:current-page", currentPage.value - 1);
}

const isPrevGroupDisabled = computed(() => currentPage.value <= visiblePages);
const isNextGroupDisabled = computed(() => currentPage.value > totalPages - visiblePages);

watch(currentPage, (newValue) => {
  if (!pageNumbers.value.includes(newValue)) {
    setCurrentPage(Math.floor((newValue - 1) / visiblePages) * visiblePages + 1);
  }
});
</script>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.page-item {
  color: #333;
  font-size: 2vh;
  padding: 1vh 0.9vw;
  margin: 0.5vh 4px;
  text-decoration: none;
  border: 1px solid #cecece;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  margin-top: 1vh;
}

.page-item:hover {
  background-color: #eee;
}

.page-item.active {
  background-color: #959595;
  color: white;
  border-color: #bbbbbb;
}

img {
  height: 1.2vh;
  width: 0.6vw;
  padding-bottom: 0.3vh;
}
</style>
