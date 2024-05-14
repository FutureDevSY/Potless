<template>
    <table>
    <thead>
        <tr>
            <!-- <th>작업팀{{ currentData[0].teamId }}</th> -->
            <th class="delete-column"></th>
        </tr>
    </thead>
    <tbody>
      <tr></tr>
    </tbody>
  </table>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/user";
import { storeToRefs } from "pinia";
import { getTeamList } from "../../../api/team/team";

const store = useAuthStore();
const { accessToken, areaName } = storeToRefs(store);
const currentData = ref([]);

function takeData() {
    getTeamList(
        accessToken.value,
        areaName.value,
        (res) => {
            console.log(res.data)
            if (res.data.status === "SUCCESS") {
                currentData.value = res.data.data;
                console.log(currentData.value)
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
<style scoped>
/* tr { display: block; float: left; }
th, td { display: block; } */
</style>
