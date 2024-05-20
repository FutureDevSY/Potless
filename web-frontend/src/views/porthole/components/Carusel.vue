<template>
  <div class="carousel-container">
    <div v-if="props.images.length == 1">
      <img class="pot-img" :src="props.images[0].url" alt="image" @click="triggerFileInput" />
      <input type="file" ref="fileInput" @change="handleFileChange" style="display: none" />
    </div>
    <div v-else>
      <Carousel
        ref="carousel"
        v-model="currentSlide"
        :items-to-show="1"
        :wrap-around="false"
        @update:modelValue="handleCarouselUpdate"
      >
        <Slide v-for="(image, index) in props.images" :key="index">
          <div class="carousel-img">
            <img class="pot-img" :src="image.url" :alt="'Image ' + (index + 1)" />
          </div>
        </Slide>
      </Carousel>
      <button @click="prev" :disabled="isPreviousDisabled" class="chevron-left"></button>
      <button @click="next" :disabled="isNextDisabled" class="chevron-right"></button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
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

const carousel = ref(null);
const currentSlide = ref(0);
const isPreviousDisabled = ref(true);
const isNextDisabled = ref(false);

const handleFileChange = (event) => {
  const selectedFile = event.target.files.length > 0 ? event.target.files[0] : null;
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
      if (res.data.status == "SUCCESS") {
        emit("uploadComplete", res.data);
        // console.log(res.data.message);
      }
    },
    (error) => {
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

const next = () => {
  carousel.value.next();
};

const prev = () => {
  carousel.value.prev();
};

const handleCarouselUpdate = (slide) => {
  currentSlide.value = slide;
  isPreviousDisabled.value = currentSlide.value === 0;
  isNextDisabled.value = currentSlide.value === props.images.length - 1;
};

onMounted(() => {
  handleCarouselUpdate(0);
});
</script>

<style scoped>
.carousel-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}
.pot-img {
  width: 99%;
  height: 66vh;
  margin-top: 0.5vh;
  /* width: auto;
  height: 515px; */
  object-fit: cover;
  object-position: center;
  transition: transform 0.3s ease;
  border: 2px solid #dfdfdf;
}
.pot-img:hover {
  cursor: pointer;
}

.chevron-left {
  position: absolute;
  top: 50%;
  left: 21.5%;
  width: 8vh;
  height: 8vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-left:after {
  content: "";
  position: absolute;
  top: 40%;
  left: 50%;
  border-style: solid;
  border-color: #575757;
  border-width: 0 7px 7px 0;
  border-radius: 3px;
  display: block;
  padding: 12px;
  transform: translate(-50%, -50%) rotate(135deg);
  transition: transform 0.3s ease;
}

.chevron-left:hover:after {
  transform: translate(-70%, -50%) rotate(135deg);
}
.chevron-right {
  position: absolute;
  top: 50%;
  right: 43%;
  width: 8vh;
  height: 8vh;
  background: transparent;
  border: none;
  cursor: pointer;
}

.chevron-right:after {
  content: "";
  position: absolute;
  top: 40%;
  left: 50%;
  border-style: solid;
  border-color: #575757;
  border-width: 0 7px 7px 0;
  border-radius: 3px;
  display: block;
  padding: 12px;
  transform: translate(-50%, -50%) rotate(-45deg);
  transition: transform 0.3s ease;
}

.chevron-right:hover:after {
  transform: translate(-30%, -50%) rotate(-45deg);
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
</style>
