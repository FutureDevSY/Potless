<template>
  <div ref="lottieContainer" style="width: auto; height: 21vh"></div>
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
    loop: false,
    autoplay: false,
    path: "/lottie/weather_loading.json",
  });

  anim.addEventListener("complete", () => {
    setTimeout(() => {
      anim.goToAndPlay(0);
    });
  });

  anim.play();
});

onBeforeUnmount(() => {
  anim.destroy();
});
</script>
