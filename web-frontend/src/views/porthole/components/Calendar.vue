<template>
  <div class="date-picker">
    <VDatePicker
      v-model.range="dateRange"
      :select-attribute="selectDragAttribute"
      :drag-attribute="selectDragAttribute"
      @drag="dragValue = $event"
    >
      <template #default="{ togglePopover, inputValue, inputEvents }">
        <div class="input" @click="() => togglePopover()" tabindex="0">
          {{ formattedDateRange }}
          <img src="../../../assets/icon/calendar.png" alt="#" @click="() => togglePopover()" />
        </div>
      </template>
    </VDatePicker>
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";

const emit = defineEmits(["update:dateRange"]);
const getUtcDate = (date) => {
  return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
};

const today = getUtcDate(new Date());
const oneWeekAgo = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 6);

const formatDate = (date) => {
  return date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
};

const dateRange = ref({
  start: oneWeekAgo,
  end: today,
});

const dragValue = ref(null);
const selectDragAttribute = computed(() => ({
  popover: {
    visibility: "hover",
    isInteractive: false,
  },
}));
const formattedDateRange = ref("");

watch(
  dateRange,
  (newRange) => {
    if (newRange.end >= today) {
      dateRange.value.end = today;
    }
    formattedDateRange.value = `${formatDate(newRange.start)} - ${formatDate(newRange.end)}`;
    emit("update:dateRange", { ...dateRange.value });
  },
  { immediate: true, deep: true }
);
</script>

<style scoped>
.date-picker {
  display: flex;
  padding: 5px;
  width: 350px;
  font-size: 16px;
  justify-content: end;
  z-index: 5;
}

.input {
  display: flex;
  align-items: center;
  background-color: white;
  border: 1px solid #ccc;
  padding: 1.1vh;
  cursor: pointer;
}

button {
  margin-left: 10px;
  padding: 6px 12px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  cursor: pointer;
}

button:hover {
  background-color: #e1e1e1;
}

img {
  margin-left: 10px;
  height: 24px;
}
</style>
