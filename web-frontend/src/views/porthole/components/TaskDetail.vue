<template>
  <table>
    <thead>
      <tr>
        <th class="detect-column">탐지 일시</th>
        <th>위험성</th>
        <th>행정동</th>
        <th>도로명</th>
        <th class="delete-column"></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="taskDetail in taskDetails" :key="taskDetail.id">
        <td>
          <div>{{ taskDetail.detect.split(' ')[0] }}</div> <!-- 날짜 -->
          <div>{{ taskDetail.detect.split(' ')[1] }}</div> <!-- 시간 -->
        </td>
        <td :class="dangerClass(taskDetail.danger)">{{ taskDetail.danger }}</td>
        <td>{{ taskDetail.city }}</td>
        <td>{{ taskDetail.road }}</td>
        <td class="remove-button" @click="removeTask(taskDetail.id)">⛔</td>
      </tr>
    </tbody>
  </table>
</template>
  
<script setup>
  import { reactive } from 'vue';
  
  const props = defineProps({
    task: Object
  });

  // 모달창 작업 지시서 디테일: 더미데이터
  const taskDetails = reactive(
    [
    { "id": 1,
      "detect": "2023-01-04 14:22:33",
      "danger": "심각",
      "type": "포트홀",
      "city": "가양동",
      "road": "계족로"
    },
    { "id": 2,
      "detect": "2023-01-05 14:22:33",
      "danger": "주의",
      "type": "포트홀",
      "city": "자양동",
      "road": "우암로"
    },
    {"id": 3,
      "detect": "2023-01-06 14:22:33",
      "danger": "양호",
      "type": "도로파손",
      "city": "가양동",
      "road": "우암로"
    },
    {"id": 4,
      "detect": "2023-01-07 14:22:33",
      "danger": "심각",
      "type": "포트홀",
      "city": "가양동",
      "road": "우암로"
    },
    {"id": 5,
      "detect": "2023-01-08 14:22:33",
      "danger": "심각",
      "type": "포트홀",
      "city": "가양동",
      "road": "계족로"
    },
    {"id": 6,
      "detect": "2023-01-09 14:22:33",
      "danger": "양호",
      "type": "포트홀",
      "city": "자양동",
      "road": "우암로"
    },
    {"id": 7,
      "detect": "2023-01-10 14:22:33",
      "danger": "심각",
      "type": "포트홀",
      "city": "가양동",
      "road": "계족로"
    },
    {"id": 8,
      "detect": "2023-01-11 14:22:33",
      "danger": "주의",
      "type": "도로파손",
      "city": "가양동",
      "road": "계족로"
    },
    {"id": 9,
      "detect": "2023-01-12 14:22:33",
      "danger": "양호",
      "type": "도로파손",
      "city": "자양동",
      "road": "계족로"
    },
    {"id": 10,
      "detect": "2023-01-13 14:22:33",
      "danger": "심각",
      "type": "포트홀",
      "city": "가양동",
      "road": "계족로"
    }
  ]
  );


  // 위험성 필터링
const dangerClass = (danger) => {
  switch (danger) {
    case "심각":
      return "serious";
    case "주의":
      return "cautious";
    case "양호":
      return "safe";
    default:
      return "";
  }
};


// 작업 리스트 삭제
const removeTask = (id) => {
  const index = taskDetails.findIndex(detail => detail.id === id);
  if (index !== -1) {
    taskDetails.splice(index, 1);
  }
};

</script>
  
<style scoped>
.serious {
  color: #f5172d;
}

.cautious {
  color: #FFB800;
}

.safe {
  color: #047c02;
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

th,
td {
  border: 1px solid #ddd;
  text-align: center;
  padding: 15px;
}

thead {
  background-color: #f9f9f9;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

.delete-column {
  width: 70px; 
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
</style>
  