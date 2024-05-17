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
    path: "../../../../public/lottie/clouds.json",
  });

  // 애니메이션을 3초마다 시작하고 멈추기
  anim.addEventListener("complete", () => {
    setTimeout(() => {
      anim.goToAndPlay(0); // 애니메이션이 완료되면 처음부터 재생
    });
  });

  anim.play(); // 최초 재생
});

onBeforeUnmount(() => {
  anim.destroy();
});
</script>
