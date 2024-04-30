<template>
  <div class="modal" @click="closeModal">
    <div class="modal-content">
      <span class="close" @click="toggleModal">&times;</span>
      <img src="" alt="" />
      <!-- <div class="carousel-container">
        <button class="prev" @click="prevImage">Prev</button>
        <img :src="currentImage" alt="Carousel Image" />
        <button class="next" @click="nextImage">Next</button>
      </div> -->
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const props = defineProps({
  toggleModal: Function,
  imgInfo: Array,
});

const currentIndex = ref(0);
const defaultImageUrl = "../../../assets/image/default.PNG";

const currentImage = computed(() => {
  return props.imgInfo[currentIndex.value]?.imageUrl || defaultImageUrl;
});

const prevImage = () => {
  currentIndex.value = currentIndex.value === 0 ? props.imgInfo.length - 1 : currentIndex.value - 1;
};

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % props.imgInfo.length;
};

const closeModal = (event) => {
  if (event.target.classList.contains("modal")) {
    props.toggleModal();
  }
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  position: relative;
  width: 80%;
  max-width: 600px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}

.carousel-container {
  position: relative;
  width: 100%;
  background-color: white;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}

.carousel-container img {
  width: 100%;
  height: auto;
  display: block;
  margin: auto;
}

button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: #f4f4f4;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #e2e2e2;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

button.prev {
  left: 10px;
}

button.next {
  right: 10px;
}

.close {
  position: absolute;
  top: 10px;
  right: 20px;
  color: #aaa;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
}

img {
  width: 100%;
}
</style>
