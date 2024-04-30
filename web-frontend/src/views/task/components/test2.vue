<template>
  <div>
    <div ref="contentToPrint" class="print-content">
      <h1>Hello Vue 3 with Script Setup!</h1>
      <p>This content will be printed in PDF format.</p>
    </div>
    <button @click="generatePdf">Generate PDF</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import html2pdf from "html2pdf.js";

const contentToPrint = ref(null);

function generatePdf() {
  const options = {
    margin: 1,
    filename: "downloaded.pdf",
    image: { type: "jpeg", quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: "in", format: "letter", orientation: "portrait" },
  };

  if (contentToPrint.value) {
    html2pdf().set(options).from(contentToPrint.value).save();
  }
}
</script>

<style scoped>
.print-content {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin: 10px 0;
}
</style>
