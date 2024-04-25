import { createApp } from "vue";
import App from "./App.vue";

// Pinia
import { createPinia } from "pinia";

// Router
import router from "./router";
import VueApexCharts from "vue3-apexcharts";
import { setupCalendar, DatePicker } from "v-calendar";
import "v-calendar/style.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(setupCalendar, { locale: "ko" });

app.component("VDatePicker", DatePicker);
app.component("apexchart", VueApexCharts);

app.mount("#app");
