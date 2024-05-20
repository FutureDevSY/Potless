<template>
  <div class="carousel-container">
    <div v-if="props.teams.length == 0" class="team-container">
      <div>보수 공사팀이 없습니다.</div>
    </div>
    <div v-else class="team-container">
      <Carousel class="carousel-box">
        <Slide v-for="(team, index) in props.teams" :key="index">
          <div class="carousel__item">
            <TeamDetail :team="team"></TeamDetail>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
  </div>
</template>

<script setup>
import { Carousel, Slide, Navigation } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import TeamDetail from "./TeamDetail.vue";

const props = defineProps({
  teams: {
    type: Array,
    required: true,
  },
});
</script>

<style scoped>
.carousel-container {
  height: 100%;
}
.carousel__item {
  width: 100%;
  height: 100%;
}

.team-container {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.carousel-box {
  width: 95%;
}
</style>
