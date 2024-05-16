<template>
    <div class="carusel-container">
        <TeamCarusel :teams="currentData"/>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTeamList } from "../../../api/team/team";
import TeamCarusel from "./TeamCarusel.vue";

const store = useAuthStore();
const { accessToken, areaName } = storeToRefs(store);
const currentData = ref([]);

const takeData = () => {
    getTeamList(
        accessToken.value,
        areaName.value,
        (res) => {
            if (res.data.status === "SUCCESS") {
                currentData.value = res.data.data;
            } else {
                console.log(res.data.message);
            }
        },
        (error) => {
            console.log(error.response.data.message);
        }
    );
}

onMounted(() => {
    takeData();
})
</script>

<style scoped></style>