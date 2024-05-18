import { createApp } from "vue";
import App from "./App.vue";

// Pinia
import { createPinia } from "pinia";
import piniaPluginPersist from "pinia-plugin-persist";
// Router
import router from "./router";
import VueApexCharts from "vue3-apexcharts";
import { setupCalendar, DatePicker } from "v-calendar";
import "v-calendar/style.css";
import VueSweetalert2 from "vue-sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import Vue3lottie from "vue3-lottie";

const app = createApp(App);
const pinia = createPinia();

pinia.use(piniaPluginPersist);

app.use(pinia);
app.use(router);
app.use(setupCalendar, { locale: "ko" });
app.use(VueSweetalert2);
app.use(Vue3lottie, { name: "LottieAnimation" });
app.provide("swal", app.config.globalProperties.$swal);

app.component("VDatePicker", DatePicker);
app.component("apexchart", VueApexCharts);

app.mount("#app");
