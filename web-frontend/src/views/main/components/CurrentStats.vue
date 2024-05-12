<template>
  <div class="main-container">
    <div class="info-container">
      <div class="title">오늘 현황</div>

      <div class="danger">
        <div>
          <img class="bad-img" src="../../../assets/icon/bad.png" alt="bad" />
          <div class="info-text end">심각 - 4건</div>
        </div>
        <div>
          <img class="soso-img" src="../../../assets/icon/soso.png" alt="soso" />
          <div class="info-text mid">보통 - 4건</div>
        </div>
        <div>
          <img class="good-img" src="../../../assets/icon/good.png" alt="good" />
          <div class="info-text start">양호 - 4건</div>
        </div>
      </div>
    </div>
    <div class="type">
      <div class="infos">
        <img class="icons" src="../../../assets/icon/pothole-icon.png" alt="pothole" />
        <div class="name">포트홀</div>
        <div class="number">4건</div>
      </div>
      <div class="infos">
        <img class="icons" src="../../../assets/icon/road-icon.png" alt="road" />
        <div class="name">도로 파손</div>
        <div class="number">4건</div>
      </div>
      <div class="infos">
        <img class="icons" src="../../../assets/icon/line-icon.png" alt="line" />
        <div class="name">도로 마모</div>
        <div class="number">4건</div>
      </div>
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.main-container {
  background-color: rgba(241, 241, 241, 0.641);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.255);
  border-radius: 15px;
  padding: 1.5vh 0vw 0vh 0vw;
  display: grid;
  grid-template-columns: 55% 30%;
  gap: 8%;
}
.title {
  color: #373737;
  font-size: 2vh;
  font-weight: bold;
  padding-left: 1vw;
}
.info-container {
  display: grid;
  grid-template-rows: 12% 82%;
  gap: 6%;
}
.danger {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  padding-left: 5px;
}
.bad-img,
.soso-img,
.good-img {
  height: 11vh;
}
.info-text {
  font-size: 1.8vh;
  text-align: center;
  width: 11vh;
  color: #373737;
  font-weight: bold;
}
.end {
  color: rgb(246, 44, 44);
}
.mid {
  color: rgb(255, 174, 0);
}
.start {
  color: rgb(17, 201, 0);
}
.type {
  padding-top: 1vh;
  display: grid;
  grid-template-rows: 33% 33% 33%;
}
.icons {
  height: 4vh;
}
.infos {
  display: flex;
  padding-bottom: 1px;
}
.name {
  color: #373737;
  padding: 1vh;
  font-size: 1.8vh;
}
.number {
  color: #373737;
  padding: 1vh;
  font-size: 2vh;
  margin-left: auto;
  font-weight: bold;
}
</style>
