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
import { ref, computed, watch, watchEffect, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const props = defineProps({
  totalPage: Number,
  pageInfo: Number,
});

const totalPages = ref(props.totalPage);
watchEffect(() => {
  if (props.totalPage != null) {
    totalPages.value = props.totalPage;
  }
});

const currentPage = ref(props.pageInfo || 1);
const visiblePages = 5;
const emit = defineEmits(["update:current-page"]);

const pageNumbers = computed(() => {
  if (totalPages.value) {
    let start =
      Math.floor((currentPage.value - 1) / visiblePages) * visiblePages;
    return Array.from({ length: visiblePages }, (_, i) => start + i + 1).filter(
      (page) => page <= totalPages.value
    );
  }
  return [];
});

function setCurrentPage(page) {
  if (page != null && !isNaN(page)) {
    page = Math.max(1, Math.min(page, totalPages.value || 1));
    currentPage.value = page;
    if (router && page !== parseInt(route.query.page)) {
      router.push({ query: { ...route.query, page: page.toString() } });
    }
    emit("update:current-page", page - 1);
  }
}

const isPrevGroupDisabled = computed(() => currentPage.value <= visiblePages);
const isNextGroupDisabled = computed(
  () => currentPage.value > totalPages.value - visiblePages
);

watch(currentPage, (newValue) => {
  if (!pageNumbers.value.includes(newValue) && totalPages.value) {
    setCurrentPage(
      Math.floor((newValue - 1) / visiblePages) * visiblePages + 1
    );
  }
});

onMounted(() => {
  const pageFromQuery = parseInt(route.query.page) || props.pageInfo || 1;
  setCurrentPage(pageFromQuery);
});
</script>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  margin-top: 3vh;
}

.page-item {
  color: #333;
  font-size: 2vh;
  padding: 10px 15px;
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
  background-color: #bbbbbb;
  color: white;
  border-color: #bbbbbb;
}

img {
  height: 10px;
  width: 10px;
  padding-bottom: 3px;
}
</style>
