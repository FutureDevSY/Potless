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

const app = createApp(App);
const pinia = createPinia();

pinia.use(piniaPluginPersist);

app.use(pinia);
app.use(router);
app.use(setupCalendar, { locale: "ko" });

app.component("VDatePicker", DatePicker);
app.component("apexchart", VueApexCharts);

app.mount("#app");
