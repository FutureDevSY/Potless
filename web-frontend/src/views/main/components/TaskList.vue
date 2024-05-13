<template>
  <div>
    <button @click="takeData('lastWeek')">저번주</button>
    <button @click="takeData('thisWeek')">이번주</button>
    <button @click="takeData('nextWeek')">다음주</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTaskList } from "../../../api/task/taskList";
import { startOfWeek, endOfWeek, addWeeks, subWeeks, format } from "date-fns";

const store = useAuthStore();
const { accessToken, areaId } = storeToRefs(store);
const currentData = ref([]);

function formatDate(date) {
  return format(date, "yyyy-MM-dd");
}

function getWeekRange(type) {
  const now = new Date();
  let start, end;

  switch (type) {
    case "lastWeek":
      start = startOfWeek(subWeeks(now, 1));
      end = endOfWeek(subWeeks(now, 1));
      break;
    case "thisWeek":
      start = startOfWeek(now);
      end = endOfWeek(now);
      break;
    case "nextWeek":
      start = startOfWeek(addWeeks(now, 1));
      end = endOfWeek(addWeeks(now, 1));
      break;
  }

  return { start: formatDate(start), end: formatDate(end) };
}

function takeData(weekType) {
  const { start, end } = getWeekRange(weekType);

  const rawParams = {
    areaId: areaId.value,
    start: start,
    end: end,
    status: "작업전",
    size: 10,
  };

  const queryParams = Object.fromEntries(
    Object.entries(rawParams).filter(
      ([key, value]) => value !== "" && value != null
    )
  );

  getTaskList(
    accessToken.value,
    queryParams,
    (res) => {
      if (res.data.status === "SUCCESS") {
        console.log(res.data.message);
        currentData.value = res.data.data.content;
        console.log(currentData.value);
      } else {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
}
</script>

<style scoped>
button {
  margin-right: 10px;
}
</style>
