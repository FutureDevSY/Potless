import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 포트홀 전체 통계
const getStatistics = async (accessToken, success, fail) => {
  await local
    .get(`/damage/statistics`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

export { getStatistics };
