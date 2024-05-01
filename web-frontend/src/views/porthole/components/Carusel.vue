<template>
  <img class="image" :src="image" alt="#" />
</template>

<script setup>
const props = defineProps({
  image: String,
});
</script>

<style scoped>
.image {
  width: 600px;
  height: 530px;
}
</style>
