<template>
  <div class="task-detail-container">
    <div class="header">
      <div class="report-container">
        <div class="report-num">{{ route.params.id }}</div>
        <div class="report-info">
          <div class="report-name">
            {{ taskHeader ? taskHeader.projectName : "Loading..." }}
          </div>
          <div class="report-total" v-if="taskHeader">
            (총 {{ taskHeader.projectSize }}건)
          </div>
        </div>
        <div class="report-work">
          <div class="report-worker">
            담당자 : {{ taskHeader ? taskHeader.managerName : "Loading..." }}
          </div>
          <div>
            팀이름 : {{ taskHeader?.teamName ? taskHeader.teamName : "미정" }}
          </div>
        </div>
      </div>
      <div>
        <button class="pdf-button" @click="showPath">최적 경로 찾기</button>
        <button class="pdf-button" @click="openPdf">PDF 미리보기</button>
      </div>
    </div>
    <List :data="taskData" v-if="taskData" @updateList="handleUpdateList" />
    <PathModal
      v-if="isModalVisible"
      :pathData="modalData"
      :wayPoint="wayPoint"
      @close="closeModal"
    />

    <div v-if="isPdfModalVisible" class="pdf-modal">
      <div class="modal-content">
        <div class="button-group">
          <button
            class="pdf-button"
            @click="generatePdf"
            v-if="taskData && taskData.length"
          >
            PDF로 변환하기
          </button>
          <button @click="closePdfModal" class="pdf-button">닫기</button>
          <div class="loading-container" v-if="showLoading">
            <LottieLoading />
          </div>
        </div>
        <div id="pdf" class="report-pdf" v-if="taskData && taskData.length">
          <PDFGeneratorMain
            :task-number="taskNumber"
            :task-header="taskHeader"
            ref="documentRef"
            class="pdf"
            id="pdf"
          />
          <div v-for="pothole in taskData" :key="pothole.id">
            <PDFGeneratorDetail
              :pothole="pothole"
              ref="documentRef"
              class="pdf"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import List from "./components/List.vue";
import PDFGeneratorMain from "./components/PDFGeneratorMain.vue";
import PDFGeneratorDetail from "./components/PDFGeneratorDetail.vue";
import { getTaskDetail, postOptimal } from "../../api/task/taskDetail";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import PathModal from "./components/PathModal.vue";
import LottieLoading from "./components/LottieLoading.vue";
import html2pdf from "html2pdf.js";

const route = useRoute();
const store = useAuthStore();
const { accessToken, coordinates } = storeToRefs(store);
const documentRef = ref(null);

const taskNumber = ref(route.params.id);
const taskHeader = ref(null);
const taskData = ref(null);
const showLoading = ref(false);
const modalData = ref(null);
const isModalVisible = ref(false);
const wayPoint = ref(null);
const isPdfModalVisible = ref(false);

onMounted(() => {
  showDetail();
});

const handleUpdateList = () => {
  showDetail();
};

function showDetail() {
  getTaskDetail(
    accessToken.value,
    route.params.id,
    (res) => {
      if (res.data.status === "SUCCESS") {
        console.log(res.data.message);
        taskHeader.value = res.data.data;
        taskData.value = res.data.data.damageDetailToProjectDtos;
      } else {
        console.log(res.data.message);
      }
    },
    (error) => {
      console.error("Error fetching task details:", error);
    }
  );
}

function closeModal() {
  isModalVisible.value = false;
}

function showPath() {
  const pathBody = {
    projectId: taskNumber.value,
    origin: coordinates.value,
  };

  postOptimal(
    accessToken.value,
    pathBody,
    (res) => {
      if (res.data.status === "SUCCESS") {
        modalData.value = res.data.data.routes[0].sections;
        wayPoint.value = res.data.data.routes[0].summary.waypoints;
        isModalVisible.value = true;
      }
    },
    (error) => {
      console.error("Error posting optimal path:", error);
    }
  );
}

function openPdf() {
  isPdfModalVisible.value = true;
}

function closePdfModal() {
  isPdfModalVisible.value = false;
}

function generatePdf() {
  showLoading.value = true;
  setTimeout(() => {
    showLoading.value = false;
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
      .finally(() => {
        closePdfModal();
      });
  }, 5000);
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
  /* visibility: hidden; */
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

.pdf-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 45%;
  max-height: 90vh;
  overflow-y: auto;
  background-color: white;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.button-group {
  display: flex;
  justify-content: end;
}

.loading-container {
  position: absolute;
  top: 50%;
  left: 60%;
  transform: translate(-50%, -50%);
  z-index: 1050;
}
</style>
