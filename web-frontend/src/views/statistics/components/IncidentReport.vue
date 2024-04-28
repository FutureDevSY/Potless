<template>
  <div class="data-container">
    <div class="data-header">
      <div class="data-title text-light-title">{{ title }}</div>
      <div class="data-number text-light-title">{{ number }}</div>
    </div>
    <div class="data-footer">
      <div class="data-percent .text-light-title">
        <img class="updown-icon up-icon" v-if="isPositive" src="../../../assets/icon/up.png" alt="#" />
        <img class="updown-icon" v-if="!isPositive" src="../../../assets/icon/down.png" alt="#" />
        {{ formattedPercent }}
        <span class="tooltip">{{ tooltipText }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  title: String,
  number: [String, Number],
  percent: String,
  subtitle: String,
});

const isPositive = computed(() => props.percent.charAt(0) === '+');

const formattedPercent = computed(() => props.percent.substring(1));

const tooltipText = computed(() => {
  let comparisonText;
  switch (props.title) {
    case '오늘':
      comparisonText = '어제와 비교했을 때';
      break;
    case '1주일':
      comparisonText = '지난 1주일과 비교했을 때';
      break;
    case '1개월':
      comparisonText = '지난 1달과 비교했을 때';
      break;
  }

  return isPositive.value
    ? `${comparisonText} ${formattedPercent.value} 만큼 올랐습니다.`
    : `${comparisonText} ${formattedPercent.value} 만큼 내렸습니다.`;
});
</script>

<style scoped>
.text-light-title {
  color: #1d1d1d;
}
.data-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px 25px;
  margin: 5px 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.updown-icon {
  padding-top: 4px;
  width: 15px;
  height: auto;
}

.up-icon {
  transform: translateY(2px);
}

.data-header,
.data-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.data-footer {
  margin-top: 10px;
  justify-content: end;
}

.data-title {
  font-size: 16px;
}

.data-number {
  font-size: 20px;
  font-weight: bold;
}

.data-percent {
  font-size: 16px;
  position: relative; 
  padding-bottom: 1px;
  cursor: help;

}

.data-percent::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 1px;
  background-color: #D8D8D8;
  transition: background-color 0.3s ease;
}

.data-percent:hover::after {
  background-color: #555555;
}

.tooltip {
  visibility: hidden; 
  background-color: #8e8e8e;
  color: white;
  text-align: center;
  border-radius: 6px;
  padding: 5px 10px;
  position: absolute;
  z-index: 4;
  top: 120%; 
  left: 50%; 
  transform: translateX(-50%);
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.3s, visibility 0.3s;
}

.data-percent:hover .tooltip {
  visibility: visible;
  opacity: 1;
}
</style>
