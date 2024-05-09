<template>
  <div ref="lottieContainer" style="width: 5%; height: 5%"></div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref } from "vue";
import lottie from "lottie-web";

const lottieContainer = ref(null);
let anim;

onMounted(() => {
  anim = lottie.loadAnimation({
    container: lottieContainer.value,
    renderer: "svg",
    loop: true,
    autoplay: true,
    path: "../../../../public/lottie/Radar.json",
  });
});

onBeforeUnmount(() => {
  anim.destroy();
});
</script>
