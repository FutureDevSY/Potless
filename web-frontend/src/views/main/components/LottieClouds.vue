<template>
  <div
    ref="lottieContainer"
    style="position: absolute; top: 6.5%; left: 12%; width: auto; height: 21vh"
  ></div>
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
    path: "/lottie/clouds.json",
  });

  anim.addEventListener("complete", () => {
    setTimeout(() => {
      anim.goToAndPlay(0);
    }, 5000);
  });

  anim.play();
});

onBeforeUnmount(() => {
  anim.destroy();
});
</script>
