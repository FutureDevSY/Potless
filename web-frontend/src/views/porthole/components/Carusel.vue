<template>
  <div class="carousel-container">
    <Carousel :items-to-show="1.5" :wrap-around="true">
      <Slide v-for="(image, index) in props.images" :key="index">
        <img class="pot-img" :src="image.url" :alt="'Image ' + (index + 1)" />
      </Slide>
    </Carousel>
  </div>
</template>

<script setup>
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";

const props = defineProps({
  images: {
    type: Array,
    required: true,
  },
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
