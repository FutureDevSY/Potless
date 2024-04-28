<template>
  <div class="task-detail-container">
    <div class="header">
      <div>{{ taskHeader.projectId }}</div>
      <div>{{ taskHeader.projectName }}</div>
      <div>담당자 : {{ taskHeader.managerName }}</div>
      <div>작업 건수: {{ taskHeader.projectSize }} 건</div>
      <div>작업 일자: {{ taskHeader.projectDate }}</div>
      <button @click="generatePdf">PDF로 변환하기</button>
    </div>
    <List :data="taskData" />
    <div id="pdf" class="report-pdf">
      <PDFGeneratorMain
        :task-header="taskHeader"
        ref="documentRef"
        class="pdf"
        id="pdf"
      />
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
  const pdfArea = document.getElementById("pdf");
  pdfArea.style.display = "block";

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
      pdfArea.style.display = "none";
    });
}
</script>

<style scoped>
.task-detail-container {
  box-sizing: border-box;
  overflow: hidden;
  max-height: 100vh;
}
.header {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background-color: #f0f0f0;
}

.report-pdf {
  margin: 0%;
  padding: 0%;
}
</style>
