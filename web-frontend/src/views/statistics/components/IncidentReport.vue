<template>
  <div class="data-container">
    <div class="data-header">
      <div class="data-title">{{ title }}</div>
      <div class="data-number">{{ number }}</div>
    </div>
    <div class="data-footer">
      <div class="data-subtitle">{{ subtitle }}</div>
      <div
        class="data-percent"
        :class="{ positive: isPositive, negative: !isPositive }"
      >
        {{ percent }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from "vue";

const props = defineProps({
  title: String,
  number: [String, Number],
  percent: String,
  subtitle: String,
});

const isPositive = computed(() => props.percent.charAt(0) === "+");
</script>

<style scoped>
.data-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  margin: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 200px;
}

.data-header,
.data-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.data-title,
.data-subtitle {
  font-size: 14px;
  color: #555;
}

.data-number {
  font-size: 18px;
  font-weight: bold;
}

.data-percent {
  font-size: 16px;
}

.positive {
  color: #4caf50;
}

.negative {
  color: #f44336;
}
</style>
