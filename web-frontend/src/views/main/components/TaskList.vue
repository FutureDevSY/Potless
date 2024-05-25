<template>
  <div class="week-navigation">
    <div class="chevron-navigation">
      <button
        @click="changeWeek('previous')"
        :disabled="isPreviousDisabled"
        class="chevron-left"
      ></button>
    </div>
    <span class="titled">{{ weekLabel }}</span>
    <div class="chevron-navigation">
      <button @click="changeWeek('next')" :disabled="isNextDisabled" class="chevron-right"></button>
    </div>
  </div>
  <div class="all-projects" v-if="currentData.length === 0">
    <div class="no-projects-message">해당 일정에 보고서가 없습니다.</div>
  </div>
  <div class="all-projects" v-else>
    <div
      class="projects"
      v-for="project in currentData"
      :key="project.projectId"
      @click="store2.moveTaskDetail(project.projectId)"
    >
      <div class="pro-name">
        {{ project.projectName }}
      </div>
      <div class="pro-size">( 총 : {{ project.projectSize }}건)</div>
      <div class="detailed">
        <div class="pro-manager">
          담당 <span class="main-text">{{ project.managerName }}</span>
        </div>
        <div class="pro-date">
          작업 예정 : <span class="main-text">{{ project.projectDate }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { useMoveStore } from "@/stores/move";
import { storeToRefs } from "pinia";
import { getTaskList } from "../../../api/task/taskList";
import { startOfWeek, endOfWeek, addWeeks, subWeeks, format } from "date-fns";

const store2 = useMoveStore();
const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);
const currentData = ref([]);
const weekIndex = ref(0);
const weekLabel = ref("이번주 작업 보고서");
const isPreviousDisabled = ref(false);
const isNextDisabled = ref(false);

function formatDate(date) {
  return format(date, "yyyy-MM-dd");
}

function getWeekRange(type) {
  const now = new Date();
  let start, end;

  switch (type) {
    case -1:
      start = startOfWeek(subWeeks(now, 1));
      end = endOfWeek(subWeeks(now, 1));
      weekLabel.value = "저번주 작업 보고서";
      break;
    case 0:
      start = startOfWeek(now);
      end = endOfWeek(now);
      weekLabel.value = "이번주 작업 보고서";
      break;
    case 1:
      start = startOfWeek(addWeeks(now, 1));
      end = endOfWeek(addWeeks(now, 1));
      weekLabel.value = "다음주 작업 보고서";
      break;
  }
  return { start: formatDate(start), end: formatDate(end) };
}

function changeWeek(direction) {
  if (direction === "next" && weekIndex.value < 1) {
    weekIndex.value += 1;
  } else if (direction === "previous" && weekIndex.value > -1) {
    weekIndex.value -= 1;
  }
  takeData();
  updateButtonState();
}

function updateButtonState() {
  isPreviousDisabled.value = weekIndex.value <= -1;
  isNextDisabled.value = weekIndex.value >= 1;
}

function takeData() {
  const { start, end } = getWeekRange(weekIndex.value);
  const rawParams = {
    areaId: areaId.value,
    start: start,
    end: end,
    status: "작업전",
    size: 10,
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(([key, value]) => value !== "" && value != null)
  );

  getTaskList(
    accessToken.value,
    queryParams,
    (res) => {
      if (res.data.status === "SUCCESS") {
        currentData.value = res.data.data.content;
      } else {
        // console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error.response.data.message);
    }
  );
}

onMounted(() => {
  takeData();
  updateButtonState();
});
</script>

<style scoped>
.pro-manager,
.pro-date {
  font-size: 1.7vh;
}
.pro-manager {
  margin-bottom: 0.5vh;
}
.detailed {
  margin-left: 40px;
}
.pro-size {
  font-size: 1.6vh;
  font-weight: 500;
}
.pro-name {
  font-size: 2vh;
  font-weight: bold;
}
.all-projects {
  overflow: auto;
  height: 58vh;
  margin: 0vh 1vw;
}
.projects {
  color: #1e476d;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3vh 0px;
  margin: 0.5vh 0.5vw 1.5vh 0.5vw;
  border-radius: 15px;
  background-color: #f5f8fe;
  cursor: pointer;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.175);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.projects:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.296);
}
button {
  margin-right: 10px;
}

.week-navigation {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 1.5vh 0px 2vh 0px;
  gap: 100px;
}
.titled {
  color: #1e476d;
  font-size: 2.4vh;
  font-weight: bold;
  margin: 1.5vh 0 1vh 0;
}
.chevron-left {
  position: relative;
  width: 2.5vh;
  height: 2.5vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-left:after {
  content: "";
  position: absolute;
  top: 58%;
  left: 150%;
  border-style: solid;
  border-color: #1e476d;
  border-width: 0 4px 4px 0;
  display: block;
  padding: 6px;
  transform: translate(-50%, -50%) rotate(135deg);
  transition: transform 0.3s ease;
  border-radius: 3px;
}

.chevron-left:hover:after {
  transform: translate(-80%, -50%) rotate(135deg);
}
.chevron-right {
  position: relative;
  width: 2.5vh;
  height: 2.5vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-right:after {
  content: "";
  position: absolute;
  top: 58%;
  left: 0%;
  border-style: solid;
  border-color: #1e476d;
  border-width: 0 4px 4px 0;
  display: block;
  padding: 6px;
  transform: translate(-50%, -50%) rotate(-45deg);
  transition: transform 0.3s ease;
  border-radius: 3px;
}

.chevron-right:hover:after {
  transform: translate(-20%, -50%) rotate(-45deg);
}
.chevron-left:disabled:after,
.chevron-right:disabled:after {
  border-color: #ccc;
  cursor: default;
}

.chevron-left:disabled,
.chevron-right:disabled {
  cursor: default;
}
.all-projects::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.all-projects::-webkit-scrollbar-track {
  border-radius: 10px;
}

.all-projects::-webkit-scrollbar-thumb {
  background-color: rgb(205, 205, 205);
  border-radius: 10px;
  border: 2px solid transparent;
}

.all-projects::-webkit-scrollbar-thumb:hover {
  background-color: rgb(187, 187, 187);
}

.no-projects-message {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 90%;
}
</style>
