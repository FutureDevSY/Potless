<template>
  <div class="pagination-container">
    <div
      class="page-item"
      @click="setCurrentPage(currentPage - 5)"
      :class="{ disabled: isPrevGroupDisabled }"
    >
      «
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
      »
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";

const props = defineProps({
  totalpage: Number,
});

const totalPages = props.totalpage;
const currentPage = ref(1);
const visiblePages = 5;
const emit = defineEmits(["update:current-page"]);

const pageNumbers = computed(() => {
  let start = Math.floor((currentPage.value - 1) / visiblePages) * visiblePages;
  return Array.from({ length: visiblePages }, (_, i) => start + i + 1).filter(
    (page) => page <= totalPages
  );
});

function setCurrentPage(page) {
  if (page < 1) {
    currentPage.value = 1;
  } else if (page > totalPages) {
    currentPage.value = totalPages;
  } else {
    currentPage.value = page;
  }
  emit("update:current-page", currentPage.value);
}

const isPrevGroupDisabled = computed(() => currentPage.value <= visiblePages);
const isNextGroupDisabled = computed(
  () => currentPage.value > totalPages - visiblePages
);

watch(currentPage, (newValue) => {
  if (!pageNumbers.value.includes(newValue)) {
    setCurrentPage(
      Math.floor((newValue - 1) / visiblePages) * visiblePages + 1
    );
  }
});
</script>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  margin-top: 100px;
}

.page-item {
  color: #333;
  padding: 8px 12px;
  margin: 0 4px;
  text-decoration: none;
  border: 1px solid #ddd;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.page-item:hover {
  background-color: #eee;
}

.page-item.active {
  background-color: #666;
  color: white;
  border-color: #666;
}
</style>
