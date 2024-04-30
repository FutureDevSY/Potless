<template>
  <div class="task-detail-container">
    <div class="header">
      <div class="report-container">
        <div class="report-num">{{ taskHeader.projectId }}</div>
        <div class="report-info">
          <div class="report-name">{{ taskHeader.projectName }}</div>
          <div class="report-total">(총 {{ taskHeader.projectSize }}건)</div>
        </div>
        <div class="report-work">
          <div class="report-worker">담당자 : {{ taskHeader.managerName }}</div>
          <div class="report-date">작업 일자: {{ taskHeader.projectDate }}</div>
        </div>
      </div>

      <button class="pdf-button" @click="generatePdf">PDF로 변환하기</button>
    </div>
    <List :data="taskData" />
    <div id="pdf" class="report-pdf">
      <PDFGeneratorMain :task-header="taskHeader" ref="documentRef" class="pdf" id="pdf" />
      <div v-for="pothole in taskData" :key="pothole.id">
        <PDFGeneratorDetail :pothole="pothole" ref="documentRef" class="pdf" />
      </div>
    </div>
  </div>
</template>

<script setup>
import html2pdf from "html2pdf.js";
import { ref, onMounted } from "vue";
import List from "./components/List.vue";
import data from "./DummyData.json";
import PDFGeneratorMain from "./components/PDFGeneratorMain.vue";
import PDFGeneratorDetail from "./components/PDFGeneratorDetail.vue";

const taskHeader = ref(data.projectResponse);
const taskData = ref(data.projectDetailResponse);
const documentRef = ref(null);

onMounted(() => {
  onMounted(() => {
    if (documentRef.value) {
      console.log("Document ref is available:", documentRef.value);
    } else {
      console.error("Document ref is not available");
    }
  });
});

function generatePdf() {
  const container = document.querySelector(".task-detail-container");
  const pdfArea = document.getElementById("pdf");

  container.style.overflow = "auto";
  container.style.height = "auto";

  pdfArea.style.visibility = "visible";
  pdfArea.style.opacity = "1";

  const options = {
    margin: 0,
    filename: "downloaded.pdf",
    image: { type: "jpeg", quality: 0.98 },
    html2canvas: { scale: 2 },
  };

  html2pdf()
    .set(options)
    .from(pdfArea)
    .save()
    .then(() => {
      pdfArea.style.visibility = "hidden";
      pdfArea.style.opacity = "0";
      container.style.overflow = "hidden";
      container.style.height = "81vh"; // 초기 높이로 복귀
    });
}
</script>

<style scoped>
.task-detail-container {
  margin: 3vh 4vh 0vh 4vh;
  box-sizing: border-box;
  overflow-y: hidden;
  height: 81vh;
}
.header {
  display: flex;
  padding: 1vh 0px 5vh 0px;
  justify-content: space-between;
}

.report-container {
  display: flex;
}

.report-pdf {
  margin: 0%;
  display: block;
  padding: 0%;
  visibility: hidden;
}

.report-num,
.report-info,
.report-total {
  color: #373737;
  font-weight: bold;
}

.report-num {
  font-size: 2.2vh;
  border: 2px solid #373737;
  padding: 0.9vh 5px;
  margin-right: 20px;
}

.report-info {
  display: flex;
  padding-top: 5px;
  margin-right: 4vw;
}

.report-name {
  margin-right: 5px;
  font-size: 2.8vh;
}

.report-total {
  padding-top: 7px;
  font-size: 2.2vh;
}

.report-work {
  color: #5b5b5b;
  font-weight: bold;
  font-size: 1.8vh;
  margin-top: 5px;
}

.report-worker {
  padding-bottom: 3px;
}

.pdf-button {
  background-color: #151c62;
  /* width: 370px;
  height: 45px; */
  cursor: pointer;
  border-radius: 8px;
  font-size: 1.8vh;
  position: relative;
  overflow: hidden;
  color: rgb(255, 255, 255);
  transition: all 0.3s;
  padding: 1vh 1.5vh;
}

.pdf-button:hover {
  background-color: #0e1241;
}
</style>
