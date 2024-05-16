<template>
    <div v-if="props.teams.length == 0">
      <div>팀 없음</div>
    </div>
    <div v-else>
      <Carousel :items-to-show="1.5" :wrap-around="true">
        <Slide v-for="(team, index) in props.teams" :key="index">
          <div class="carousel__item">
            <TeamDetail :team="team"></TeamDetail>
          </div>
        </Slide>
      </Carousel>
    </div>
</template>

<script setup>
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import TeamDetail from "./TeamDetail.vue"

const props = defineProps({
  teams: {
    type: Array,
    required: true,
  },
});
</script>

<style scoped></style>