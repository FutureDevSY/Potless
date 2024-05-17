<template>
  <div
    ref="lottieContainer"
    style="position: absolute; top: 7.2%; left: 12.5%; width: auto; height: 18vh"
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
    path: "../../../../public/lottie/rain.json",
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
