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

const showUpIcon = computed(() => {
  return props.percent.charAt(0) !== "-" && props.percent !== "N/A";
});

const showDownIcon = computed(() => {
  return props.percent.charAt(0) === "-" && props.percent !== "N/A";
});

const formattedPercent = computed(() => {
  return typeof props.percent === "number" ? `${props.percent.toFixed(1)}%` : props.percent;
});

const isPositive = computed(() => {
  return props.percent.charAt(0) === "+";
});

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
  color: #1e476d;
}
.data-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  border-radius: 10px;
  padding: 13% 13%;
  margin: 0px 10px;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.175);
  background-color: #f5f8fe;
  transition: transform 0.4s ease, box-shadow 0.3s ease, border-color 0.4s ease;
}

.data-container:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.296);
  transform: translateY(-5px);
}

.updown-icon {
  padding-top: 4px;
  width: 20px;
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
  font-size: 2vh;
  transform: translateY(-5%);
}

.data-number {
  font-size: 2.2vh;
  font-weight: bold;
}

.data-percent {
  font-size: 2vh;
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
  height: 2px;
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
  padding: 10% 15%;
  position: absolute;
  z-index: 4;
  font-size: 2vh;
  padding: 1vh 1vh;
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
