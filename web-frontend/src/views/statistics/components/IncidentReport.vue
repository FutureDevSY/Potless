<template>
  <div class="data-container">
    <div class="data-header">
      <div class="data-title text-light-title">{{ title }}</div>
      <div class="data-number text-light-title">{{ number }}</div>
    </div>
    <div class="data-footer">
      <div class="data-percent .text-light-title">
        <img
          v-if="showUpIcon"
          class="updown-icon up-icon"
          src="../../../assets/icon/up.png"
          alt="Increase"
        />
        <img
          v-if="showDownIcon"
          class="updown-icon down-icon"
          src="../../../assets/icon/down.png"
          alt="Decrease"
        />
        {{ formattedPercent }}
        <div class="tooltip">
          <span v-for="(line, index) in tooltipText" :key="index" class="tooltip-line">
            {{ line }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  title: String,
  number: Number,
  percent: String,
  subtitle: String,
  diff: Number,
});

// 증감률 양수, 음수 일 때 Icon 표시
const showUpIcon = computed(() => {
  return props.percent.charAt(0) !== "-" && props.percent !== "N/A";
});

const showDownIcon = computed(() => {
  return props.percent.charAt(0) === "-" && props.percent !== "N/A";
});

// 증감률을 표시
const formattedPercent = computed(() => {
  return typeof props.percent === "number"
    ? `${props.percent.toFixed(1)}%` // 숫자인 경우 소수점 한 자리까지 표시
    : props.percent; // 문자열인 경우 그대로 표시
});

// 증감률이 양수 판단
const isPositive = computed(() => {
  return props.percent.charAt(0) === "+"; // "+"로 시작하면 양수
});

// 툴팁
const tooltipText = computed(() => {
  let comparisonText;
  let tooltipLines = [];
  switch (props.title) {
    case "오늘":
      comparisonText = "어제";
      break;
    case "최근 1주일":
      comparisonText = "지난 1주일";
      break;
    case "최근 1달":
      comparisonText = "지난 1달";
      break;
  }

  if (props.percent === "N/A") {
    tooltipLines.push("지난 기간동안 감지된 포트홀이 없습니다.");
  } else {
    const changeDirection = props.percent.charAt(0) === "+" ? "올랐습니다" : "내렸습니다";
    tooltipLines.push(`${comparisonText} (${props.diff}건)와/과 비교했을 때`);
    tooltipLines.push(`${props.percent} 만큼 ${changeDirection}`);
  }

  return tooltipLines;
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
  padding: 25px 30px;
  margin: 0px 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
}

.data-container:hover {
  transform: translateY(-5px);
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
  font-size: 17px;
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
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 1px;
  background-color: #d8d8d8;
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
  transition: opacity 0.2s, visibility 0.2s;
}

.tooltip-line {
  display: block;
}

.data-percent:hover .tooltip {
  visibility: visible;
  opacity: 1;
}
</style>
