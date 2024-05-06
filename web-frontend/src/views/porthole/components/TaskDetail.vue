<template>
  <table>
    <thead>
      <tr>
        <th class="delete-column"></th>
        <th>종류</th>
        <th>위험성</th>
        <th>행정동</th>
        <th>도로명</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="taskDetail in taskDetails" :key="taskDetail.id">
        <td class="close-cursor" @click="removeTask(taskDetail.id)">
          <span class="close-button">&times;</span>
        </td>
        <td>
          {{ taskDetail.detect }}
        </td>
        <td class="dangers-column">
          <div class="danger-type" :class="dangerClass(taskDetail.danger)">
            <p>{{ taskDetail.danger }}</p>
          </div>
        </td>
        <td>{{ taskDetail.city }}</td>
        <td>{{ taskDetail.road }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import { reactive } from "vue";

const props = defineProps({
  task: Object,
});

// 모달창 작업 지시서 디테일: 더미데이터
const taskDetails = reactive([
  {
    id: 1,
    detect: "POTHOLE",
    danger: 3,
    type: "포트홀",
    city: "가양동",
    road: "계족로",
  },
  {
    id: 2,
    detect: "POTHOLE",
    danger: 2,
    type: "포트홀",
    city: "자양동",
    road: "우암로",
  },
  {
    id: 3,
    detect: "POTHOLE",
    danger: 1,
    type: "도로파손",
    city: "가양동",
    road: "우암로",
  },
  {
    id: 4,
    detect: "POTHOLE",
    danger: 3,
    type: "포트홀",
    city: "가양동",
    road: "우암로",
  },
  {
    id: 5,
    detect: "POTHOLE",
    danger: 3,
    type: "포트홀",
    city: "가양동",
    road: "계족로",
  },
  {
    id: 6,
    detect: "POTHOLE",
    danger: 1,
    type: "포트홀",
    city: "자양동",
    road: "우암로",
  },
  {
    id: 7,
    detect: "POTHOLE",
    danger: 3,
    type: "포트홀",
    city: "가양동",
    road: "계족로",
  },
  {
    id: 8,
    detect: "POTHOLE",
    danger: 2,
    type: "도로파손",
    city: "가양동",
    road: "계족로",
  },
  {
    id: 9,
    detect: "POTHOLE",
    danger: 1,
    type: "도로파손",
    city: "자양동",
    road: "계족로",
  },
  {
    id: 10,
    detect: "POTHOLE",
    danger: 3,
    type: "포트홀",
    city: "가양동",
    road: "계족로",
  },
]);

// 위험성 필터링
const dangerClass = (danger) => {
  switch (danger) {
    case 3:
      return "serious";
    case 2:
      return "cautious";
    case 1:
      return "safe";
    default:
      return "";
  }
};

// 작업 리스트 삭제
const removeTask = (id) => {};
</script>

<style scoped>
.serious {
  background-color: #f5172d;
}

.cautious {
  background-color: #ffb700bf;
}

.safe {
  background-color: #008a1e75;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

td {
  border-top: 1px solid #dddddda1;
  border-bottom: 1px solid #dddddda1;
  border-left: none;
  border-right: none;
  text-align: center;
  padding: 1.7vh;
  font-size: 1.9vh;
  color: #373737;
  background-color: #f1f1f170;
}

thead th {
  position: sticky;
  top: 0;
  background-color: #d3d5ed;
  z-index: 0;
  padding: 1vh 1vh;
  font-size: 1.7vh;
  color: #6c6c6c;
}

tbody tr:hover {
  background-color: #dddddd44;
}

.danger-column {
  width: 5vw;
  text-align: center;
  white-space: nowrap;
}

.danger-type {
  display: inline-block;
  width: 35px;
  height: 35px;
  border-radius: 100%;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  line-height: 42px;
}

p {
  margin: -3px;
}

.delete-column {
  width: 50px;
  min-width: 50px;
  text-align: center;
  white-space: nowrap;
}

.button {
  padding: 5px 10px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  height: 37.78px;
}

.button:hover {
  background-color: #e1e1e1;
}

.remove-button {
  cursor: pointer;
}

.close-button {
  color: #cd0404;
  font-weight: bold;
  font-size: 2.8vh;
}

.close-button:hover {
  color: #8f0000;
}

.close-cursor {
  cursor: pointer;
}
</style>
