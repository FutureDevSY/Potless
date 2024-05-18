<template>
  <div class="chart">
    <apexchart type="pie" :options="props.options" :series="props.series" />
  </div>
</template>

<script setup>
const props = defineProps({
  options: Object,
  series: Array,
  section: String,
});
</script>

<style scoped>
</style>
