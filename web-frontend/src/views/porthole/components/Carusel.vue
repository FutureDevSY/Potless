<template>
  <div class="carousel-container">
    <div v-if="props.images.length == 1">
      <img
        class="pot-img"
        :src="props.images[0].url"
        alt="image"
        @click="triggerFileInput"
      />
      <input
        type="file"
        ref="fileInput"
        @change="handleFileChange"
        style="display: none"
      />
    </div>
    <div v-else>
      <Carousel :items-to-show="1.5" :wrap-around="true">
        <Slide v-for="(image, index) in props.images" :key="index">
          <img class="pot-img" :src="image.url" :alt="'Image ' + (index + 1)" />
        </Slide>
      </Carousel>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import { useSwal } from "@/composables/useSwal";
import { postPotholeImageChange } from "../../../api/pothole/pothole";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";

const swal = useSwal();
const fileInput = ref(null);
const store2 = useAuthStore();
const { accessToken } = storeToRefs(store2);
const emit = defineEmits(["uploadComplete"]);

const handleFileChange = (event) => {
  const selectedFile =
    event.target.files.length > 0 ? event.target.files[0] : null;
  if (selectedFile) {
    showAlert();
  }
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const uploadFile = () => {
  const formData = new FormData();
  formData.append("files", fileInput.value.files[0]);
  formData.append("damageId", props.damageId);

  postPotholeImageChange(
    accessToken.value,
    formData,
    (res) => {
      console.log(res);
      if (res.data.status == "SUCCESS") {
        emit("uploadComplete", res.data);
        console.log(res.data.message);
      }
    },
    (error) => {
      console.log(error);
      console.log(error.response.data.message);
    }
  );
};

const showAlert = () => {
  swal({
    title: "도로 파손 이미지를 업로드 하시겠습니까?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "저장",
    cancelButtonText: "취소",
    reverseButtons: true,
    width: "700px",
  }).then((result) => {
    if (result.isConfirmed) {
      uploadFile();
      swal("저장이 완료되었습니다.");
    }
  });
};

const props = defineProps({
  images: {
    type: Array,
    required: true,
  },
  damageId: Number,
});
</script>

<style scoped>
.carousel-container {
  width: 600px;
  height: 515px;
  overflow: hidden;
}
.pot-img {
  width: 400px;
  height: 512px;
  display: block;
}
.pot-img:hover {
  cursor: pointer;
}
.carousel__item {
  width: 400px;
  height: 500px;
}
.carousel__slide {
  padding: 0 5px;
}

.carousel__viewport {
  perspective: 2000px;
}

.carousel__track {
  transform-style: preserve-3d;
}
</style>
